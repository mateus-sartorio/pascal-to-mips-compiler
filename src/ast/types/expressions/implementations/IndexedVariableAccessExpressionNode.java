package ast.types.expressions.implementations;

import ast.types.expressions.contracts.ExpressionNode;
import types.VariableType;

public class IndexedVariableAccessExpressionNode extends VariableAccessExpressionNode {
  ExpressionNode indexExpressionNode;

  public IndexedVariableAccessExpressionNode(String identifier, VariableType type, ExpressionNode index) {
    super(identifier, type);
    this.indexExpressionNode = index;
  }
}
