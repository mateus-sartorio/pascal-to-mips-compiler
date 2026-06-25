package ast.types.declarations.implementations;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.statements.implementations.CompoundStatementNode;
import types.PrimitiveTypeEnum;

public class FunctionDeclarationNode extends ProcedureOrFunctionDeclarationNode {
  public final PrimitiveTypeEnum returnType;

  public FunctionDeclarationNode(
    int id,
    String identifier,
    VariableDeclarationPartNode parameters,
    VariableDeclarationPartNode localVariables,
    PrimitiveTypeEnum returnType,
    CompoundStatementNode compoundStatement
  ) {
    super(id, identifier, parameters, localVariables, compoundStatement);
    this.returnType = returnType;
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"function: (%s) %s\"];\n".formatted(getDotNotationIdentifier(), returnType, identifier));
    
    sb.append(parameters.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + parameters.getDotNotationIdentifier() + " [label=\"parameters\"];\n");

    sb.append(localVariables.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + localVariables.getDotNotationIdentifier() + " [label=\"local variables\"];\n");

    sb.append(compoundStatement.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + compoundStatement.getDotNotationIdentifier() + " [label=\"body\"];\n");

    return sb.toString();
  }
}
