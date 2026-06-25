package ast.types.expressions.implementations;

import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class ComparisonOperatorExpressionNode extends BinaryOperatorExpressionNode {
  public final String operator;
  
  public ComparisonOperatorExpressionNode(
    int id,
    ExpressionNode left,
    ExpressionNode right,
    String operator
  ) {
    super(id, left, right, new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN));
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
