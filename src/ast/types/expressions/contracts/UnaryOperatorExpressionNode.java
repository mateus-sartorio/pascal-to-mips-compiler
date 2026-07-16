package ast.types.expressions.contracts;

import types.VariableType;

/**
 * Representa um nó de expressão unária na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão e o tipo da operação unária.
 */
public abstract class UnaryOperatorExpressionNode extends ExpressionNode {
  /**
   * Expressão que está sendo operada pela operação unária.
   */
  public final ExpressionNode expression;

  /**
   * Construtor para criar um nó de expressão unária.
   *
   * @param id Identificador único para o nó.
   * @param expression Expressão que está sendo operada pela operação unária.
   * @param type Tipo da operação unária.
   */
  public UnaryOperatorExpressionNode(int id, ExpressionNode expression, VariableType type) {
    super(id, type);
    this.expression = expression;
  }
}
