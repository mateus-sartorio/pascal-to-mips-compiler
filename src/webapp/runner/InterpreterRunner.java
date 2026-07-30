package webapp.runner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ast.AstBuilder;
import ast.types.ProgramNode;
import checker.SemanticChecker;
import interpreter.Interpreter;
import parser.PascalLexer;
import parser.PascalParser;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable;
import tables.StringLiteralsTable;
import tables.VariablesTable;

/**
 * Ponto de entrada do processo separado que executa um programa Pascal.
 *
 * <p>O interpretador chama {@code System.exit} em qualquer erro de execução e escreve
 * direto em {@code System.out}. Dentro do servidor, uma divisão por zero no programa
 * do usuário derrubaria a aplicação inteira; num processo próprio ela apenas encerra
 * o filho. A saída padrão daqui é a saída do programa, e nada mais.
 *
 * @see webapp.service.InterpreterService
 */
public final class InterpreterRunner {
  /** Código de saída para fonte que não chegou a ser executável. */
  public static final int EXIT_NOT_RUNNABLE = 70;

  private InterpreterRunner() {
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("usage: InterpreterRunner <source-file>");
      System.exit(EXIT_NOT_RUNNABLE);
    }

    String sourceCode = Files.readString(Path.of(args[0]), StandardCharsets.UTF_8);

    // A análise semântica também escreve em System.out. Só a saída do programa deve
    // chegar ao usuário, então as mensagens de preparo vão para um buffer descartado.
    PrintStream programOutput = System.out;
    System.setOut(new PrintStream(new ByteArrayOutputStream(), true, StandardCharsets.UTF_8));

    PascalLexer lexer = new PascalLexer(CharStreams.fromString(sourceCode));
    PascalParser parser = new PascalParser(new CommonTokenStream(lexer));
    ParseTree tree = parser.program();

    if (parser.getNumberOfSyntaxErrors() > 0) {
      System.setOut(programOutput);
      System.exit(EXIT_NOT_RUNNABLE);
    }

    SemanticChecker semanticChecker = new SemanticChecker();
    semanticChecker.visit(tree);

    VariablesTable globalVariablesTable = semanticChecker.getGlobalVariablesTable();
    StringLiteralsTable stringLiteralsTable = semanticChecker.getStringLiteralsTable();
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = semanticChecker.getBuiltInProceduresAndFunctionsTable();
    ProceduresAndFunctionsTable proceduresAndFunctionsTable = semanticChecker.getProceduresAndFunctionsTable();

    AstBuilder astBuilder = new AstBuilder(
      semanticChecker.getProgramIdentifier(),
      globalVariablesTable,
      builtInProceduresAndFunctionsTable,
      proceduresAndFunctionsTable
    );

    ProgramNode programNode = (ProgramNode) astBuilder.visit(tree);

    // A partir daqui tudo o que for impresso é do programa.
    System.setOut(programOutput);

    new Interpreter(
      globalVariablesTable,
      stringLiteralsTable,
      builtInProceduresAndFunctionsTable,
      proceduresAndFunctionsTable,
      programNode
    ).execute();
  }
}
