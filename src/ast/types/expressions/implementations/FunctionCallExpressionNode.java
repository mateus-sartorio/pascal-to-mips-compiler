package ast.types.expressions.implementations;

import java.util.List;

import ast.types.expressions.contracts.ExpressionNode;
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

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"%s() -> %s\"];\n".formatted(getDotNotationIdentifier(), procedureIdentifier, returnType));

    for(var argument : arguments) {
      sb.append(argument.toDotNotation());
      sb.append(getDotNotationIdentifier() + " -> " + argument.getDotNotationIdentifier() + " [label=\"argument\"];\n");
    }
    
    return sb.toString();
  }
}
