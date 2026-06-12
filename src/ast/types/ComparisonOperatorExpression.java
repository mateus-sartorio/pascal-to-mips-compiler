package ast.types;

public class ComparisonOperatorExpression extends BinaryOperatorExpressionNode {
  String operator;
  
  public ComparisonOperatorExpression(
    ExpressionNode left,
    ExpressionNode right,
    String operator
  ) {
    super(left, right);
    this.operator = operator;
  }
}
