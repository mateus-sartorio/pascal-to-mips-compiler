package ast.types.declarations.contracts;

import ast.types.AstNode;

/**
 * Representa um nó de declaração na árvore de sintaxe abstrata (AST).
 * Este nó é uma classe abstrata que serve como base para diferentes tipos de declarações.
 */
public abstract class DeclarationNode extends AstNode {
  /**
   * Construtor para criar um nó de declaração.
   *
   * @param id Identificador único para o nó.
   */
  public DeclarationNode(int id) {
    super(id);
  }
}
