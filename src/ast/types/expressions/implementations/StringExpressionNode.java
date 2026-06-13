package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;

public class StringExpressionNode extends ExpressionNode {
  public final String value;

  public StringExpressionNode(String value) {
    this.value = value;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"'%s'\"];\n".formatted(getDotNotationIdentifier(), value));
    
    return sb.toString();
  }
}
