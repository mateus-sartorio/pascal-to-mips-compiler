package ast.types;

public class IfStatementStatementNode extends StatementNode {
  ExpressionNode condition;
  CompoundStatementNode thenStatement;
  CompoundStatementNode elseStatement;

  public IfStatementStatementNode(ExpressionNode condition, CompoundStatementNode thenStatement, CompoundStatementNode elseStatement) {
    this.condition = condition;
    this.thenStatement = thenStatement;
    this.elseStatement = elseStatement;
  }
}
