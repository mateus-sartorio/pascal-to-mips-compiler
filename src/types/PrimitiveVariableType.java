package types;

public class PrimitiveVariableType extends VariableType {
  public PrimitiveVariableType(PrimitiveType basePrimitiveType) {
    this.basePrimitiveType = basePrimitiveType;
  }

  @Override
  public String toString() {
    return basePrimitiveType.toString();
  }
}
