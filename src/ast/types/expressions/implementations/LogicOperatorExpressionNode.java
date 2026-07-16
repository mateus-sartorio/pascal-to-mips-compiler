package ast.types.expressions.implementations;

import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

/**
 * Representa um nó de expressão de operador lógico na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão à esquerda, a expressão à direita e o operador lógico.
 */
public class LogicOperatorExpressionNode extends BinaryOperatorExpressionNode {
  /**
   * Operador lógico representado por este nó.
   */
  public final String operator;

  /**
   * Construtor para criar um nó de expressão de operador lógico.
   *
   * @param id Identificador único para o nó.
   * @param left Expressão à esquerda da operação lógica.
   * @param right Expressão à direita da operação lógica.
   * @param operator Operador lógico representado por este nó.
   */
  public LogicOperatorExpressionNode(int id, ExpressionNode left, ExpressionNode right, String operator) {
    super(id, left, right, new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN));
    this.operator = operator;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"%s\"];\n".formatted(getDotNotationIdentifier(), operator));
    sb.append(left.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), left.getDotNotationIdentifier()));

    sb.append(right.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), right.getDotNotationIdentifier()));
    
    return sb.toString();
  }
}
