package ast.types;

public class AssignmentStatementNode extends StatementNode {
  String variableIdentifier;
  ExpressionNode expression;

  public AssignmentStatementNode(String variableNameIdentifier, ExpressionNode expression) {
    this.variableIdentifier = variableNameIdentifier;
    this.expression = expression;
  }
}
