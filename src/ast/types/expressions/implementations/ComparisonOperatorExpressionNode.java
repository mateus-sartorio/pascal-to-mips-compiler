package ast.types.expressions.implementations;

import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;

public class ComparisonOperatorExpressionNode extends BinaryOperatorExpressionNode {
  public final String operator;
  
  public ComparisonOperatorExpressionNode(
    ExpressionNode left,
    ExpressionNode right,
    String operator
  ) {
    super(left, right);
    this.operator = operator;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"%s\"];\n".formatted(getDotNotationIdentifier(), operator));

    sb.append(left.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), left.getDotNotationIdentifier()));

    sb.append(right.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), right.getDotNotationIdentifier()));
    
    return sb.toString();
  }
}
