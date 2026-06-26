package tables;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveTypeEnum;
import types.ProcedureOrFunctionEnum;


public final class BuiltInProceduresAndFunctionsTable {
  public static final class BuiltInProceduresAndFunctionsEntry {
    public final String identifier;
    public final ProcedureOrFunctionEnum type;
    public final PrimitiveTypeEnum returnType;
    public final VariablesTable parameters;

    BuiltInProceduresAndFunctionsEntry(
      String identifier,
      ProcedureOrFunctionEnum type,
      PrimitiveTypeEnum returnType,
      VariablesTable parameters
    ) {
      this.identifier = identifier;
      this.type = type;
      this.returnType = returnType;
      this.parameters = parameters;
    }
  }

  private Map<String, BuiltInProceduresAndFunctionsEntry> table = new LinkedHashMap<>();

  public boolean isEmpty() {
    return table.isEmpty();
  }

  public boolean lookProcedureOrFunction(String identifier) {
    return table.containsKey(identifier.toLowerCase());
  }

  public BuiltInProceduresAndFunctionsEntry get(String identifier) {
    return table.get(identifier.toLowerCase());
  }

  public void addProcedure(String identifier, List<VariableTableEntry> parameterList) {
    addProcedureOrFunction(identifier, ProcedureOrFunctionEnum.PROCEDURE, parameterList, null);
  }

  public void addFunction(String identifier, List<VariableTableEntry> parameterList, PrimitiveTypeEnum returnType) {
    addProcedureOrFunction(identifier, ProcedureOrFunctionEnum.FUNCTION, parameterList, returnType);
  }

  private void addProcedureOrFunction(String identifier, ProcedureOrFunctionEnum type, List<VariableTableEntry> parameterList, PrimitiveTypeEnum returnType) {
    BuiltInProceduresAndFunctionsEntry newEntry = new BuiltInProceduresAndFunctionsEntry(
      identifier,
      type,
      returnType,
      new VariablesTable(parameterList)
    );

    table.put(identifier.toLowerCase(), newEntry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);

    for(BuiltInProceduresAndFunctionsEntry entry : table.values()) {
      f.format(
        "%s '%s'%s\n",
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? "Built-in function" : "Built-in procedure",
        entry.identifier,
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? (", return type: " + entry.returnType.toString()) : ""
      );

      if(!entry.parameters.isEmpty()) {
        f.format("Parameters:\n");
        f.format(entry.parameters.toString());
      }

      f.format("\n");
    }

    f.close();
    
    return sb.toString();
  }
}