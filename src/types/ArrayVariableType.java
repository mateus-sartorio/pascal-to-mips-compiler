package types;

import java.util.Formatter;

public class ArrayVariableType extends VariableType {
    public final int startIndex;
    public final int endIndex;

    public ArrayVariableType(
      PrimitiveType basePrimitiveType,
      int startIndex,
      int endIndex
    ) {
      this.basePrimitiveType = basePrimitiveType;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
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
