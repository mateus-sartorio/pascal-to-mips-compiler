package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveTypeEnum;
import types.ProcedureOrFunctionEnum;
import types.VariableType;

/**
 * Uma tabela de procedimentos e funções, que mapeia cada identificador para uma entrada contendo informações sobre o procedimento ou função.
 */
public final class ProceduresAndFunctionsTable {
  public static final class ProceduresAndFunctionsEntry {
    public final String identifier;
    public final int line;
    public final ProcedureOrFunctionEnum type;
    public final PrimitiveTypeEnum returnType;
    public final VariablesTable parameters;
    public final VariablesTable localVariables;

    /**
     * Construtor para criar uma entrada de procedimento ou função.
     *
     * @param identifier o identificador do procedimento ou função.
     * @param line a linha onde o procedimento ou função é definido.
     * @param type o tipo (procedimento ou função).
     * @param returnType o tipo de retorno (apenas para funções).
     * @param parameters a tabela de parâmetros do procedimento ou função.
     * @param localVariables a tabela de variáveis locais do procedimento ou função.
     */
    ProceduresAndFunctionsEntry(
      String identifier,
      int line,
      ProcedureOrFunctionEnum type,
      PrimitiveTypeEnum returnType,
      VariablesTable parameters,
      VariablesTable localVariables
    ) {
      this.identifier = identifier;
      this.line = line;
      this.type = type;
      this.returnType = returnType;
      this.parameters = parameters;
      this.localVariables = localVariables;
    }
  }

  /**
   * A tabela que mapeia identificadores de procedimentos e funções para suas respectivas entradas.
   */
  private Map<String, ProceduresAndFunctionsEntry> table = new LinkedHashMap<>();

  /**
   * Verifica se a tabela está vazia.
   *
   * @return true se a tabela estiver vazia, false caso contrário.
   */
  public boolean isEmpty() {
    return table.isEmpty();
  }

  /**
   * Retorna o tamanho da tabela.
   *
   * @return o número de procedimentos e funções na tabela.
   */
  public List<ProceduresAndFunctionsEntry> getAll() {
    return new ArrayList<>(table.values());
  }

  /**
   * Verifica se um identificador de procedimento ou função está presente na tabela.
   *
   * @param identifier o identificador a ser verificado.
   * @return true se o identificador estiver presente, false caso contrário.
   */
  public boolean lookProcedureOrFunction(String identifier) {
    return table.containsKey(identifier.toLowerCase());
  }

  /**
   * Retorna a entrada associada a um identificador de procedimento ou função específico.
   *
   * @param identifier o identificador do procedimento ou função.
   * @return a entrada correspondente ao identificador, ou null se não existir.
   */
  public ProceduresAndFunctionsEntry get(String identifier) {
    return table.get(identifier.toLowerCase());
  }

  /**
   * Retorna a entrada de parâmetro ou variável local associada a um identificador específico, procurando em todos os procedimentos e funções.
   *
   * @param localVariableIdentifier o identificador da variável local ou parâmetro.
   * @return a entrada correspondente ao identificador, ou null se não existir.
   */
  public VariableTableEntry getParameterOrLocalVariableFromAnyProcedureOrFunction(String localVariableIdentifier) {
    for(ProceduresAndFunctionsEntry entry : table.values()) {
      var parameter = entry.parameters.get(localVariableIdentifier);

      if(parameter != null) {
        return parameter;
      }

      var localVariable = entry.localVariables.get(localVariableIdentifier);
      if(localVariable != null) {
        return localVariable;
      }
    }

    return null;
  }

  /**
   * Verifica se uma variável local está presente em um procedimento ou função específico.
   *
   * @param procedureOrFunctionIdentifier o identificador do procedimento ou função.
   * @param localVariableIdentifier o identificador da variável local.
   * @return true se a variável local estiver presente, false caso contrário.
   */
  public boolean lookupProcedureOrFunctionLocalVariable(
    String procedureOrFunctionIdentifier,
    String localVariableIdentifier
  ) {
    assert lookProcedureOrFunction(procedureOrFunctionIdentifier);

    ProceduresAndFunctionsEntry entry = get(procedureOrFunctionIdentifier);

    VariablesTable localVariables = entry.localVariables;

    return localVariables.lookupVariable(localVariableIdentifier);
  }

  /**
   * Verifica se um parâmetro está presente em um procedimento ou função específico.
   *
   * @param procedureOrFunctionIdentifier o identificador do procedimento ou função.
   * @param localVariableIdentifier o identificador do parâmetro.
   * @return true se o parâmetro estiver presente, false caso contrário.
   */
  public boolean lookupProcedureOrFunctionLocalParameter(
    String procedureOrFunctionIdentifier,
    String localVariableIdentifier
  ) {
    assert lookProcedureOrFunction(procedureOrFunctionIdentifier);

    ProceduresAndFunctionsEntry entry = get(procedureOrFunctionIdentifier);

    VariablesTable parameters = entry.parameters;

    return parameters.lookupVariable(localVariableIdentifier);
  }

  /**
   * Adiciona um procedimento à tabela.
   *
   * @param identifier o identificador do procedimento.
   * @param line a linha onde o procedimento é definido.
   */
  public void addProcedure(String identifier, int line) {
    ProceduresAndFunctionsEntry entry = new ProceduresAndFunctionsEntry(
      identifier,
      line,
      ProcedureOrFunctionEnum.PROCEDURE,
      PrimitiveTypeEnum.NO_TYPE,
      new VariablesTable(),
      new VariablesTable()
    );

    table.put(identifier.toLowerCase(), entry);
  }

  /**
   * Adiciona uma função à tabela.
   *
   * @param identifier o identificador da função.
   * @param line a linha onde a função é definida.
   * @param type o tipo de retorno da função.
   */
  public void addFunction(String identifier, int line, PrimitiveTypeEnum type) {
    ProceduresAndFunctionsEntry entry = new ProceduresAndFunctionsEntry(
      identifier,
      line,
      ProcedureOrFunctionEnum.FUNCTION,
      type,
      new VariablesTable(),
      new VariablesTable()
    );

    table.put(identifier.toLowerCase(), entry);
  }

  /**
   * Adiciona um parâmetro a um procedimento ou função específico.
   *
   * @param procedureOrFunctionIdentifier o identificador do procedimento ou função.
   * @param parameterIdentifier o identificador do parâmetro.
   * @param line a linha onde o parâmetro é definido.
   * @param type o tipo do parâmetro.
   */
  public void addProcedlureOrFunctionParameter(
    String procedureOrFunctionIdentifier,
    String parameterIdentifier,
    int line,
    VariableType type
  ) {
    
    ProceduresAndFunctionsEntry entry = get(procedureOrFunctionIdentifier);

    assert entry != null;

    VariablesTable parameters = entry.parameters;

    assert !(parameters.lookupVariable(parameterIdentifier));

    parameters.addVariable(parameterIdentifier, line, type);
  }

  /**
   * Adiciona uma variável local a um procedimento ou função específico.
   *
   * @param procedureOrFunctionIdentifier o identificador do procedimento ou função.
   * @param variableIdentifier o identificador da variável local.
   * @param line a linha onde a variável local é definida.
   * @param type o tipo da variável local.
   */
  public void addProcedlureOrFunctionVariable(
    String procedureOrFunctionIdentifier,
    String variableIdentifier,
    int line,
    VariableType type
  ) {
    ProceduresAndFunctionsEntry entry = get(procedureOrFunctionIdentifier);

    assert entry != null;

    VariablesTable localVariables = entry.localVariables;

    assert !(localVariables.lookupVariable(variableIdentifier));

    localVariables.addVariable(variableIdentifier, line, type);
  }

  /**
   * Retorna uma representação em string da tabela de procedimentos e funções.
   *
   * @return uma string representando a tabela.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);

    
    for(ProceduresAndFunctionsEntry entry : table.values()) {
      f.format(
        "%s '%s' - line %d%s\n",
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? "Function" : "Procedure",
        entry.identifier,
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

      f.format("\n");
    }
    f.close();
    
    return sb.toString();
  }
}