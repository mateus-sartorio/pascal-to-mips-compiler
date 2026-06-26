package ast.types.expressions.contracts;

import ast.types.AstNode;
import types.VariableType;

public abstract class ExpressionNode extends AstNode {
  public final VariableType type;
  
  public ExpressionNode(int id, VariableType type) {
    super(id);
    this.type = type;
  }
}
