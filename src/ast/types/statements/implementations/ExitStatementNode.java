package ast.types.statements.implementations;

import ast.types.statements.contract.StatementNode;

/**
 * Representa um nó de instrução de saída na árvore de sintaxe abstrata (AST).
 * Este nó é usado para indicar a saída de um procedimento ou função.
 */
public class ExitStatementNode extends StatementNode {
  public ExitStatementNode(int id) {
    super(id);
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"exit\"];\n".formatted(getDotNotationIdentifier()));

    return sb.toString();
  }
}
