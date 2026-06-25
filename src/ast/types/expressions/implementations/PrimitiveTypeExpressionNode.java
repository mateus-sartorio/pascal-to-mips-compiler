package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;

public class PrimitiveTypeExpressionNode<T> extends ExpressionNode {
  public final T value;

  public PrimitiveTypeExpressionNode(int id, T value) {
    super(id);
    this.value = value;
  }

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
