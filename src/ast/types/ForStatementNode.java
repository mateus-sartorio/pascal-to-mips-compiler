package ast.types;

public class ForStatementNode extends StatementNode {
  String controlVariableIdentifier;
  ExpressionNode initialValue;
  ExpressionNode finalValue;
  boolean isDownto;
  CompoundStatementNode body;

  public ForStatementNode(
    String controlVariableIdentifier,
    ExpressionNode initialValue,
    ExpressionNode finalValue,
    boolean isDownto,
    CompoundStatementNode body
  ) {
    this.controlVariableIdentifier = controlVariableIdentifier;
    this.initialValue = initialValue;
    this.finalValue = finalValue;
    this.isDownto = isDownto;
    this.body = body;
  }
}
