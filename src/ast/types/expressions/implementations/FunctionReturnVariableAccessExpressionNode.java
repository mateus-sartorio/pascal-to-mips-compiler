package ast.types.expressions.implementations;

import types.VariableType;

/**
 * Representa um nó de expressão de acesso à variável de retorno de uma função na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a função e o tipo da variável de retorno.
 */
public class FunctionReturnVariableAccessExpressionNode extends VariableAccessExpressionNode {

  /**
   * Construtor para criar um nó de expressão de acesso à variável de retorno de uma função.
   *
   * @param id Identificador único para o nó.
   * @param functionIdentifier Identificador da função que possui a variável de retorno.
   * @param type Tipo da variável de retorno da função.
   */
  public FunctionReturnVariableAccessExpressionNode(int id, String functionIdentifier, VariableType type) {
    super(id, functionIdentifier, type);
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
