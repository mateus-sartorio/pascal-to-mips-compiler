import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import parser.PascalLexer;
import parser.PascalParser;

public class Main {
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


      // acho que não precisa por que ja tem o catch
      if (parser.getNumberOfSyntaxErrors() != 0) {
        
        System.out.println("Tiveram erros sintaticos, encerrando a compilação");
        return;
      }

      SemanticChecker 


    } catch (Exception e) {
      System.out.println("Erro na compilação");
    }
    

    



  }
}
