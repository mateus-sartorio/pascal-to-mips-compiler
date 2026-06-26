package ast.types.declarations.implementations;

import ast.types.declarations.contracts.DeclarationNode;
import types.VariableType;

public class VariableDeclarationNode extends DeclarationNode {
  public final String identifier;
  public final VariableType type;

  public VariableDeclarationNode(int id, String identifier, VariableType type) {
    super(id);
    this.identifier = identifier;
    this.type = type;
  }

  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"(%s) %s\"];\n".formatted(getDotNotationIdentifier(), type, identifier));
    
    return sb.toString();
  }
}
