package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.contracts.UnaryOperatorExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class NotOperatorExpressionNode extends UnaryOperatorExpressionNode {
  public NotOperatorExpressionNode(int id, ExpressionNode expression) {
    super(id, expression, new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN));
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
