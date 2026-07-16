package ast.types.expressions.contracts;

import types.VariableType;

/**
 * Representa um nó de expressão binária na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão à esquerda, a expressão à direita e o tipo da operação binária.
 */
public abstract class BinaryOperatorExpressionNode extends ExpressionNode {
  /**
   * Expressão à esquerda da operação binária.
   */
  public final ExpressionNode left;
  /**
   * Expressão à direita da operação binária.
   */
  public final ExpressionNode right;

  /**
   * Construtor para criar um nó de expressão binária.
   *
   * @param id Identificador único para o nó.
   * @param left Expressão à esquerda da operação binária.
   * @param right Expressão à direita da operação binária.
   * @param type Tipo da operação binária.
   */
  public BinaryOperatorExpressionNode(int id, ExpressionNode left, ExpressionNode right, VariableType type) {
    super(id, type);
    this.left = left;
    this.right = right;
  }
}
