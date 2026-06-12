package ast.types;

public class RealExpressionNode extends ExpressionNode {
  public final double value;

  public RealExpressionNode(double value) {
    this.value = value;
  }
}
