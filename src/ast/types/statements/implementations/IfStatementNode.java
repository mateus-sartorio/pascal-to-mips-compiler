package ast.types.statements.implementations;

import java.util.Optional;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

/**
 * Representa um nó de instrução condicional "if" na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a condição, a instrução "then" e a instrução "else" (opcional).
 */
public class IfStatementNode extends StatementNode {
  /**
   * Condição da instrução "if".
   */
  public final ExpressionNode condition;
  /**
   * Instrução a ser executada se a condição for verdadeira.
   */
  public final StatementNode thenStatement;
  /**
   * Instrução a ser executada se a condição for falsa (opcional).
   */
  public final Optional<StatementNode> elseStatement;

  /**
   * Construtor para criar um nó de instrução condicional "if".
   *
   * @param id Identificador único para o nó.
   * @param condition Condição da instrução "if".
   * @param thenStatement Instrução a ser executada se a condição for verdadeira.
   * @param elseStatement Instrução a ser executada se a condição for falsa (opcional).
   */
  public IfStatementNode(
    int id,
    ExpressionNode condition,
    StatementNode thenStatement,
    Optional<StatementNode> elseStatement
  ) {
    super(id);
    this.condition = condition;
    this.thenStatement = thenStatement;
    this.elseStatement = elseStatement;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
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
