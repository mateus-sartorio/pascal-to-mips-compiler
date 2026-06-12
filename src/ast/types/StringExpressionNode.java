package ast.types;

public class StringExpressionNode extends ExpressionNode {
  public final String value;

  public StringExpressionNode(String value) {
    this.value = value;
  }
}
