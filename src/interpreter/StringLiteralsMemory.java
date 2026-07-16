package interpreter;

import java.util.List;

import tables.StringLiteralsTable;

/**
 * Representa a memória de literais de string, permitindo adicionar, buscar e recuperar literais de string.
 */
public class StringLiteralsMemory {
  private final List<String> stringLiteralsList;

  /**
   * Construtor que inicializa a memória de literais de string com base em uma tabela de literais de string.
   *
   * @param stringLiteralsTable A tabela de literais de string usada para inicializar a memória.
   */
  public StringLiteralsMemory(StringLiteralsTable stringLiteralsTable) {
    this.stringLiteralsList = stringLiteralsTable.toList();
  }

  /**
   * Adiciona um literal de string à memória, se ainda não estiver presente, e retorna o índice do literal.
   *
   * @param entry O literal de string a ser adicionado.
   * @return O índice do literal de string na memória.
   */
  public int addEntry(String entry) {
    if(!stringLiteralsList.contains(entry)) {
      stringLiteralsList.add(entry);
    }

    return indexOf(entry);
  }

  /**
   * Retorna o índice de um literal de string na memória.
   *
   * @param entry O literal de string a ser buscado.
   * @return O índice do literal de string na memória, ou -1 se não encontrado.
   */
  public int indexOf(String entry) {
    return stringLiteralsList.indexOf(entry);
  }

  /**
   * Retorna o literal de string armazenado na memória no índice especificado.
   *
   * @param index O índice do literal de string a ser recuperado.
   * @return O literal de string armazenado na memória no índice especificado.
   */
  public String getEntry(int index) {
    return stringLiteralsList.get(index);
  }
}
