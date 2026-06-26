package ast.types.declarations.implementations;

import java.util.Optional;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.statements.implementations.CompoundStatementNode;

public class ProcedureDeclarationNode extends ProcedureOrFunctionDeclarationNode {
  public ProcedureDeclarationNode(
    int id,
    String identifier,
    Optional<VariableDeclarationPartNode> parameters,
    Optional<VariableDeclarationPartNode> localVariables,
    CompoundStatementNode compoundStatement
  ) {
    super(id, identifier, parameters, localVariables, compoundStatement);
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"procedure: '%s'\"];\n".formatted(getDotNotationIdentifier(), identifier));
    
    parameters.ifPresent(param -> {
      sb.append(param.toDotNotation());
      sb.append(getDotNotationIdentifier() + " -> " + param.getDotNotationIdentifier() + " [label=\"parameters\"];\n");
    });

    localVariables.ifPresent(localVar -> {
      sb.append(localVar.toDotNotation());
      sb.append(getDotNotationIdentifier() + " -> " + localVar.getDotNotationIdentifier() + " [label=\"local variables\"];\n");
    });

    sb.append(compoundStatement.toDotNotation());
    sb.append(getDotNotationIdentifier() + " -> " + compoundStatement.getDotNotationIdentifier() + " [label=\"body\"];\n");

    return sb.toString();
  }
}
