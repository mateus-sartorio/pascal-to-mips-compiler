package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;

public class CharToStringExpressionNode extends ExpressionNode {
  public final ExpressionNode expression;

  public CharToStringExpressionNode(int id, ExpressionNode expression) {
    super(id);
    this.expression = expression;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    sb.append("%s [label=\"(char) -> (string)\"];\n".formatted(getDotNotationIdentifier()));
    sb.append(expression.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));
    return sb.toString();
  }
}
