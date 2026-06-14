package ast.types.expressions.contracts;

public abstract class UnaryOperatorExpressionNode extends ExpressionNode {
  public final ExpressionNode expression;

  public UnaryOperatorExpressionNode(ExpressionNode expression) {
    this.expression = expression;
  }
}
