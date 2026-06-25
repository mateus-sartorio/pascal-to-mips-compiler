package ast.types.declarations.implementations;

import java.util.List;

import ast.types.AstNode;
import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;

public class ProcedureAndFunctionDeclarationPartNode extends AstNode {
  public final List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarations;

  public ProcedureAndFunctionDeclarationPartNode(int id, List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarations) {
    super(id);
    this.procedureOrFunctionDeclarations = procedureOrFunctionDeclarations;
  }

  @Override
  public String getDotNotationIdentifier() {
    return "\"ProcedureAndFunctionDeclarationPartNode\"";
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"procedures and functions\"];\n".formatted(getDotNotationIdentifier()));
    
    for (ProcedureOrFunctionDeclarationNode procedureOrFunction : procedureOrFunctionDeclarations) {
      sb.append(procedureOrFunction.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), procedureOrFunction.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
