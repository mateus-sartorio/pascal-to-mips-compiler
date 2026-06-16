package ast.types.statements.implementations;

import java.util.Optional;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

public class IfStatementNode extends StatementNode {
  public final ExpressionNode condition;
  public final StatementNode thenStatement;
  public final Optional<StatementNode> elseStatement;

  public IfStatementNode(
    ExpressionNode condition,
    StatementNode thenStatement,
    Optional<StatementNode> elseStatement
  ) {
    this.condition = condition;
    this.thenStatement = thenStatement;
    this.elseStatement = elseStatement;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"if\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(condition.toDotNotation());
    sb.append("%s -> %s [label=\"condition\"];\n".formatted(getDotNotationIdentifier(), condition.getDotNotationIdentifier()));

    sb.append(thenStatement.toDotNotation());
    sb.append("%s -> %s [label=\"then\"];\n".formatted(getDotNotationIdentifier(), thenStatement.getDotNotationIdentifier()));
    
    elseStatement.ifPresent(statement -> {
      sb.append(statement.toDotNotation());
      sb.append("%s -> %s [label=\"else\"];\n".formatted(getDotNotationIdentifier(), statement.getDotNotationIdentifier()));
    });
    
    return sb.toString();
  }
}
