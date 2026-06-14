import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ast.AstBuilder;
import parser.PascalLexer;
import parser.PascalParser;
import tables.ProceduresAndFunctionsTable;
import tables.VariablesTable;
import checker.*;

public class Main {
  public static void main(String[] args) throws IOException {
    // Criando um char stream que le todos os caracteres do arquivo
    CharStream input = CharStreams.fromFileName(args[0], StandardCharsets.UTF_8);

    // Criando o lexer que vai consumir esses caracteres
    PascalLexer lexer = new PascalLexer(input);

    // Criando um buffer com os tokens vindos do lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Criando o parser utilizando o buffer de tokens
    PascalParser parser = new PascalParser(tokens);

    // Iniciando o processo de parsing (criando a parser tree)
    ParseTree tree = parser.program();

    if (parser.getNumberOfSyntaxErrors() != 0) {
      return;
    }
    
    // Criando o analisaador semântico para percorrer a parser tree e fazer as verificações semânticas
    SemanticChecker semanticChecker = new SemanticChecker();
    semanticChecker.visit(tree);
    
    semanticChecker.printLiteralsTable();
    semanticChecker.printBuiltInProceduresAndFunctionsTable();
    semanticChecker.printGlobalVariablesTable();
    semanticChecker.printProceduresAndFunctionsTable();

    String programIdentifier = semanticChecker.getProgramIdentifier();
    VariablesTable globalVariablesTable = semanticChecker.getGlobalVariablesTable();
    ProceduresAndFunctionsTable proceduresAndFunctionsTable = semanticChecker.getProceduresAndFunctionsTable();

    AstBuilder astBuilder = new AstBuilder(
      programIdentifier,
      globalVariablesTable,
      proceduresAndFunctionsTable
    );

    astBuilder.visit(tree);

    String dotNotation = astBuilder.toDotNotation();

    Files.writeString(Path.of("test.dot"), dotNotation, StandardCharsets.UTF_8);
  }
}