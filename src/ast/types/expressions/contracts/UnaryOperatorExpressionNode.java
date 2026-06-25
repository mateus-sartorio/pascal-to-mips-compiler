package ast.types.expressions.contracts;

public abstract class UnaryOperatorExpressionNode extends ExpressionNode {
  public final ExpressionNode expression;

  public UnaryOperatorExpressionNode(int id, ExpressionNode expression) {
    super(id);
    this.expression = expression;
  }
}
