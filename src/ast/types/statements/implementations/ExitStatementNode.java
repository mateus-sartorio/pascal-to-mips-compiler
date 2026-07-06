package ast.types.statements.implementations;

import ast.types.statements.contract.StatementNode;

public class ExitStatementNode extends StatementNode {
  public ExitStatementNode(int id) {
    super(id);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"exit\"];\n".formatted(getDotNotationIdentifier()));

    return sb.toString();
  }
}
