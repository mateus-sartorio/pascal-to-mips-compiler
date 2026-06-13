package ast.types.expressions.implementations;

import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;

public class LogicOperatorExpressionNode extends BinaryOperatorExpressionNode {
  String operator;

  public LogicOperatorExpressionNode(ExpressionNode left, ExpressionNode right, String operator) {
    super(left, right);
    this.operator = operator;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append(getDotNotationIdentifier() + " [label=\"LogicOperatorExpressionNode: " + operator + "\"];\n");

    sb.append(left.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + left.getDotNotationIdentifier() + ";\n");

    sb.append(right.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + right.getDotNotationIdentifier() + ";\n");
    
    return sb.toString();
  }
}
