package ast.types;

import types.VariableType;

public class VariableDeclarationNode extends DeclarationNode {
  String identifier;
  VariableType type;

  public VariableDeclarationNode(String identifier, VariableType type) {
    this.identifier = identifier;
    this.type = type;
  }
}
