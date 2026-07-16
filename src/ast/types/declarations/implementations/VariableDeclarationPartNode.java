package ast.types.declarations.implementations;

import java.util.List;

import ast.types.AstNode;

/**
 * Representa um nó de parte de declaração de variáveis na árvore de sintaxe abstrata (AST).
 * Este nó contém uma lista de declarações de variáveis.
 */
public class VariableDeclarationPartNode extends AstNode {
  public final List<VariableDeclarationNode> variables;

  /**
   * Construtor para criar um nó de parte de declaração de variáveis.
   *
   * @param id Identificador único para o nó.
   * @param variables Lista de declarações de variáveis.
   */
  public VariableDeclarationPartNode(int id, List<VariableDeclarationNode> variables) {
    super(id);
    this.variables = variables;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"variables\"];\n".formatted(getDotNotationIdentifier()));
    
    for (VariableDeclarationNode variable : variables) {
      sb.append(variable.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), variable.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
