package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.contracts.UnaryOperatorExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

/**
 * Representa um nó de expressão de operador lógico "not" na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão que está sendo negada.
 */
public class NotOperatorExpressionNode extends UnaryOperatorExpressionNode {
  /**
   * Construtor para criar um nó de expressão de operador lógico "not".
   *
   * @param id Identificador único para o nó.
   * @param expression Expressão que está sendo negada.
   */
  public NotOperatorExpressionNode(int id, ExpressionNode expression) {
    super(id, expression, new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN));
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"not\"];\n".formatted(getDotNotationIdentifier()));
    
    sb.append(expression.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));
    
    return sb.toString();
  }
}
