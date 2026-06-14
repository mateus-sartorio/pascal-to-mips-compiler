package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;

public class RealExpressionNode extends ExpressionNode {
  public final double value;

  public RealExpressionNode(double value) {
    this.value = value;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"%.2f\"];\n".formatted(getDotNotationIdentifier(), value));
    
    return sb.toString();
  }
}
