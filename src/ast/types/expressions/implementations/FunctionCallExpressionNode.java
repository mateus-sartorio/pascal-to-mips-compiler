package ast.types.expressions.implementations;

import java.util.List;

import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class FunctionCallExpressionNode extends ExpressionNode {
  public final String functionIdentifier;
  public final List<ExpressionNode> arguments;

  public FunctionCallExpressionNode(int id, String procedureIdentifier, List<ExpressionNode> arguments, PrimitiveTypeEnum returnType) {
    super(id, new PrimitiveVariableType(returnType));
    this.functionIdentifier = procedureIdentifier;
    this.arguments = arguments;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"(%s) %s()\"];\n".formatted(getDotNotationIdentifier(), type.basePrimitiveType, functionIdentifier));

    for(var argument : arguments) {
      sb.append(argument.toDotNotation());
      sb.append("%s -> %s [label=\"argument\"];\n".formatted(getDotNotationIdentifier(), argument.getDotNotationIdentifier()));
    }
    
    return sb.toString();
  }
}
