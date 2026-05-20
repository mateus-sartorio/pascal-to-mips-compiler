package types;

import java.util.Formatter;

public class VariableType {
  private final boolean isPrimitive;
    private final PrimitiveType primitiveType;
    private final int startIndex;
    private final int endIndex;

    public VariableType(
      PrimitiveType primitiveType,
      int startIndex,
      int endIndex
    ) {
      this.isPrimitive = false;
      this.primitiveType = primitiveType;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

    public VariableType(PrimitiveType primitiveType) {
      this.isPrimitive = true;
      this.primitiveType = primitiveType;
      this.startIndex = 0;
      this.endIndex = 0;
    }

    @Override
    public String toString() {
      if(isPrimitive) {
        return primitiveType.toString();
      }

      StringBuilder sb = new StringBuilder();
      Formatter f = new Formatter(sb);
      f.format("array[%d..%d] of %s", startIndex, endIndex, primitiveType.toString());
      f.close();

      return sb.toString();
	  }
}
