package types;

import java.util.Formatter;

/**
 * Representa uma variável de tipo array.
 */
public class ArrayVariableType extends VariableType {
  public final int lowerBound;
  public final int upperBound;

  /**
   * Construtor da classe ArrayVariableType.
   * @param basePrimitiveType O tipo primitivo base do array.
   * @param lowerBound O limite inferior do array.
   * @param upperBound O limite superior do array.
   */
  public ArrayVariableType(
    PrimitiveTypeEnum basePrimitiveType,
    int lowerBound,
    int upperBound
  ) {
    super(basePrimitiveType, false);
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  /**
   * Retorna o tamanho do array.
   * @return O tamanho do array.
   */
  public int size() {
    return upperBound - lowerBound + 1;
  }

  /**
   * Verifica se o tipo de variável atual é equivalente a outro tipo de variável.
   * @param other O outro tipo de variável a ser comparado.
   * @return true se os tipos forem equivalentes, false caso contrário.
   */
  @Override
  public boolean isEquivalent(VariableType other) {
    if(!(other instanceof ArrayVariableType)) {
      return false;
    }

    ArrayVariableType otherCorrectType = (ArrayVariableType) other;
    
    return (
      this.basePrimitiveType == other.basePrimitiveType &&
      this.lowerBound == otherCorrectType.lowerBound &&
      this.upperBound == otherCorrectType.upperBound
    );
  }

  /**
   * Retorna uma representação em string do tipo de variável array.
   * @return Uma string representando o tipo de variável array.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    f.format("array[%d..%d] of %s", lowerBound, upperBound, basePrimitiveType.toString());
    f.close();

    return sb.toString();
  }

  /**
   * Verifica se o tipo de variável atual é ordinal.
   * @return false, pois arrays não são tipos ordinais.
   */
  public boolean isOrdinal() {
    return false;
  }
}