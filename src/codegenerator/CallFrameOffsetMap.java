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

public class CallFrameOffsetMap {
  public record LocalVariableEntry(int offset, int size) {}

  private final Map<String, LocalVariableEntry> map;

  public CallFrameOffsetMap(ProceduresAndFunctionsTable proceduresAndFunctionsTable) {
    this.map = new HashMap<>();

    for(ProceduresAndFunctionsEntry entry : proceduresAndFunctionsTable.getAll()) {
      List<VariableTableEntry> expandedList = entry.parameters.toList();
      expandedList.addAll(entry.localVariables.toList());
      VariablesTable expandedVariablesTable = new VariablesTable(expandedList);
      
      int offset = 0;
      for(VariableTableEntry variable : expandedVariablesTable.toList()) {
        int size = switch (variable.type) {
          case PrimitiveVariableType _ -> Constants.WORD_SIZE;
          case ArrayVariableType type   -> Constants.WORD_SIZE * type.size();
          default -> throw new RuntimeException("Unsupported variable type");
        };

        map.put(variable.identifier.toLowerCase(), new LocalVariableEntry(offset, size));
        offset += size;
      }
    }
  }

  public LocalVariableEntry get(String identifier) {
    return map.get(identifier);
  }
}
