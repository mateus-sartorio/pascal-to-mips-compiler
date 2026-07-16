package ast.types.declarations.implementations;

import java.util.Optional;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.statements.implementations.CompoundStatementNode;

/**
 * Representa um nó de declaração de procedimento na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador do procedimento, parâmetros, variáveis locais
 * e a instrução composta que representa o corpo do procedimento.
 */
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

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
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
