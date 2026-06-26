package ast.types.expressions.contracts;

import types.VariableType;

public abstract class UnaryOperatorExpressionNode extends ExpressionNode {
  public final ExpressionNode expression;

  public UnaryOperatorExpressionNode(int id, ExpressionNode expression, VariableType type) {
    super(id, type);
    this.expression = expression;
  }
}
