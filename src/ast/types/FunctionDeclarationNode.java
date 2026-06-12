package ast.types;

import java.util.List;

import types.PrimitiveTypeEnum;

public class FunctionDeclarationNode extends ProcedureOrFunctionDeclarationNode {
  String identifier;

  List<VariableDeclarationNode> parameters;
  List<VariableDeclarationNode> localVariables;

  PrimitiveTypeEnum returnType;

  CompoundStatementNode compoundStatement;

  public FunctionDeclarationNode(
    String identifier,
    List<VariableDeclarationNode> parameters,
    List<VariableDeclarationNode> localVariables,
    PrimitiveTypeEnum returnType,
    CompoundStatementNode compoundStatement
  ) {
    this.identifier = identifier;
    this.parameters = parameters;
    this.localVariables = localVariables;
    this.returnType = returnType;
    this.compoundStatement = compoundStatement;
  }
}
