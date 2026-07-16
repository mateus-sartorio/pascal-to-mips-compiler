package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

/**
 * Representa um nó de expressão que converte um caractere em uma string na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão que está sendo convertida.
 */
public class CharToStringExpressionNode extends ExpressionNode {
  /**
   * Expressão que está sendo convertida de caractere para string.
   */
  public final ExpressionNode expression;

  /**
   * Construtor para criar um nó de expressão de conversão de caractere para string.
   *
   * @param id Identificador único para o nó.
   * @param expression Expressão que está sendo convertida de caractere para string.
   */
  public CharToStringExpressionNode(int id, ExpressionNode expression) {
    super(id, new PrimitiveVariableType(PrimitiveTypeEnum.STRING));
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
    sb.append("%s [label=\"(char) -> (string)\"];\n".formatted(getDotNotationIdentifier()));
    sb.append(expression.toDotNotation());
    sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), expression.getDotNotationIdentifier()));
    return sb.toString();
  }
}
