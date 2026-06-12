package ast.types;

import java.util.List;

public class ProcedureDeclarationNode extends ProcedureOrFunctionDeclarationNode {
  String identifier;

  List<VariableDeclarationNode> parameters;
  List<VariableDeclarationNode> localVariables;

  CompoundStatementNode compoundStatement;

  public ProcedureDeclarationNode(
    String identifier,
    List<VariableDeclarationNode> parameters,
    List<VariableDeclarationNode> localVariables,
    CompoundStatementNode compoundStatement
  ) {
    this.identifier = identifier;
    this.parameters = parameters;
    this.localVariables = localVariables;
    this.compoundStatement = compoundStatement;
  }
}
