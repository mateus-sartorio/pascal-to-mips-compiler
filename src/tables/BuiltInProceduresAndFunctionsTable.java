package tables;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveType;
import types.ProcedureOrFunctionEnum;


public final class BuiltInProceduresAndFunctionsTable {
  public static final class BuiltInProceduresAndFunctionsEntry {
    public final String name;
    public final ProcedureOrFunctionEnum type;
    public final PrimitiveType returnType;
    public final VariablesTable parameters;

    BuiltInProceduresAndFunctionsEntry(
      String name,
      int line,
      ProcedureOrFunctionEnum type,
      PrimitiveType returnType,
      VariablesTable parameters
    ) {
      this.name = name;
      this.type = type;
      this.returnType = returnType;
      this.parameters = parameters;
    }
  }

  private Map<String, List<BuiltInProceduresAndFunctionsEntry>> table = new LinkedHashMap<>();

  public boolean isEmpty() {
    return table.isEmpty();
  }

  public boolean lookProcedureOrFunction(String identifier) {
    return table.containsKey(identifier);
  }

  public List<BuiltInProceduresAndFunctionsEntry> get(String identifier) {
    return table.get(identifier);
  }

  public void addProcedure(String identifier, List<VariableTableEntry> parameterList) {
    addProcedureOrFunction(identifier, ProcedureOrFunctionEnum.PROCEDURE, null, parameterList);
  }

  public void addFunction(String identifier, PrimitiveType returnType, List<VariableTableEntry> parameterList) {
    addProcedureOrFunction(identifier, ProcedureOrFunctionEnum.FUNCTION, returnType, parameterList);
  }

  private void addProcedureOrFunction(String identifier, ProcedureOrFunctionEnum type, PrimitiveType returnType, List<VariableTableEntry> parameterList) {
    BuiltInProceduresAndFunctionsEntry newEntry = new BuiltInProceduresAndFunctionsEntry(
      identifier,
      -1,
      type,
      returnType,
      new VariablesTable(parameterList)
    );

    List<BuiltInProceduresAndFunctionsEntry> entries;

    if(table.containsKey(identifier)) {
      entries = table.get(identifier);
      entries.add(newEntry);
    }
    else {
      entries = List.of(newEntry);
    }
    
    table.put(identifier, entries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);

    for(List<BuiltInProceduresAndFunctionsEntry> entriesList : table.values()) {
      for(BuiltInProceduresAndFunctionsEntry entry : entriesList) {
        f.format(
          "%s '%s'%s\n",
          entry.type == ProcedureOrFunctionEnum.FUNCTION ? "Built-In Function" : "Built-In Procedure",
          entry.name,
          entry.type == ProcedureOrFunctionEnum.FUNCTION ? (", return type: " + entry.returnType.toString()) : ""
        );
  
        if(!entry.parameters.isEmpty()) {
          f.format("Parameters:\n");
          f.format(entry.parameters.toString());
        }

        f.format("\n");
      }
    }
    f.close();
    
    return sb.toString();
  }
}
