package ast.types.statements.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.implementations.VariableAccessExpressionNode;

/**
 * Representa um nó de instrução de retorno na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão de acesso à variável e a expressão que está sendo retornada.
 */
public class ReturnStatementNode extends AssignmentStatementNode {
  public ReturnStatementNode(int id, VariableAccessExpressionNode variableAccessExpressionNode, ExpressionNode expression) {
    super(id, variableAccessExpressionNode, expression);
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"return\"];\n".formatted(getDotNotationIdentifier()));

    sb.append(expression.toDotNotation());
    sb.append("%s -> %s [label=\"value\"] ;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));

    return sb.toString();
  }
}
