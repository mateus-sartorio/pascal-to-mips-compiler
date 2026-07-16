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

/**
 * Representa um mapa de offsets para variáveis locais em um frame de chamada (call frame) de uma função ou procedimento.
 * Cada variável local é associada a um offset e tamanho dentro do frame de chamada.
 */
public class CallFrameOffsetMap {
  /**
   * Representa uma entrada de variável local, contendo o offset e o tamanho da variável dentro do frame de chamada.
   */
  public record LocalVariableEntry(int offset, int size) {}

  /**
   * Mapa que associa o identificador da variável local (em minúsculas) à sua entrada de variável local (offset e tamanho).
   */
  private final Map<String, LocalVariableEntry> map;

  /**
   * Construtor que inicializa o mapa de offsets para variáveis locais com base em uma tabela de procedimentos e funções.
   *
   * @param proceduresAndFunctionsTable A tabela de procedimentos e funções usada para construir o mapa de offsets.
   */
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

  /**
   * Retorna a entrada de variável local (offset e tamanho) associada ao identificador da variável local.
   *
   * @param identifier O identificador da variável local (em minúsculas).
   * @return A entrada de variável local correspondente, ou null se a variável não estiver presente no mapa.
   */
  public LocalVariableEntry get(String identifier) {
    return map.get(identifier);
  }
}
