package ast.types;

public class LogicOperatorExpressionNode extends BinaryOperatorExpressionNode {
  String operator;

  public LogicOperatorExpressionNode(ExpressionNode left, ExpressionNode right, String operator) {
    super(left, right);
    this.operator = operator;
  }
}
