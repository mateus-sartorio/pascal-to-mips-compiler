package webapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

import org.springframework.stereotype.Service;

import webapp.dto.ExecutionDto;
import webapp.runner.InterpreterRunner;

/**
 * Executa o programa do usuário numa JVM separada e devolve o que ele imprimiu.
 *
 * <p>Rodar o interpretador dentro do servidor não é possível: ele chama
 * {@code System.exit} em qualquer erro de execução, de modo que uma divisão por zero
 * no programa do usuário encerraria a aplicação. Num processo separado, o mesmo
 * {@code System.exit} apenas termina o filho, a entrada e a saída padrão são de
 * verdade (sem trocar o estado global da JVM), e um laço interminável pode ser morto
 * de fato — coisa que uma thread não permite desde que {@code Thread.stop} deixou de
 * existir.
 */
@Service
public class InterpreterService {
  private static final long TIMEOUT_SECONDS = 5;
  private static final int OUTPUT_LIMIT_BYTES = 64 * 1024;
  private static final int STDERR_LIMIT_BYTES = 4 * 1024;
  private static final String LAUNCHER = "org.springframework.boot.loader.launch.PropertiesLauncher";

  /**
   * Compila e executa o programa num processo próprio.
   *
   * @param sourceCode o programa Pascal, que já compilou no processo principal
   * @param standardInput o texto oferecido à entrada padrão, que pode ser nulo
   * @return o que o programa imprimiu, junto do motivo de uma eventual parada
   */
  public ExecutionDto run(String sourceCode, String standardInput) {
    Path sourceFile = null;

    try {
      sourceFile = Files.createTempFile("pascal-run-", ".pas");
      Files.writeString(sourceFile, sourceCode, StandardCharsets.UTF_8);

      return execute(sourceFile, standardInput == null ? "" : standardInput);
    } catch (IOException exception) {
      return new ExecutionDto("", false, false, "Could not start the program: " + exception.getMessage());
    } finally {
      deleteQuietly(sourceFile);
    }
  }

  private ExecutionDto execute(Path sourceFile, String standardInput) throws IOException {
    Process process = new ProcessBuilder(command(sourceFile)).start();

    BoundedOutputStream output = new BoundedOutputStream(OUTPUT_LIMIT_BYTES);
    BoundedOutputStream errors = new BoundedOutputStream(STDERR_LIMIT_BYTES);

    // Os três fluxos são atendidos fora desta thread: escrever toda a entrada antes de
    // ler a saída travaria assim que o programa enchesse o buffer do pipe.
    Thread writer = Thread.ofVirtual().start(() -> writeInput(process, standardInput));
    Thread reader = Thread.ofVirtual().start(() -> drain(process.getInputStream(), output));
    Thread errorReader = Thread.ofVirtual().start(() -> drain(process.getErrorStream(), errors));

    boolean finished;
    try {
      finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);

      if (!finished) {
        // Um laço sem fim morre aqui de verdade, e não como uma thread abandonada.
        process.destroyForcibly();
        process.waitFor();
      }

      writer.join(1000);
      reader.join(1000);
      errorReader.join(1000);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      process.destroyForcibly();
      return new ExecutionDto(output.toString(), false, output.isTruncated(), "The run was interrupted.");
    }

    return new ExecutionDto(
      output.toString(),
      !finished,
      output.isTruncated(),
      finished ? describe(process.exitValue(), errors.toString()) : null
    );
  }

  /**
   * Monta a linha de comando do processo filho.
   *
   * <p>Quando a aplicação roda a partir do jar empacotado, as classes ficam sob
   * {@code BOOT-INF/classes} e um {@code -cp} comum não as encontra, então o filho é
   * iniciado pelo carregador do Spring Boot. Rodando por um classpath normal, como no
   * IDE, a classe é chamada direto.
   */
  private List<String> command(Path sourceFile) {
    String classPath = System.getProperty("java.class.path");

    List<String> command = new ArrayList<>();
    command.add(javaExecutable());
    // Um programa que aloca sem parar esbarra aqui em vez de na memória da máquina.
    command.add("-Xmx256m");
    command.add("-cp");
    command.add(classPath);

    if (isPackagedJar(classPath)) {
      command.add("-Dloader.main=" + InterpreterRunner.class.getName());
      command.add(LAUNCHER);
    } else {
      command.add(InterpreterRunner.class.getName());
    }

    command.add(sourceFile.toString());

    return command;
  }

  private String javaExecutable() {
    return ProcessHandle.current().info().command()
      .orElseGet(() -> Path.of(System.getProperty("java.home"), "bin", "java").toString());
  }

  private boolean isPackagedJar(String classPath) {
    if (classPath == null || classPath.contains(java.io.File.pathSeparator) || !classPath.endsWith(".jar")) {
      return false;
    }

    try (JarFile jar = new JarFile(classPath)) {
      return jar.getEntry("BOOT-INF/classes/") != null;
    } catch (IOException exception) {
      return false;
    }
  }

  /**
   * Traduz o desfecho do processo em algo que o usuário possa agir a respeito.
   */
  private String describe(int exitCode, String errorOutput) {
    if (exitCode == 0) {
      return null;
    }

    if (errorOutput.contains("NoSuchElementException")) {
      return "The program asked for more input than was provided.";
    }

    if (errorOutput.contains("InputMismatchException")) {
      return "The input does not match the type the program tried to read.";
    }

    if (errorOutput.contains("StackOverflowError")) {
      return "Ran out of stack: the program recursed too deeply.";
    }

    if (errorOutput.contains("OutOfMemoryError")) {
      return "The program ran out of memory.";
    }

    if (exitCode == InterpreterRunner.EXIT_NOT_RUNNABLE) {
      return "The program could not be prepared for execution.";
    }

    // O interpretador imprime o motivo antes de sair com 1, então a saída já explica.
    return exitCode == 1 ? "The program stopped with a runtime error." : "The program exited with status " + exitCode + ".";
  }

  private void writeInput(Process process, String standardInput) {
    try (OutputStream stdin = process.getOutputStream()) {
      stdin.write(standardInput.getBytes(StandardCharsets.UTF_8));
    } catch (IOException exception) {
      // O programa terminou sem ler tudo; o código de saída conta o que houve.
    }
  }

  private void drain(InputStream source, OutputStream destination) {
    try (InputStream stream = source) {
      stream.transferTo(destination);
    } catch (IOException exception) {
      // O processo foi morto e o pipe fechou: o que já chegou continua válido.
    }
  }

  private void deleteQuietly(Path path) {
    if (path == null) {
      return;
    }

    try {
      Files.deleteIfExists(path);
    } catch (IOException exception) {
      // Arquivo temporário; o sistema operacional cuida do resto.
    }
  }

  /**
   * Acumula a saída até um limite e descarta o excesso, para que um laço que imprime
   * sem parar não consuma a memória do servidor.
   */
  private static final class BoundedOutputStream extends OutputStream {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final int limit;
    private boolean truncated;

    private BoundedOutputStream(int limit) {
      this.limit = limit;
    }

    @Override
    public synchronized void write(int value) {
      if (buffer.size() < limit) {
        buffer.write(value);
        return;
      }

      truncated = true;
    }

    @Override
    public synchronized void write(byte[] bytes, int offset, int length) {
      int room = Math.min(length, limit - buffer.size());

      if (room > 0) {
        buffer.write(bytes, offset, room);
      }

      if (room < length) {
        truncated = true;
      }
    }

    private synchronized boolean isTruncated() {
      return truncated;
    }

    @Override
    public synchronized String toString() {
      return buffer.toString(StandardCharsets.UTF_8);
    }
  }
}
