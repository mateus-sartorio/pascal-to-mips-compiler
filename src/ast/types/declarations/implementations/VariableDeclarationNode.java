package ast.types.declarations.implementations;

import ast.types.declarations.contracts.DeclarationNode;
import types.VariableType;

public class VariableDeclarationNode extends DeclarationNode {
  String identifier;
  VariableType type;

  public VariableDeclarationNode(String identifier, VariableType type) {
    this.identifier = identifier;
    this.type = type;
  }

  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, identifier));
    
    return sb.toString();
  }
}
