package ast.types.statements.implementations;

import java.util.List;

import ast.types.statements.contract.StatementNode;

/**
 * Representa um nó de instrução composta na árvore de sintaxe abstrata (AST).
 * Este nó contém uma lista de instruções que são executadas em sequência.
 */
public class CompoundStatementNode extends StatementNode {
  /**
   * Lista de instruções que compõem a instrução composta.
   */
  public final List<StatementNode> statements;

  /**
   * Construtor para criar um nó de instrução composta.
   *
   * @param id Identificador único para o nó.
   * @param statements Lista de instruções que compõem a instrução composta.
   */
  public CompoundStatementNode(int id, List<StatementNode> statements) {
    super(id);
    this.statements = statements;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"{ }\"];\n".formatted(getDotNotationIdentifier()));
    
    for (var statement : statements) {
      sb.append(statement.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), statement.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
