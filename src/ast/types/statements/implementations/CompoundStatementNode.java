package ast.types.statements.implementations;

import java.util.List;

import ast.types.statements.contract.StatementNode;

public class CompoundStatementNode extends StatementNode {
  public final List<StatementNode> statements;

  public CompoundStatementNode(int id, List<StatementNode> statements) {
    super(id);
    this.statements = statements;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"{ }\"];\n".formatted(getDotNotationIdentifier()));
    
    for (var statement : statements) {
      sb.append(statement.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), statement.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
