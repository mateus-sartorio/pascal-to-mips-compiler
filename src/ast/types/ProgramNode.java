package ast.types;

import java.util.Optional;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.declarations.implementations.ProcedureAndFunctionDeclarationPartNode;
import ast.types.declarations.implementations.VariableDeclarationPartNode;
import ast.types.statements.implementations.CompoundStatementNode;

public class ProgramNode extends AstNode {
  public final String programIdentifier;
  public final Optional<VariableDeclarationPartNode> variableDeclarationPart;
  public final Optional<ProcedureAndFunctionDeclarationPartNode> proceduresAndFunctions;
  public final CompoundStatementNode compoundStatement;

  public ProgramNode(
    int id,
    String programIdentifier,
    Optional<VariableDeclarationPartNode> variableDeclarationPart,
    Optional<ProcedureAndFunctionDeclarationPartNode> proceduresAndFunctions,
    CompoundStatementNode compoundStatement
  ) {
    super(id);
    this.programIdentifier = programIdentifier;
    this.variableDeclarationPart = variableDeclarationPart;
    this.proceduresAndFunctions = proceduresAndFunctions;
    this.compoundStatement = compoundStatement;
  }

  @Override
  public String getDotNotationIdentifier() {
    return "\"ProgramNode\"";
  }

  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"%s\"];\n".formatted(getDotNotationIdentifier(), programIdentifier));
    
    variableDeclarationPart.ifPresent(variableDeclarationPart -> {
      sb.append(variableDeclarationPart.toDotNotation());
      sb.append("%s -> %s [label=\"global variables\"];\n".formatted(getDotNotationIdentifier(), variableDeclarationPart.getDotNotationIdentifier()));
    });
    
    proceduresAndFunctions.ifPresent(proceduresAndFunctions -> {
      sb.append(proceduresAndFunctions.toDotNotation());
      sb.append("%s -> %s [label=\"procedure and function declarations\"] ;\n".formatted(getDotNotationIdentifier(), proceduresAndFunctions.getDotNotationIdentifier()));
    });

    sb.append(compoundStatement.toDotNotation());
    sb.append("%s -> %s [label=\"main body\"];\n".formatted(getDotNotationIdentifier(), compoundStatement.getDotNotationIdentifier()));

    return sb.toString();
  }

  public ProcedureOrFunctionDeclarationNode getDeclaration(String identifier) {
    for(var p : proceduresAndFunctions.get().procedureOrFunctionDeclarations) {
      if(p.identifier.equals(identifier)) {
        return p;
      }
    }

    return null;
  }
}
