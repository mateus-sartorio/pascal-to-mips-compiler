package ast.types.expressions.implementations;

import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveVariableType;
import types.TypeRules;
import types.VariableType;

/**
 * Representa um nó de expressão de operador aritmético na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a expressão à esquerda, a expressão à direita e o operador aritmético.
 */
public class ArithmeticOperatorExpressionNode extends BinaryOperatorExpressionNode {
  /**
   * Operador aritmético representado por este nó.
   */
  public final String operator;

  /**
   * Construtor para criar um nó de expressão de operador aritmético.
   *
   * @param id Identificador único para o nó.
   * @param left Expressão à esquerda da operação aritmética.
   * @param right Expressão à direita da operação aritmética.
   * @param operator Operador aritmético representado por este nó.
   */
  public ArithmeticOperatorExpressionNode(int id, ExpressionNode left, ExpressionNode right, String operator) {
    VariableType resultType = left.type;
    
    if(operator.equals("+")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.PLUS_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }
    else if(operator.equals("-") || operator.equals("*")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.MATH_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }
    else if(operator.equals("/")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.REAL_DIVISION_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }
    else if(operator.equals("div")) {
      resultType = new PrimitiveVariableType(TypeRules.getResultType(
        TypeRules.INTEGER_DIVISION_TABLE,
        left.type.basePrimitiveType,
        right.type.basePrimitiveType
      ));
    }

    super(id, left, right, resultType);
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
