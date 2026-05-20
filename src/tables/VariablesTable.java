package tables;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Map;

import types.VariableType;


public final class VariablesTable {
  public static final class VariableTableEntry {
    public final String name;
    public final int line;
    public final VariableType type;

    VariableTableEntry(String name, int line, VariableType type) {
      this.name = name;
      this.line = line;
      this.type = type;
    }
  }

  private Map<String, VariableTableEntry> table = new LinkedHashMap<>();

  public boolean lookupVariable(String identifier) {
    return table.containsKey(identifier);
  }

  public void addVariable(String identifier, int line, VariableType type) {
    VariableTableEntry entry = new VariableTableEntry(identifier, line, type);
    table.put(identifier, entry);
  }

  public VariableTableEntry get(String identifier) {
    return table.get(identifier);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    int i = 0;
    for (VariableTableEntry entry : table.values()) {
      f.format(
        "Entry %d -- name: %s, line: %d, type: %s\n",
        i,
        entry.name,
        entry.line,
        entry.type.toString()
      );
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}
