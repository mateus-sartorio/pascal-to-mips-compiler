package ast.types.statements.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

public class AssignmentStatementNode extends StatementNode {
  public final ExpressionNode variableAccessExpressionNode;
  public final ExpressionNode expression;

  public AssignmentStatementNode(int id, ExpressionNode variableAccessExpressionNode, ExpressionNode expression) {
    super(id);
    this.variableAccessExpressionNode = variableAccessExpressionNode;
    this.expression = expression;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\":=\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(variableAccessExpressionNode.toDotNotation());
    sb.append("%s -> %s [label=\"variable\"] ;\n".formatted(getDotNotationIdentifier(), variableAccessExpressionNode.getDotNotationIdentifier()));

    sb.append(expression.toDotNotation());
    sb.append("%s -> %s [label=\"value\"] ;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));

    return sb.toString();
  }
}
