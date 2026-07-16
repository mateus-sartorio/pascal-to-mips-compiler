package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uma tabela de literais de string, que mapeia cada literal para um índice inteiro único.
 */
public final class StringLiteralsTable {

  private Map<Integer, String> map = new HashMap<>();
  private Map<String, Integer> inverseMap = new HashMap<>();

  /**
   * Verifica se a tabela está vazia.
   *
   * @return true se a tabela estiver vazia, false caso contrário.
   */
  public boolean isEmpty() {
    return map.isEmpty();
  }

  /**
   * Retorna o tamanho da tabela.
   *
   * @return o número de literais de string na tabela.
   */
  public int size() {
    return map.size();
  }

  /**
   * Retorna uma lista de todos os literais de string na tabela.
   *
   * @return uma lista contendo todos os literais de string.
   */
  public List<String> toList() {
    return new ArrayList<>(map.values());
  }

  /**
   * Retorna uma lista de todas as chaves (índices) na tabela, ordenadas.
   *
   * @return uma lista contendo todas as chaves ordenadas.
   */
  public List<Integer> keySet() {
    var list = new ArrayList<>(map.keySet());
    list.sort(null);
    return list;
  }

  /**
   * Retorna o literal de string associado a uma chave (índice) específica.
   *
   * @param key o índice do literal de string.
   * @return o literal de string correspondente à chave, ou null se não existir.
   */
  public String get(Integer key) {
    return map.get(key);
  }

  /**
   * Retorna o índice associado a um literal de string específico.
   *
   * @param value o literal de string.
   * @return o índice correspondente ao literal, ou null se não existir.
   */
  public Integer indexOf(String value) {
    return inverseMap.get(value);
  }

  /**
   * Adiciona um literal de string à tabela, se ainda não estiver presente.
   *
   * @param literal o literal de string a ser adicionado.
   */
  public void addStringLiteral(String literal) {
    int size = map.size();

    if(!inverseMap.containsKey(literal)) {
      map.put(size, literal);
      inverseMap.put(literal, size);
    }
  }

  /**
   * Retorna uma representação em string da tabela de literais de string.
   *
   * @return uma string representando a tabela.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    int i = 0;
    for (String literal : map.values()) {
      f.format("%d. '%s'\n", i, literal);
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}