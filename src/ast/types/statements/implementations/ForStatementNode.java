package ast.types.statements.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

public class ForStatementNode extends StatementNode {
  String controlVariableIdentifier;
  ExpressionNode initialValue;
  ExpressionNode finalValue;
  boolean isDownto;
  StatementNode body;

  public ForStatementNode(
    String controlVariableIdentifier,
    ExpressionNode initialValue,
    ExpressionNode finalValue,
    boolean isDownto,
    StatementNode body
  ) {
    this.controlVariableIdentifier = controlVariableIdentifier;
    this.initialValue = initialValue;
    this.finalValue = finalValue;
    this.isDownto = isDownto;
    this.body = body;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"for\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(initialValue.toDotNotation());
    sb.append("%s -> %s [label=\"initialValue\"];\n".formatted(getDotNotationIdentifier(), initialValue.getDotNotationIdentifier()));

    sb.append(finalValue.toDotNotation());
    sb.append("%s -> %s [label=\"finalValue\"];\n".formatted(getDotNotationIdentifier(), finalValue.getDotNotationIdentifier()));

    sb.append(body.toDotNotation());
    sb.append("%s -> %s [label=\"body\"];\n".formatted(getDotNotationIdentifier(), body.getDotNotationIdentifier()));

    return sb.toString();
  }
}
