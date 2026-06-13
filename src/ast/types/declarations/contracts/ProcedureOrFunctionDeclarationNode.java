package ast.types.declarations.contracts;

import ast.types.declarations.implementations.VariableDeclarationPartNode;
import ast.types.statements.implementations.CompoundStatementNode;

public abstract class ProcedureOrFunctionDeclarationNode extends DeclarationNode {
  public String identifier;
  public VariableDeclarationPartNode parameters;
  public VariableDeclarationPartNode localVariables;
  public CompoundStatementNode compoundStatement;

  public ProcedureOrFunctionDeclarationNode(
    String identifier,
    VariableDeclarationPartNode parameters,
    VariableDeclarationPartNode localVariables,
    CompoundStatementNode compoundStatement
  ) {
    this.identifier = identifier;
    this.parameters = parameters;
    this.localVariables = localVariables;
    this.compoundStatement = compoundStatement;
  }
}
