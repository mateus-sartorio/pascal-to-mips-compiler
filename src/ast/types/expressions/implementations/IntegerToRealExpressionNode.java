package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

/**
 * Representa um nó de expressão que converte um número inteiro em um número real na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão que está sendo convertida.
 */
public class IntegerToRealExpressionNode extends ExpressionNode {
  /**
   * Expressão que está sendo convertida de inteiro para real.
   */
  public final ExpressionNode expression;

  /**
   * Construtor para criar um nó de expressão de conversão de inteiro para real.
   *
   * @param id Identificador único para o nó.
   * @param expression Expressão que está sendo convertida de inteiro para real.
   */
  public IntegerToRealExpressionNode(int id, ExpressionNode expression) {
    super(id, new PrimitiveVariableType(PrimitiveTypeEnum.REAL));
    this.expression = expression;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    sb.append("%s [label=\"(integer) -> (real)\"];\n".formatted(getDotNotationIdentifier()));
    sb.append(expression.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));
    return sb.toString();
  }
}
