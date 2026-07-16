package interpreter;

/**
 * Representa una palavra de 4 bytes (32 bits) que pode ser interpretada como um inteiro ou um ponto flutuante.
 */
public class Word {
  /**
   * Armazena os bytes da palavra.
   */
  private byte[] bytes;

  /**
   * Construtor privado para criar uma palavra a partir de um array de bytes.
   *
   * @param bytes O array de bytes que representa a palavra.
   */
  public static Word fromInt(int value) {
    Word word = new Word();
    word.bytes = new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) (value >>> 0) };
    return word;
  }

  /**
   * Construtor privado para criar uma palavra a partir de um array de bytes.
   *
   * @param bytes O array de bytes que representa a palavra.
   */
  public int toInt() {
    return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | ((bytes[3] & 0xFF) << 0);
  }

  public float toFloat() {
    int intBits = this.toInt();
    return Float.intBitsToFloat(intBits);
  }
  
  public static Word fromFloat(float value) {
    int intBits = Float.floatToIntBits(value);
    return fromInt(intBits);
  }
}
