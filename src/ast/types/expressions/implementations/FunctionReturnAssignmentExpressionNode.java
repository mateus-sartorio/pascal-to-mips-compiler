package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.VariableType;

public class FunctionReturnAssignmentExpressionNode extends ExpressionNode {
  public final String functionIdentifier;

  public FunctionReturnAssignmentExpressionNode(int id, String functionIdentifier, VariableType type) {
    super(id, type);
    this.functionIdentifier = functionIdentifier;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, functionIdentifier));
    
    return sb.toString();
  }
}
