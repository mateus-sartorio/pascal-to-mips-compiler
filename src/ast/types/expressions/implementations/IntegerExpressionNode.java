package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;

public class IntegerExpressionNode extends ExpressionNode {
  public final int value;

  public IntegerExpressionNode(int value) {
    this.value = value;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"%d\"];\n".formatted(getDotNotationIdentifier(), value));
    
    return sb.toString();
  }
}
