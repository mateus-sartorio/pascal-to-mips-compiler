package types;

/**
 * Representa uma variável constante de tipo primitivo.
 * @param <T> O tipo do valor literal da variável constante.
 */
public class ConstantPrimitiveVariableType<T> extends PrimitiveVariableType {
  public final T value;

  /**
   * Construtor da classe ConstantPrimitiveVariableType.
   * @param basePrimitiveType O tipo primitivo base da variável constante.
   * @param value O valor literal da variável constante.
   */
  public ConstantPrimitiveVariableType(PrimitiveTypeEnum basePrimitiveType, T value) {
    super(basePrimitiveType, false);
    this.value = value;
  }
}
