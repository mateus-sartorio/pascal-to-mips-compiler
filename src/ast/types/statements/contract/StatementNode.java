package ast.types.statements.contract;

import ast.types.AstNode;

/**
 * Representa um nó de instrução na árvore de sintaxe abstrata (AST).
 * Este nó é uma classe abstrata que serve como base para diferentes tipos de instruções.
 */
public abstract class StatementNode extends AstNode {
  public StatementNode(int id) {
    super(id);
  }
}
