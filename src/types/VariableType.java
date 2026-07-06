package types;

public abstract class VariableType {
  public final PrimitiveTypeEnum basePrimitiveType;
  public final boolean isIndexed;

  public VariableType(PrimitiveTypeEnum basePrimitiveType, boolean isIndexed) {
    this.basePrimitiveType = basePrimitiveType;
    this.isIndexed = isIndexed;
  }

  public abstract boolean isEquivalent(VariableType other);

  public abstract boolean isOrdinal();
}