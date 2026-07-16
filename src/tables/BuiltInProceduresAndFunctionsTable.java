package tables;

import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveTypeEnum;
import types.ProcedureOrFunctionEnum;

/**
 * Representa uma tabela de procedimentos e funções embutidas (built-in) do compilador.
 */
public final class BuiltInProceduresAndFunctionsTable {
  public static final class BuiltInProceduresAndFunctionsEntry {
    public final String identifier;
    public final ProcedureOrFunctionEnum type;
    public final PrimitiveTypeEnum returnType;
    public final VariablesTable parameters;

    /**
     * Construtor da entrada da tabela de procedimentos e funções embutidas.
     *
     * @param identifier O identificador do procedimento ou função.
     * @param type O tipo (procedimento ou função).
     * @param returnType O tipo de retorno (apenas para funções).
     * @param parameters A tabela de parâmetros do procedimento ou função.
     */
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

  /**
   * Tabela de procedimentos e funções embutidas, mapeando identificadores para suas entradas correspondentes.
   */
  private Map<String, BuiltInProceduresAndFunctionsEntry> table = new LinkedHashMap<>();

  /**
   * Verifica se a tabela de procedimentos e funções embutidas está vazia.
   *
   * @return true se a tabela estiver vazia, false caso contrário.
   */
  public boolean isEmpty() {
    return table.isEmpty();
  }

  /**
   * Verifica se um procedimento ou função com o identificador fornecido existe na tabela.
   *
   * @param identifier O identificador do procedimento ou função a ser verificado.
   * @return true se o procedimento ou função existir, false caso contrário.
   */
  public boolean lookProcedureOrFunction(String identifier) {
    return table.containsKey(identifier.toLowerCase());
  }

  /**
   * Obtém a entrada da tabela de procedimentos e funções embutidas correspondente ao identificador fornecido.
   *
   * @param identifier O identificador do procedimento ou função a ser obtido.
   * @return A entrada correspondente, ou null se não existir.
   */
  public BuiltInProceduresAndFunctionsEntry get(String identifier) {
    return table.get(identifier.toLowerCase());
  }

  /**
   * Adiciona um procedimento à tabela de procedimentos e funções embutidas.
   *
   * @param identifier O identificador do procedimento.
   * @param parameterList A lista de parâmetros do procedimento.
   */
  public void addProcedure(String identifier, List<VariableTableEntry> parameterList) {
    addProcedureOrFunction(identifier, ProcedureOrFunctionEnum.PROCEDURE, parameterList, null);
  }

  /**
   * Adiciona uma função à tabela de procedimentos e funções embutidas.
   *
   * @param identifier O identificador da função.
   * @param parameterList A lista de parâmetros da função.
   * @param returnType O tipo de retorno da função.
   */
  public void addFunction(String identifier, List<VariableTableEntry> parameterList, PrimitiveTypeEnum returnType) {
    addProcedureOrFunction(identifier, ProcedureOrFunctionEnum.FUNCTION, parameterList, returnType);
  }

  /**
   * Adiciona um procedimento ou função à tabela de procedimentos e funções embutidas.
   *
   * @param identifier O identificador do procedimento ou função.
   * @param type O tipo (procedimento ou função).
   * @param parameterList A lista de parâmetros do procedimento ou função.
   * @param returnType O tipo de retorno (apenas para funções).
   */
  private void addProcedureOrFunction(String identifier, ProcedureOrFunctionEnum type, List<VariableTableEntry> parameterList, PrimitiveTypeEnum returnType) {
    BuiltInProceduresAndFunctionsEntry newEntry = new BuiltInProceduresAndFunctionsEntry(
      identifier,
      type,
      returnType,
      new VariablesTable(parameterList)
    );

    table.put(identifier.toLowerCase(), newEntry);
  }

  /**
   * Retorna uma representação em string da tabela de procedimentos e funções embutidas.
   *
   * @return Uma string representando a tabela.
   */
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