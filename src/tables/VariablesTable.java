package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import types.VariableType;

/**
 * Classe que representa uma tabela de variáveis, onde cada entrada contém o identificador da variável, a linha em que foi declarada e seu tipo.
 */
public final class VariablesTable {
  /**
   * Classe interna que representa uma entrada na tabela de variáveis.
   */
  public static final class VariableTableEntry {
    public final String identifier;
    public final Integer line;
    public final VariableType type;

    /**
     * Construtor da classe VariableTableEntry
     * @param identifier O identificador da variável
     * @param line A linha em que a variável foi declarada
     * @param type O tipo da variável
     */
    public VariableTableEntry(String identifier, int line, VariableType type) {
      this.identifier = identifier;
      this.line = line;
      this.type = type;
    }

    /**
     * Construtor da classe VariableTableEntry sem linha (útil para variáveis temporárias ou quando a linha não é relevante)
     * @param name O identificador da variável
     * @param type O tipo da variável
     */
    public VariableTableEntry(String name, VariableType type) {
      this.identifier = name;
      this.line = null;
      this.type = type;
    }
  }

  /**
   * Mapa que armazena as entradas da tabela de variáveis, onde a chave é o identificador da variável em minúsculas.
   */
  private Map<String, VariableTableEntry> table = new LinkedHashMap<>();

  /**
   * Construtor da classe VariablesTable, inicializando a tabela de variáveis como um LinkedHashMap vazio.
   */
  public VariablesTable() {
    table = new LinkedHashMap<>();
  }

  /**
   * Construtor da classe VariablesTable que recebe uma lista de entradas de tabela de variáveis e as adiciona à tabela.
   * @param list A lista de entradas de tabela de variáveis
   */
  public VariablesTable(List<VariableTableEntry> list) {
    table = new LinkedHashMap<>();

    for(VariableTableEntry entry : list) {
      table.put(entry.identifier.toLowerCase(), entry);
    }
  }

  /**
   * Verifica se a tabela de variáveis está vazia.
   * @return true se a tabela estiver vazia, false caso contrário
   */
  public boolean isEmpty() {
    return table.isEmpty();
  }

  /**
   * Retorna o tamanho da tabela de variáveis.
   * @return O número de entradas na tabela de variáveis
   */
  public int size() {
    return table.size();
  }

  /**
   * Verifica se uma variável com o identificador fornecido existe na tabela de variáveis.
   * @param identifier O identificador da variável a ser verificada
   * @return true se a variável existir na tabela, false caso contrário
   */
  public boolean lookupVariable(String identifier) {
    return table.containsKey(identifier.toLowerCase());
  }

  /**
   * Adiciona uma nova variável à tabela de variáveis.
   * @param identifier O identificador da variável
   * @param line A linha em que a variável foi declarada
   * @param type O tipo da variável
   */
  public void addVariable(String identifier, int line, VariableType type) {
    VariableTableEntry entry = new VariableTableEntry(identifier, line, type);
    table.put(identifier.toLowerCase(), entry);
  }

  /**
   * Adiciona uma nova variável à tabela de variáveis sem linha (útil para variáveis temporárias ou quando a linha não é relevante).
   * @param identifier O identificador da variável
   * @param type O tipo da variável
   */
  public VariableTableEntry get(String identifier) {
    return table.get(identifier.toLowerCase());
  }

  /**
   * Retorna o índice da variável com o identificador fornecido na tabela de variáveis.
   * @param identifier O identificador da variável
   * @return O índice da variável na tabela, ou -1 se a variável não existir
   */
  public int getIndex(String identifier) {
    return new ArrayList<>(table.keySet()).indexOf(identifier.toLowerCase());
  }

  /**
   * Converte a tabela de variáveis em uma lista de entradas de tabela de variáveis.
   * @return Uma lista contendo todas as entradas da tabela de variáveis
   */
  public List<VariableTableEntry> toList() {
    return new ArrayList<VariableTableEntry>(table.values());
  }

  /**
   * Retorna uma representação em string da tabela de variáveis, listando cada entrada com seu índice, identificador, linha (se disponível) e tipo.
   * @return Uma string representando a tabela de variáveis
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    int i = 0;
    for (VariableTableEntry entry : table.values()) {
      f.format(
        "%d. identifier: '%s'%s, type: %s\n",
        i,
        entry.identifier,
        entry.line != null ? ", line: " + entry.line : "",
        entry.type.toString()
      );
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}