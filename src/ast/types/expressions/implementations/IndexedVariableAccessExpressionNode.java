package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveVariableType;
import types.VariableType;

/**
 * Representa um nó de expressão de acesso a uma variável indexada na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador da variável, o tipo da variável e a expressão que representa o índice.
 */
public class IndexedVariableAccessExpressionNode extends VariableAccessExpressionNode {
  /**
   * Expressão que representa o índice da variável.
   */
  public final ExpressionNode indexExpressionNode;

  /**
   * Construtor para criar um nó de expressão de acesso a uma variável indexada.
   *
   * @param id Identificador único para o nó.
   * @param identifier Identificador da variável que está sendo acessada.
   * @param type Tipo da variável que está sendo acessada.
   * @param index Expressão que representa o índice da variável.
   */
  public IndexedVariableAccessExpressionNode(int id, String identifier, VariableType type, ExpressionNode index) {
    super(id, identifier, new PrimitiveVariableType(type.basePrimitiveType));
    this.indexExpressionNode = index;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s[]\"];\n".formatted(getDotNotationIdentifier(), type.basePrimitiveType, identifier));
    
    sb.append(indexExpressionNode.toDotNotation());
    sb.append("%s -> %s [label=\"index\"] ;\n".formatted(getDotNotationIdentifier(), indexExpressionNode.getDotNotationIdentifier()));

    return sb.toString();
  }
}
