package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;
import types.VariableType;

/**
 * Representa um nó de expressão de tipo primitivo na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o valor do tipo primitivo.
 *
 * @param <T> Tipo do valor primitivo (Integer, Double, String, Boolean, Character).
 */
public class PrimitiveTypeExpressionNode<T> extends ExpressionNode {
  public final T value;

  /**
   * Construtor para criar um nó de expressão de tipo primitivo.
   *
   * @param id Identificador único para o nó.
   * @param value Valor do tipo primitivo.
   */
  public PrimitiveTypeExpressionNode(int id, T value) {
    VariableType expressionType = switch (value) {
      case Integer _ -> new PrimitiveVariableType(PrimitiveTypeEnum.INTEGER);
      case Double _ -> new PrimitiveVariableType(PrimitiveTypeEnum.REAL);
      case String _ -> new PrimitiveVariableType(PrimitiveTypeEnum.STRING);
      case Boolean _ -> new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN);
      case Character _ -> new PrimitiveVariableType(PrimitiveTypeEnum.CHAR);
      default -> throw new RuntimeException("Unsupported primitive type: " + value.getClass().getSimpleName());
    };

    super(id, expressionType);
    this.value = value;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    switch (value) {
      case String s -> sb.append("%s [label=\"'%s'\"];\n".formatted(getDotNotationIdentifier(), s));
      case Character c -> sb.append("%s [label=\"'%s'\"];\n".formatted(getDotNotationIdentifier(), c));
      case Double d -> sb.append("%s [label=\"%.2f\"];\n".formatted(getDotNotationIdentifier(), d));
      default -> sb.append("%s [label=\"%s\"];\n".formatted(getDotNotationIdentifier(), value.toString()));
    }
    
    return sb.toString();
  }
}
