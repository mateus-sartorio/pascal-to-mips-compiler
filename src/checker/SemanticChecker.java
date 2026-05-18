package checker;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.Token;

import parser.PascalParserBaseVisitor;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {

  private static class Entry {
      public String varID;
      public String varType;
      public int line;
  }

  // Tabela de símbolos para armazenar as variáveis declaradas no código
  private Map<String, Entry> symbolsTable = new LinkedHashMap<>();

   public String printSymbolsTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("ID\tTYPE\tLINE\n");
    for (Entry entry : symbolsTable.values()) {
        sb.append(entry.varID).append("\t").append(entry.varType).append("\t").append(entry.line).append("\n");
    }
    return sb.toString();
  }

  // Tabela de strings para armazenar as strings literais encontradas no código
  private Set<String> stringsTable = new LinkedHashSet<>();

  public String printStringsTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("STRINGS\n");
    for (String str : stringsTable) {
        sb.append(str).append("\n");
    }
    return sb.toString();
  }

  private void checkVar(Token token) {
    String varID = token.getText();
    if (!symbolsTable.containsKey(varID)) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", token.getLine(), varID);
    }
  }

 
  
}
