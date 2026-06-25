package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.VariableType;

public class VariableAccessExpressionNode extends ExpressionNode {
  public final String identifier;
  public final VariableType type;

  public VariableAccessExpressionNode(int id, String identifier, VariableType type) {
    super(id);
    this.identifier = identifier;
    this.type = type;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, identifier));
    
    return sb.toString();
  }
}
