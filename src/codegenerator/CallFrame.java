package codegenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.ArrayVariableType;
import types.PrimitiveVariableType;

public class CallFrame {
  public record LocalVariableEntry(int offset, int size) {}

  private final Map<String, LocalVariableEntry> map;

  public CallFrame(ProceduresAndFunctionsTable proceduresAndFunctionsTable) {
    this.map = new HashMap<>();

    for(ProceduresAndFunctionsEntry entry : proceduresAndFunctionsTable.getAll()) {
      List<VariableTableEntry> expandedList = entry.parameters.toList();
      expandedList.addAll(entry.localVariables.toList());
      VariablesTable expandedVariablesTable = new VariablesTable(expandedList);
      
      int i = 0;
      for(VariableTableEntry variable : expandedVariablesTable.toList()) {
        switch(variable.type) {
          case PrimitiveVariableType _ -> {
            LocalVariableEntry localVariableEntry = new LocalVariableEntry(
              i * Constants.WORD_SIZE,
              Constants.WORD_SIZE
            );

            map.put(variable.identifier.toLowerCase(), localVariableEntry);
          }
          case ArrayVariableType type -> {
            LocalVariableEntry localVariableEntry = new LocalVariableEntry(
              i * Constants.WORD_SIZE * type.size(),
              Constants.WORD_SIZE * type.size()
            );
            
            map.put(variable.identifier.toLowerCase(), localVariableEntry);
          }
          default -> throw new RuntimeException("Unsupported variable type");
        }

        i++;
      }
    }
  }

  public LocalVariableEntry get(String identifier) {
    return map.get(identifier);
  }
}
