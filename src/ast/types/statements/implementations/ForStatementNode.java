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

    String direction = isDownto ? "downto" : "to";
    sb.append("%s [label=\"for (%s)\"];\n".formatted(getDotNotationIdentifier(), direction));

    String pureId = getDotNotationIdentifier().replace("\"", "");
    String varNodeId = "\"%s_var\"".formatted(pureId);

    sb.append("%s [label=\"variable: %s\"];\n".formatted(varNodeId, controlVariableIdentifier));
    sb.append("%s -> %s [label=\"controlVariable\"];\n".formatted(getDotNotationIdentifier(), varNodeId));

    sb.append(initialValue.toDotNotation());
    sb.append("%s -> %s [label=\"initialValue\"];\n".formatted(getDotNotationIdentifier(), initialValue.getDotNotationIdentifier()));

    sb.append(finalValue.toDotNotation());
    sb.append("%s -> %s [label=\"finalValue\"];\n".formatted(getDotNotationIdentifier(), finalValue.getDotNotationIdentifier()));

    sb.append(body.toDotNotation());
    sb.append("%s -> %s [label=\"body\"];\n".formatted(getDotNotationIdentifier(), body.getDotNotationIdentifier()));

    return sb.toString();
  }
}
