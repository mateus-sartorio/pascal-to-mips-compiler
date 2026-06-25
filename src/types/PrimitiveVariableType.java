package types;

public class PrimitiveVariableType extends VariableType {
  public PrimitiveVariableType(PrimitiveTypeEnum basePrimitiveType) {
    this.basePrimitiveType = basePrimitiveType;
  }

  @Override
  public boolean isEquivalent(VariableType other) {
    if(!(other instanceof PrimitiveVariableType)) {
      return false;
    }

    PrimitiveTypeEnum resultType = TypeRules.getResultType(
      TypeRules.ASSIGNMENT_TABLE,
      other.basePrimitiveType,
      this.basePrimitiveType
    );

    return resultType != PrimitiveTypeEnum.NO_TYPE;
  }

  @Override
  public String toString() {
    return basePrimitiveType.toString();
  }

  @Override
  public boolean isOrdinal() {
    return (
      basePrimitiveType == PrimitiveTypeEnum.INTEGER ||
      basePrimitiveType == PrimitiveTypeEnum.CHAR ||
      basePrimitiveType == PrimitiveTypeEnum.BOOLEAN
    );
  }
}