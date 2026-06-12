package ast.types;

public class BooleanExpressionNode extends ExpressionNode {
  public final boolean value;

  public BooleanExpressionNode(boolean value) {
    this.value = value;
  }
}
