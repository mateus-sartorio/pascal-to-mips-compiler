package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.contracts.UnaryOperatorExpressionNode;

public class NotOperatorExpressionNode extends UnaryOperatorExpressionNode {
  public NotOperatorExpressionNode(ExpressionNode expression) {
    super(expression);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"not\"];\n".formatted(getDotNotationIdentifier()));
    
    sb.append(expression.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));
    
    return sb.toString();
  }
}
