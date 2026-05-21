import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.PascalLexer;
import parser.PascalParser;
import checker.*;

public class Main {
  public static void main(String[] args) throws IOException {

    // Criando um char stream que le todos os caracteres do arquivo
    CharStream input = CharStreams.fromFileName(args[0],StandardCharsets.UTF_8);

    // Criando o lexer que vai consumir esses caracteres
    PascalLexer lexer = new PascalLexer(input);

    // Criando um buffer com os tokens vindos do lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Criando o parser utilizando o buffer de tokens
    PascalParser parser = new PascalParser(tokens);

    // Iniciando o processo de parsing (criando a parser tree)
    ParseTree tree = parser.program();

    // Imprimindo a parser tree (opcional, para debug)
    // System.out.println("Arvore de parsing: " + tree.toStringTree(parser));

    if (parser.getNumberOfSyntaxErrors() != 0) {
      return;
    }
    
    // Criando o analisaador semântico para percorrer a parser tree e fazer as verificações
    // semânticas
    SemanticChecker semanticChecker = new SemanticChecker();
    semanticChecker.visit(tree);
    
    System.out.println("String Literals table:");
    semanticChecker.printLiteralsTable();
    
    System.out.println("Global variables:");
    semanticChecker.printGlobalVariablesTable();

    System.out.println("Procedures and functions:");
    semanticChecker.printProceduresAndFunctionsTable();
  }
}
