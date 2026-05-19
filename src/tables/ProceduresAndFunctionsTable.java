package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import types.PrimitiveType;


public final class ProceduresAndFunctionsTable {
  private enum Type {
    PROCEDURE,
    FUNCTION
  }

  private static final class Entry {
    private final String name;
    private final int line;
    private final Type type;
    private final PrimitiveType returnType;
    private final List<TypesTable> parameters;
    private final List<TypesTable> localVariables;


    Entry(String name, int line, PrimitiveType returnType, List<PrimitiveType> argTypes) {
      this.name = name;
      this.line = line;
      this.returnType = returnType;
      this.argTypes = argTypes;
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

  public int addVariable(String s, int line, PrimitiveType type, List<PrimitiveType> argTypes) {
    Entry entry = new Entry(s, line, type, argTypes);
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

  public PrimitiveType getReturnType(int i) {
    return table.get(i).returnType;
  }

  public List<PrimitiveType> getArgTypes(int i) {
    return table.get(i).argTypes;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    f.format("Variables table:\n");
    for (int i = 0; i < table.size(); i++) {
      f.format("Entry %d -- name: %s, line: %d, type: %s\n", i, getName(i), getLine(i), getReturnType(i).toString());
    }
    f.close();
    
    return sb.toString();
  }
}
