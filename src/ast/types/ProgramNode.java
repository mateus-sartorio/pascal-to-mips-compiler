package ast.types;

import java.util.List;

public class ProgramNode extends AstNode {
  List<VariableDeclarationNode> globalVariables;
  ProcedureAndFunctionDeclarationPartNode proceduresAndFunctions;
  CompoundStatementNode compoundStatement;

  public ProgramNode(
    List<VariableDeclarationNode> globalVariables,
    ProcedureAndFunctionDeclarationPartNode proceduresAndFunctions,
    CompoundStatementNode compoundStatement
  ) {
    this.globalVariables = globalVariables;
    this.proceduresAndFunctions = proceduresAndFunctions;
    this.compoundStatement = compoundStatement;
  }
}
