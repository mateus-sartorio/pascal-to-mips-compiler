package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;

public class BooleanExpressionNode extends ExpressionNode {
  public final boolean value;

  public BooleanExpressionNode(boolean value) {
    this.value = value;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\": %s\"];\n".formatted(getDotNotationIdentifier(), value));
    
    return sb.toString();
  }
}
