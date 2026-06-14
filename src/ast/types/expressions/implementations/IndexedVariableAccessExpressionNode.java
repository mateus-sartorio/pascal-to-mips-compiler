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

    if(indexExpressionNode instanceof IntegerExpressionNode) {
      var integerExpressionNode = (IntegerExpressionNode) indexExpressionNode;
      sb.append("%s [label=\"(%s) %s[%d]\"];\n".formatted(getDotNotationIdentifier(), type.basePrimitiveType, identifier, integerExpressionNode.value));
    } else {
      sb.append("%s [label=\"(%s) %s[]\"];\n".formatted(getDotNotationIdentifier(), type.basePrimitiveType, identifier));
    }

    return sb.toString();
  }
}
