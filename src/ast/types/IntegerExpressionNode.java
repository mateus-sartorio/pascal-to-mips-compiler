package ast.types;

public class IntegerExpressionNode extends ExpressionNode {
  public final int value;

  public IntegerExpressionNode(int value) {
    this.value = value;
  }
}
