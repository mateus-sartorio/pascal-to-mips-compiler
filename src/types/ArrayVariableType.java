package types;

import java.util.Formatter;

public class ArrayVariableType extends VariableType {
    public final int startIndex;
    public final int endIndex;

    public ArrayVariableType(
      PrimitiveTypeEnum basePrimitiveType,
      int startIndex,
      int endIndex
    ) {
      this.basePrimitiveType = basePrimitiveType;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    @Override
    public boolean isEquivalent(VariableType other) {
      if(!(other instanceof ArrayVariableType)) {
        return false;
      }

      ArrayVariableType otherCorrectType = (ArrayVariableType) other;

      if(!(this.startIndex == otherCorrectType.startIndex) || !(this.endIndex == otherCorrectType.endIndex)) {
        return false;
      }
      
      if(this.basePrimitiveType == PrimitiveTypeEnum.REAL && this.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        return true;
      }
    
      return this.basePrimitiveType == other.basePrimitiveType;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      Formatter f = new Formatter(sb);
      f.format("array[%d..%d] of %s", startIndex, endIndex, basePrimitiveType.toString());
      f.close();

      return sb.toString();
	  }
}
