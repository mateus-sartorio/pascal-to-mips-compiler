package ast.types.statements.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.implementations.VariableAccessExpressionNode;

public class ReturnStatementNode extends AssignmentStatementNode {
  public ReturnStatementNode(int id, VariableAccessExpressionNode variableAccessExpressionNode, ExpressionNode expression) {
    super(id, variableAccessExpressionNode, expression);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"return\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(expression.toDotNotation());
    sb.append("%s -> %s [label=\"value\"] ;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));

    return sb.toString();
  }
}
