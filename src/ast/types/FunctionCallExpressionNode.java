package ast.types;

import java.util.List;

import types.PrimitiveTypeEnum;

public class FunctionCallExpressionNode extends ExpressionNode {
  String procedureIdentifier;
  List<ExpressionNode> arguments;
  PrimitiveTypeEnum returnType;

  public FunctionCallExpressionNode(String procedureIdentifier, List<ExpressionNode> arguments, PrimitiveTypeEnum returnType) {
    this.procedureIdentifier = procedureIdentifier;
    this.arguments = arguments;
    this.returnType = returnType;
  }
}
