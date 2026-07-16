package ast.types.declarations.implementations;

import ast.types.declarations.contracts.DeclarationNode;
import types.VariableType;

/**
 * Representa um nó de declaração de variável na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador da variável e seu tipo.
 */
public class VariableDeclarationNode extends DeclarationNode {
  /**
   * Identificador da variável.
   */
  public final String identifier;
  /**
   * Tipo da variável.
   */
  public final VariableType type;

  /**
   * Construtor para criar um nó de declaração de variável.
   *
   * @param id Identificador único para o nó.
   * @param identifier Identificador da variável.
   * @param type Tipo da variável.
   */
  public VariableDeclarationNode(int id, String identifier, VariableType type) {
    super(id);
    this.identifier = identifier;
    this.type = type;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, identifier));
    
    return sb.toString();
  }
}
