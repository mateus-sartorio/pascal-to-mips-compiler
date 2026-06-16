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

    if(this.basePrimitiveType == PrimitiveTypeEnum.INTEGER && other.basePrimitiveType == PrimitiveTypeEnum.REAL) {
      return true;
    }

    if(this.basePrimitiveType == PrimitiveTypeEnum.CHAR && other.basePrimitiveType == PrimitiveTypeEnum.STRING) {
      return true;
    }
    
    return this.basePrimitiveType == other.basePrimitiveType;
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