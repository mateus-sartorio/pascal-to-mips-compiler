package ast.types.statements.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.implementations.VariableAccessExpressionNode;
import ast.types.statements.contract.StatementNode;

public class AssignmentStatementNode extends StatementNode {
  VariableAccessExpressionNode variableAccessExpressionNode;
  ExpressionNode expression;

  public AssignmentStatementNode(VariableAccessExpressionNode variableAccessExpressionNode, ExpressionNode expression) {
    this.variableAccessExpressionNode = variableAccessExpressionNode;
    this.expression = expression;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\":=\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(variableAccessExpressionNode.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), variableAccessExpressionNode.getDotNotationIdentifier()));

    sb.append(expression.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));

    return sb.toString();
  }
}
