package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import tables.VariablesTable.VariableType;
import types.PrimitiveType;


public final class ProceduresAndFunctionsTable {
  private enum ProcedureOrFunctionType {
    PROCEDURE,
    FUNCTION
  }

  private static final class Entry {
    private final String name;
    private final int line;
    private final ProcedureOrFunctionType type;
    private final PrimitiveType returnType;
    public final VariablesTable parameters;
    public final VariablesTable localVariables;


    Entry(
      String name,
      int line,
      ProcedureOrFunctionType type,
      PrimitiveType returnType,
      VariablesTable parameters,
      VariablesTable localVariables
    ) {
      this.name = name;
      this.line = line;
      this.type = type;
      this.returnType = returnType;
      this.parameters = parameters;
      this.localVariables = localVariables;
    }
  }

  private List<Entry> table = new ArrayList<Entry>();

  public int lookProcedureOrFunction(String procedureOrFunctionIdentifier) {
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).name.equals(procedureOrFunctionIdentifier)) {
        return i;
      }
    }

    return -1;
  }

  public void addProcedure(String procedureName, int line) {
    Entry entry = new Entry(
      procedureName,
      line,
      ProcedureOrFunctionType.PROCEDURE,
      null,
      null,
      null
    );

    table.add(entry);
  }

  public void addProcedlureOrFunctionParameter(
    String procedureOrFunctionName,
    String parameterName,
    int line,
    VariableType type
  ) {
    
    int index = -1;
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).name.equals(procedureOrFunctionName)) {
        index = i;
        break;
      }
    }

    VariablesTable parameters = getParameters(index);
    parameters.addVariable(parameterName, line, type);
  }

  public void addProcedlureOrFunctionVariable(
    String procedureOrFunctionIdentifier,
    String variableIdentifier,
    int line,
    VariableType type
  ) {
    
    int index = -1;
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).name.equals(procedureOrFunctionIdentifier)) {
        index = i;
        break;
      }
    }

    VariablesTable locVariablesTable = getLocalVariables(index);
    locVariablesTable.addVariable(variableIdentifier, line, type);
  }

  public int lookupVariable(String symbol, String procedureOrFunctionIdentifier) {
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).name.equals(symbol)) {
        return i;
      }
    }

    return -1;
  }

    public int lookupProcedureOrFunction(String procedureOrFunctionIdentifier) {
    for (int i = 0; i < table.size(); i++) {
      if (table.get(i).name.equals(procedureOrFunctionIdentifier)) {
        return i;
      }
    }

    return -1;
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

  public VariablesTable getParameters(int i) {
    return table.get(i).parameters;
  }

  public VariablesTable getLocalVariables(int i) {
    return table.get(i).localVariables;
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
