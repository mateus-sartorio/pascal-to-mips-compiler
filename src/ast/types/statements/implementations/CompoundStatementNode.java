package ast.types.statements.implementations;

import java.util.List;

import ast.types.statements.contract.StatementNode;

public class CompoundStatementNode extends StatementNode {
  List<StatementNode> statements;

  public CompoundStatementNode(List<StatementNode> statements) {
    this.statements = statements;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"begin...end;\"];\n".formatted(getDotNotationIdentifier()));
    
    for (var statement : statements) {
      sb.append(statement.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), statement.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
