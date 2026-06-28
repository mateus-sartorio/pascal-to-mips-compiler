package ast.types.expressions.implementations;

import types.VariableType;

public class FunctionReturnAssignmentExpressionNode extends VariableAccessExpressionNode {

  public FunctionReturnAssignmentExpressionNode(int id, String functionIdentifier, VariableType type) {
    super(id, functionIdentifier, type);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, identifier));
    
    return sb.toString();
  }
}
