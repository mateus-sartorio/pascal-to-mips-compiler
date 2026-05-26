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

    if(this.basePrimitiveType == PrimitiveTypeEnum.REAL && this.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      return true;
    }
    
    return this.basePrimitiveType == other.basePrimitiveType;
  }

  @Override
  public String toString() {
    return basePrimitiveType.toString();
  }
}
