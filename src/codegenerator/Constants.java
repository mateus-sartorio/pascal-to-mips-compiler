package codegenerator;

/**
 * Classe pra armaznar a constante do tamanho de uma palavra (word) em bytes.
 */
public class Constants {
  public static final int WORD_SIZE = 4;

  /**
   * Primeira linha do cabeçalho que separa o código traduzido do programa das rotinas
   * de apoio inseridas automaticamente.
   *
   * <p>O front-end procura exatamente por esta linha para dobrar a seção, portanto
   * alterá-la exige alterar também {@code RUNTIME_MARKER} em {@code app.js}.
   */
  public static final String RUNTIME_SECTION_MARKER = "# ===== RUNTIME SUPPORT ROUTINES";
}
