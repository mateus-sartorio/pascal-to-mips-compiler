package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.VariableType;

public class FunctionReturnAssignmentExpressionNode extends ExpressionNode {
  public final String functionIdentifier;
  public final VariableType type;

  public FunctionReturnAssignmentExpressionNode(String functionIdentifier, VariableType type) {
    this.functionIdentifier = functionIdentifier;
    this.type = type;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, functionIdentifier));
    
    return sb.toString();
  }
}
