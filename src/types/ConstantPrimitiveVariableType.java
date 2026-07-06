package types;

public class ConstantPrimitiveVariableType<T> extends PrimitiveVariableType {
  public final T value;

  public ConstantPrimitiveVariableType(PrimitiveTypeEnum basePrimitiveType, T value) {
    super(basePrimitiveType, false);
    this.value = value;
  }
}
