package types;

public abstract class VariableType {
  public PrimitiveTypeEnum basePrimitiveType;

  public abstract boolean isEquivalent(VariableType other);
}
