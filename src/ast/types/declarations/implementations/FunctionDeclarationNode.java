package ast.types.declarations.implementations;

import java.util.Optional;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.statements.implementations.CompoundStatementNode;
import types.PrimitiveTypeEnum;

/**
 * Representa um nó de declaração de função na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador da função, parâmetros, variáveis locais,
 * tipo de retorno e a instrução composta que representa o corpo da função.
 */
public class FunctionDeclarationNode extends ProcedureOrFunctionDeclarationNode {
  /**
   * Tipo de retorno da função.
   */
  public final PrimitiveTypeEnum returnType;

  /**
   * Construtor para criar um nó de declaração de função.
   *
   * @param id Identificador único para o nó.
   * @param identifier Identificador da função.
   * @param parameters Parte de declaração de parâmetros (opcional).
   * @param localVariables Parte de declaração de variáveis locais (opcional).
   * @param returnType Tipo de retorno da função.
   * @param compoundStatement Instrução composta que representa o corpo da função.
   */
  public FunctionDeclarationNode(
    int id,
    String identifier,
    Optional<VariableDeclarationPartNode> parameters,
    Optional<VariableDeclarationPartNode> localVariables,
    PrimitiveTypeEnum returnType,
    CompoundStatementNode compoundStatement
  ) {
    super(id, identifier, parameters, localVariables, compoundStatement);
    this.returnType = returnType;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"function: (%s) %s\"];\n".formatted(getDotNotationIdentifier(), returnType, identifier));
    
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
