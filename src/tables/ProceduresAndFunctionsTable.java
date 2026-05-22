package tables;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Map;

import types.PrimitiveType;
import types.ProcedureOrFunctionEnum;
import types.VariableType;


public final class ProceduresAndFunctionsTable {
  public static final class ProceduresAndFunctionsEntry {
    public final String name;
    public final int line;
    public final ProcedureOrFunctionEnum type;
    public final PrimitiveType returnType;
    public final VariablesTable parameters;
    public final VariablesTable localVariables;

    ProceduresAndFunctionsEntry(
      String name,
      int line,
      ProcedureOrFunctionEnum type,
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

  private Map<String, ProceduresAndFunctionsEntry> table = new LinkedHashMap<>();

  public boolean isEmpty() {
    return table.isEmpty();
  }

  public boolean lookProcedureOrFunction(String identifier) {
    return table.containsKey(identifier);
  }

  public ProceduresAndFunctionsEntry get(String identifier) {
    return table.get(identifier);
  }

  public boolean lookupProcedureOrFunctionLocalVariable(
    String procedureOrFunctionIdentifier,
    String localVariableIdentifier
  ) {
    assert table.containsKey(procedureOrFunctionIdentifier);

    ProceduresAndFunctionsEntry entry = table.get(procedureOrFunctionIdentifier);

    VariablesTable localVariables = entry.localVariables;

    return localVariables.lookupVariable(localVariableIdentifier);
  }

  public boolean lookupProcedureOrFunctionLocalParameter(
    String procedureOrFunctionIdentifier,
    String localVariableIdentifier
  ) {
    assert table.containsKey(procedureOrFunctionIdentifier);

    ProceduresAndFunctionsEntry entry = table.get(procedureOrFunctionIdentifier);

    VariablesTable parameters = entry.parameters;

    return parameters.lookupVariable(localVariableIdentifier);
  }

  public void addProcedure(String identifier, int line) {
    ProceduresAndFunctionsEntry entry = new ProceduresAndFunctionsEntry(
      identifier,
      line,
      ProcedureOrFunctionEnum.PROCEDURE,
      null,
      new VariablesTable(),
      new VariablesTable()
    );

    table.put(identifier, entry);
  }

  public void addFunction(String identifier, int line, PrimitiveType type) {
    ProceduresAndFunctionsEntry entry = new ProceduresAndFunctionsEntry(
      identifier,
      line,
      ProcedureOrFunctionEnum.FUNCTION,
      type,
      new VariablesTable(),
      new VariablesTable()
    );

    table.put(identifier, entry);
  }

  public void addProcedlureOrFunctionParameter(
    String procedureOrFunctionIdentifier,
    String parameterIdentifier,
    int line,
    VariableType type
  ) {
    
    ProceduresAndFunctionsEntry entry = table.get(procedureOrFunctionIdentifier);

    assert entry != null;

    VariablesTable parameters = entry.parameters;

    assert !(parameters.lookupVariable(parameterIdentifier));

    parameters.addVariable(parameterIdentifier, line, type);
  }

  public void addProcedlureOrFunctionVariable(
    String procedureOrFunctionIdentifier,
    String variableIdentifier,
    int line,
    VariableType type
  ) {
    ProceduresAndFunctionsEntry entry = table.get(procedureOrFunctionIdentifier);

    assert entry != null;

    VariablesTable localVariables = entry.localVariables;

    assert !(localVariables.lookupVariable(variableIdentifier));

    localVariables.addVariable(variableIdentifier, line, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);

    
    for(ProceduresAndFunctionsEntry entry : table.values()) {
      f.format(
        "%s %s - line %d%s\n",
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? "Function" : "Procedure",
        entry.name,
        entry.line,
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? (", return type: " + entry.returnType.toString()) : ""
      );

      if(!entry.parameters.isEmpty()) {
        f.format("Parameters:\n");
        f.format(entry.parameters.toString());
      }

      if(!entry.localVariables.isEmpty()) {
        f.format("Local variables:\n");
        f.format(entry.localVariables.toString());
      }
    }
    f.close();
    
    return sb.toString();
  }
}
