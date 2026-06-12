package ast.types;

public abstract class BinaryOperatorExpressionNode extends ExpressionNode {
  public final ExpressionNode left;
  public final ExpressionNode right;

  public BinaryOperatorExpressionNode(ExpressionNode left, ExpressionNode right) {
    this.left = left;
    this.right = right;
  }
}
