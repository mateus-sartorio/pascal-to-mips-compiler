package ast.types.statements.implementations;

import ast.types.declarations.implementations.VariableDeclarationNode;
import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

public class ForStatementNode extends StatementNode {
  public final VariableDeclarationNode controlVariable;
  public final ExpressionNode initialValue;
  public final ExpressionNode finalValue;
  public final boolean isDownto;
  public final StatementNode body;

  public ForStatementNode(
    VariableDeclarationNode controlVariable,
    ExpressionNode initialValue,
    ExpressionNode finalValue,
    boolean isDownto,
    StatementNode body
  ) {
    this.controlVariable = controlVariable;
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

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(controlVariable.getDotNotationIdentifier(), controlVariable.type.toString(), controlVariable.identifier));
    sb.append("%s -> %s [label=\"controlVariable\"];\n".formatted(getDotNotationIdentifier(), controlVariable.getDotNotationIdentifier()));

    sb.append(initialValue.toDotNotation());
    sb.append("%s -> %s [label=\"initialValue\"];\n".formatted(getDotNotationIdentifier(), initialValue.getDotNotationIdentifier()));

    sb.append(finalValue.toDotNotation());
    sb.append("%s -> %s [label=\"finalValue\"];\n".formatted(getDotNotationIdentifier(), finalValue.getDotNotationIdentifier()));

    sb.append(body.toDotNotation());
    sb.append("%s -> %s [label=\"body\"];\n".formatted(getDotNotationIdentifier(), body.getDotNotationIdentifier()));

    return sb.toString();
  }
}
