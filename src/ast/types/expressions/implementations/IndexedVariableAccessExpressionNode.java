package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.VariableType;

public class IndexedVariableAccessExpressionNode extends VariableAccessExpressionNode {
  ExpressionNode indexExpressionNode;

  public IndexedVariableAccessExpressionNode(String identifier, VariableType type, ExpressionNode index) {
    super(identifier, type);
    this.indexExpressionNode = index;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"(%s) %s[]\"];\n".formatted(getDotNotationIdentifier(), type.basePrimitiveType, identifier));
    
    sb.append(indexExpressionNode.toDotNotation());
    sb.append("%s -> %s [label=\"index\"] ;\n".formatted(getDotNotationIdentifier(), indexExpressionNode.getDotNotationIdentifier()));

    return sb.toString();
  }
}
