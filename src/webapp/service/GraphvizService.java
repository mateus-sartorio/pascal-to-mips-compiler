package webapp.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 * Renders Graphviz DOT source into a PNG image by piping it through the {@code dot}
 * executable.
 *
 * <p>Rendering is best effort. When Graphviz is not installed, or the graph takes too
 * long to lay out, the service returns {@code null} and the front end falls back to
 * displaying the DOT source as text.
 */
@Service
public class GraphvizService {
  private static final long RENDER_TIMEOUT_SECONDS = 10;
  private static final long STREAM_DRAIN_TIMEOUT_MILLISECONDS = 1000;

  /**
   * Styling applied at render time so that {@code AstBuilder} keeps emitting plain,
   * portable DOT. The colours mirror the palette in {@code styles.css}, and the raised
   * DPI keeps the graph sharp when the browser scales it down.
   */
  private static final List<String> THEME_OPTIONS = List.of(
    "-Gbgcolor=transparent",
    "-Nstyle=filled",
    "-Nfillcolor=#131d33",
    "-Ncolor=#2c3d63",
    "-Nfontcolor=#e9eefb",
    "-Nfontname=Helvetica",
    "-Nfontsize=12",
    "-Npenwidth=1.2",
    "-Ecolor=#5c6f96",
    "-Efontcolor=#9aa7c2",
    "-Efontname=Helvetica",
    "-Efontsize=10",
    "-Epenwidth=1.2"
  );

  /**
   * Renders the given DOT source and encodes the resulting PNG as Base64, ready to be
   * dropped into an {@code <img src="data:image/png;base64,...">} attribute.
   *
   * @param dotSource the graph description, or null
   * @return the Base64 encoded PNG, or null when the graph could not be rendered
   */
  public String renderPngAsBase64(String dotSource) {
    byte[] png = render(dotSource, "-Tpng", "-Gdpi=144");
    return png == null ? null : Base64.getEncoder().encodeToString(png);
  }

  /**
   * Renderiza o grafo como SVG. Preferível a PNG para grafos grandes: a árvore de
   * derivação de um programa curto passa de 1,4 MB para 196 KB e continua legível em
   * qualquer nível de zoom, por ser vetorial.
   *
   * @param dotSource a descrição do grafo, ou null
   * @return o SVG, ou null quando o grafo não pôde ser renderizado
   */
  public String renderSvg(String dotSource) {
    byte[] svg = render(dotSource, "-Tsvg");
    return svg == null ? null : new String(svg, StandardCharsets.UTF_8);
  }

  private byte[] render(String dotSource, String... formatOptions) {
    if (dotSource == null || dotSource.isBlank()) {
      return null;
    }

    List<String> command = new ArrayList<>();
    command.add("dot");
    command.addAll(List.of(formatOptions));
    command.addAll(THEME_OPTIONS);

    Process process;
    try {
      process = new ProcessBuilder(command)
        // Warnings are irrelevant here, and discarding them means no extra thread is
        // needed to keep the child from blocking on a full stderr buffer.
        .redirectError(ProcessBuilder.Redirect.DISCARD)
        .start();
    } catch (IOException exception) {
      // Graphviz is not installed on this machine.
      return null;
    }

    // Both pipes are serviced off the calling thread. Writing the whole graph before
    // reading any output would deadlock as soon as the PNG outgrows the pipe buffer.
    Thread writer = Thread.ofVirtual().start(() -> writeSource(process, dotSource));
    ByteArrayOutputStream png = new ByteArrayOutputStream();
    Thread reader = Thread.ofVirtual().start(() -> readOutput(process, png));

    try {
      if (!process.waitFor(RENDER_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
        // Killing the process closes both pipes, which releases the two helpers.
        process.destroyForcibly();
        return null;
      }

      writer.join(STREAM_DRAIN_TIMEOUT_MILLISECONDS);
      reader.join(STREAM_DRAIN_TIMEOUT_MILLISECONDS);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      process.destroyForcibly();
      return null;
    }

    byte[] rendered = png.toByteArray();
    return process.exitValue() == 0 && rendered.length > 0 ? rendered : null;
  }

  private void writeSource(Process process, String dotSource) {
    try (OutputStream stdin = process.getOutputStream()) {
      stdin.write(dotSource.getBytes(StandardCharsets.UTF_8));
    } catch (IOException exception) {
      // dot exited before consuming the whole graph; the exit code reports the failure.
    }
  }

  private void readOutput(Process process, ByteArrayOutputStream destination) {
    try (InputStream stdout = process.getInputStream()) {
      stdout.transferTo(destination);
    } catch (IOException exception) {
      // The pipe was closed early, leaving a truncated image that is discarded below.
    }
  }
}
