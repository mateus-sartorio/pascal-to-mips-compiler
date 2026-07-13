package types;

import java.util.Formatter;

public class ArrayVariableType extends VariableType {
  public final int lowerBound;
  public final int upperBound;

  public ArrayVariableType(
    PrimitiveTypeEnum basePrimitiveType,
    int lowerBound,
    int upperBound
  ) {
    super(basePrimitiveType, false);
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  public int size() {
    return upperBound - lowerBound + 1;
  }

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

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    f.format("array[%d..%d] of %s", lowerBound, upperBound, basePrimitiveType.toString());
    f.close();

    return sb.toString();
  }

  public boolean isOrdinal() {
    return false;
  }
}