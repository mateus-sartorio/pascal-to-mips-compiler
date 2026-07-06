package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import types.VariableType;


public final class VariablesTable {
  public static final class VariableTableEntry {
    public final String identifier;
    public final Integer line;
    public final VariableType type;

    public VariableTableEntry(String identifier, int line, VariableType type) {
      this.identifier = identifier;
      this.line = line;
      this.type = type;
    }

    public VariableTableEntry(String name, VariableType type) {
      this.identifier = name;
      this.line = null;
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
      table.put(entry.identifier.toLowerCase(), entry);
    }
  }

  public boolean isEmpty() {
    return table.isEmpty();
  }

  public int size() {
    return table.size();
  }

  public boolean lookupVariable(String identifier) {
    return table.containsKey(identifier.toLowerCase());
  }

  public void addVariable(String identifier, int line, VariableType type) {
    VariableTableEntry entry = new VariableTableEntry(identifier, line, type);
    table.put(identifier.toLowerCase(), entry);
  }

  public VariableTableEntry get(String identifier) {
    return table.get(identifier.toLowerCase());
  }

  public int getIndex(String identifier) {
    return new ArrayList<>(table.keySet()).indexOf(identifier.toLowerCase());
  }

  public List<VariableTableEntry> toList() {
    return new ArrayList<VariableTableEntry>(table.values());
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    int i = 0;
    for (VariableTableEntry entry : table.values()) {
      f.format(
        "%d. identifier: '%s'%s, type: %s\n",
        i,
        entry.identifier,
        entry.line != null ? ", line: " + entry.line : "",
        entry.type.toString()
      );
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}