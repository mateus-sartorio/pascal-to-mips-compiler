package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import types.PrimitiveType;


public final class VariablesTable {
  public static final class Type {
    private final boolean isPrimitive;
    private final PrimitiveType primitiveType;
    private final int startIndex;
    private final int endIndex;

    public Type(PrimitiveType primitiveType) {
      this.isPrimitive = true;
      this.primitiveType = primitiveType;
      this.startIndex = 0;
      this.endIndex = 0;
    }
  }
  
  private static final class Entry {
    private final String name;
    private final int line;
    private final Type type;

    Entry(String name, int line, Type type) {
      this.name = name;
      this.line = line;
      this.type = type;
    }
  }

  private List<Entry> table = new ArrayList<Entry>();

  public int lookupVariable(String symbol) {
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).name.equals(symbol)) {
        return i;
      }
    }

    return -1;
  }

  public int addVariable(String s, int line, Type type) {
    Entry entry = new Entry(s, line, type);
    int indexAdded = table.size();
    table.add(entry);
    return indexAdded;
  }

  public String getName(int i) {
    return table.get(i).name;
  }

  public int getLine(int i) {
    return table.get(i).line;
  }

  public Type getType(int i) {
    return table.get(i).type;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    f.format("Variables table:\n");
    for (int i = 0; i < table.size(); i++) {
      f.format("Entry %d -- name: %s, line: %d, type: %s\n", i, getName(i), getLine(i), getType(i).toString());
    }
    f.close();
    
    return sb.toString();
  }
}
