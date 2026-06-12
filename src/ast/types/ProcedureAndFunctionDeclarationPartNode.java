package ast.types;

import java.util.List;

public class ProcedureAndFunctionDeclarationPartNode extends AstNode {
  List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarations;

  public ProcedureAndFunctionDeclarationPartNode(List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarations) {
    this.procedureOrFunctionDeclarations = procedureOrFunctionDeclarations;
  }
}
