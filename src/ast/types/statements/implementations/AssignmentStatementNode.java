package ast.types.statements.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.implementations.VariableAccessExpressionNode;
import ast.types.statements.contract.StatementNode;

/**
 * Representa um nó de instrução de atribuição na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão de acesso à variável e a expressão que está sendo atribuída.
 */
public class AssignmentStatementNode extends StatementNode {
  /**
   * Expressão de acesso à variável que está sendo atribuída.
   */
  public final VariableAccessExpressionNode variableAccessExpressionNode;
  /**
   * Expressão que está sendo atribuída à variável.
   */
  public final ExpressionNode expression;

  /**
   * Construtor para criar um nó de instrução de atribuição.
   *
   * @param id Identificador único para o nó.
   * @param variableAccessExpressionNode Expressão de acesso à variável que está sendo atribuída.
   * @param expression Expressão que está sendo atribuída à variável.
   */
  public AssignmentStatementNode(int id, VariableAccessExpressionNode variableAccessExpressionNode, ExpressionNode expression) {
    super(id);
    this.variableAccessExpressionNode = variableAccessExpressionNode;
    this.expression = expression;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\":=\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(variableAccessExpressionNode.toDotNotation());
    sb.append("%s -> %s [label=\"variable\"] ;\n".formatted(getDotNotationIdentifier(), variableAccessExpressionNode.getDotNotationIdentifier()));

    sb.append(expression.toDotNotation());
    sb.append("%s -> %s [label=\"value\"] ;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));

    return sb.toString();
  }
}
