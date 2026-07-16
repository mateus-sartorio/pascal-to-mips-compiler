package ast.types.expressions.contracts;

import ast.types.AstNode;
import types.VariableType;

/**
 * Representa um nó de expressão na árvore de sintaxe abstrata (AST).
 * Este nó é uma classe abstrata que serve como base para diferentes tipos de expressões.
 */
public abstract class ExpressionNode extends AstNode {
  /**
   * Tipo da expressão.
   */
  public final VariableType type;
  
  /**
   * Construtor para criar um nó de expressão.
   *
   * @param id Identificador único para o nó.
   * @param type Tipo da expressão.
   */
  public ExpressionNode(int id, VariableType type) {
    super(id);
    this.type = type;
  }
}
