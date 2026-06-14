package ast.types.declarations.implementations;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.statements.implementations.CompoundStatementNode;

public class ProcedureDeclarationNode extends ProcedureOrFunctionDeclarationNode {
  public ProcedureDeclarationNode(
    String identifier,
    VariableDeclarationPartNode parameters,
    VariableDeclarationPartNode localVariables,
    CompoundStatementNode compoundStatement
  ) {
    super(identifier, parameters, localVariables, compoundStatement);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"procedure: '%s'\"];\n".formatted(getDotNotationIdentifier(), identifier));
    
    sb.append(parameters.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + parameters.getDotNotationIdentifier() + " [label=\"parameters\"];\n");

    sb.append(localVariables.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + localVariables.getDotNotationIdentifier() + " [label=\"local variables\"];\n");

    sb.append(compoundStatement.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + compoundStatement.getDotNotationIdentifier() + " [label=\"body\"];\n");

    return sb.toString();
  }
}
