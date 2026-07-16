package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.VariableType;

/**
 * Representa um nó de expressão de acesso a uma variável na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador da variável e o tipo da variável.
 */
public class VariableAccessExpressionNode extends ExpressionNode {
  /**
   * Identificador da variável que está sendo acessada.
   */
  public final String identifier;

  /**
   * Construtor para criar um nó de expressão de acesso a uma variável.
   *
   * @param id Identificador único para o nó.
   * @param identifier Identificador da variável que está sendo acessada.
   * @param type Tipo da variável que está sendo acessada.
   */
  public VariableAccessExpressionNode(int id, String identifier, VariableType type) {
    super(id, type);
    this.identifier = identifier;
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
