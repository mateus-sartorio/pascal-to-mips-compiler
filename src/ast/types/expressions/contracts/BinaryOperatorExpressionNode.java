package ast.types.expressions.contracts;

import types.VariableType;

public abstract class BinaryOperatorExpressionNode extends ExpressionNode {
  public final ExpressionNode left;
  public final ExpressionNode right;

  public BinaryOperatorExpressionNode(int id, ExpressionNode left, ExpressionNode right, VariableType type) {
    super(id, type);
    this.left = left;
    this.right = right;
  }
}
