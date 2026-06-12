package ast.types;

public class ArithmeticBinaryOperatorExpressionNode extends BinaryOperatorExpressionNode {
  String operator;

  public ArithmeticBinaryOperatorExpressionNode(ExpressionNode left, ExpressionNode right, String operator) {
    super(left, right);
    this.operator = operator;
  }
}
