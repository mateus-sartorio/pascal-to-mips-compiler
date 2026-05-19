import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import checker.*;
import parser.*;

public class App {
  public static void main(String[] args) {

    try {
      // Criando um char stream que le todos os caracteres do arquivo
      CharStream input = CharStreams.fromFileName(args[0]);

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

      if (parser.getNumberOfSyntaxErrors() == 0) {

        // Criando o visitor para percorrer a parser tree e fazer as verificações
        // semânticas
        SemanticChecker visitor = new SemanticChecker();
        visitor.visit(tree);

        System.out.println("Análise semântica concluída com sucesso!\n");
        System.out.println("Strings table: \n" + visitor.printStringsTable());
        System.out.println("Symbols table: \n" + visitor.printSymbolsTable());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
