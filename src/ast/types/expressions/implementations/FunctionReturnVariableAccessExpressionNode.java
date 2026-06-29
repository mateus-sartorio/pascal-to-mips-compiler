package ast.types.expressions.implementations;

import types.VariableType;

public class FunctionReturnVariableAccessExpressionNode extends VariableAccessExpressionNode {

  public FunctionReturnVariableAccessExpressionNode(int id, String functionIdentifier, VariableType type) {
    super(id, functionIdentifier, type);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, identifier));
    
    return sb.toString();
  }
}
