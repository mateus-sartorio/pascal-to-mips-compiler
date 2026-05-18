package checker;

import java.util.LinkedHashMap;
import java.util.Map;

import parser.PascalParserBaseVisitor;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {

  private static class Entry {
      public String varID;
      public String varType;
      public int line;
  }

  private Map<String, Entry> symbolsTable = new LinkedHashMap<>();

   public String printSymbolsTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("ID\tTYPE\tLINE\n");
    for (Entry entry : symbolsTable.values()) {
        sb.append(entry.varID).append("\t").append(entry.varType).append("\t").append(entry.line).append("\n");
    }
    return sb.toString();
  }
  public String printStringsTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("ID\tVALUE\tLINE\n");
    for (Entry entry : symbolsTable.values()) {
        sb.append(entry.varID).append("\t").append(entry.varType).append("\t").append(entry.line).append("\n");
    }
    return sb.toString();
  }

 
  
}
