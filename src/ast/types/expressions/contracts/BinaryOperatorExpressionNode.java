package ast.types.expressions.contracts;

public abstract class BinaryOperatorExpressionNode extends ExpressionNode {
  public final ExpressionNode left;
  public final ExpressionNode right;

  public BinaryOperatorExpressionNode(int id, ExpressionNode left, ExpressionNode right) {
    super(id);
    this.left = left;
    this.right = right;
  }
}
