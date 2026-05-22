package tables;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import types.VariableType;


public final class VariablesTable {
  public static final class VariableTableEntry {
    public final String name;
    public final int line;
    public final VariableType type;

    public VariableTableEntry(String name, int line, VariableType type) {
      this.name = name;
      this.line = line;
      this.type = type;
    }
  }

  private Map<String, VariableTableEntry> table = new LinkedHashMap<>();

  public VariablesTable() {
    table = new LinkedHashMap<>();
  }

  public VariablesTable(List<VariableTableEntry> list) {
    table = new LinkedHashMap<>();

    for(VariableTableEntry entry : list) {
      table.put(entry.name, entry);
    }
  }

  public boolean isEmpty() {
    return table.isEmpty();
  }

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
        "%d - name: %s%s, type: %s\n",
        i,
        entry.name,
        entry.line >= 0 ? ", line: " + entry.line : "",
        entry.type.toString()
      );
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}
