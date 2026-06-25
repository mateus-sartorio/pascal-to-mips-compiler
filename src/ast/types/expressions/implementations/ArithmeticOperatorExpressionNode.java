package ast.types.expressions.implementations;

import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveVariableType;
import types.TypeRules;
import types.VariableType;

public class ArithmeticOperatorExpressionNode extends BinaryOperatorExpressionNode {
  public final String operator;

  public ArithmeticOperatorExpressionNode(int id, ExpressionNode left, ExpressionNode right, String operator) {
    VariableType resultType = left.type;
    
    if(operator.equals("+")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.PLUS_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }
    else if(operator.equals("-") || operator.equals("*")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.MATH_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }
    else if(operator.equals("/")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.REAL_DIVISION_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }

    super(id, left, right, resultType);
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
