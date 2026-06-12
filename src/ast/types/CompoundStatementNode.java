package ast.types;

import java.util.List;

public class CompoundStatementNode extends StatementNode {
  List<StatementNode> statements;

  public CompoundStatementNode(List<StatementNode> statements) {
    this.statements = statements;
  }
}
