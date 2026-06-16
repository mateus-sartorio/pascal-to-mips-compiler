package ast.types.declarations.implementations;

import java.util.List;

import ast.types.AstNode;

public class VariableDeclarationPartNode extends AstNode {
  public final List<VariableDeclarationNode> variables;

  public VariableDeclarationPartNode(List<VariableDeclarationNode> variables) {
    this.variables = variables;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"variables\"];\n".formatted(getDotNotationIdentifier()));
    
    for (VariableDeclarationNode variable : variables) {
      sb.append(variable.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), variable.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
