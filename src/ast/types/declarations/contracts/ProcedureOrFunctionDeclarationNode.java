package ast.types.declarations.contracts;

import java.util.Optional;

import ast.types.declarations.implementations.VariableDeclarationPartNode;
import ast.types.statements.implementations.CompoundStatementNode;

public abstract class ProcedureOrFunctionDeclarationNode extends DeclarationNode {
  public final String identifier;
  public final Optional<VariableDeclarationPartNode> parameters;
  public final Optional<VariableDeclarationPartNode> localVariables;
  public final CompoundStatementNode compoundStatement;

  public ProcedureOrFunctionDeclarationNode(
    int id,
    String identifier,
    Optional<VariableDeclarationPartNode> parameters,
    Optional<VariableDeclarationPartNode> localVariables,
    CompoundStatementNode compoundStatement
  ) {
    super(id);
    this.identifier = identifier;
    this.parameters = parameters;
    this.localVariables = localVariables;
    this.compoundStatement = compoundStatement;
  }
}
