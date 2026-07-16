package ast.types.declarations.contracts;

import java.util.Optional;

import ast.types.declarations.implementations.VariableDeclarationPartNode;
import ast.types.statements.implementations.CompoundStatementNode;

/**
 * Representa um nó de declaração de procedimento ou função na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador, parâmetros, variáveis locais e a instrução composta.
 */
public abstract class ProcedureOrFunctionDeclarationNode extends DeclarationNode {
  /**
   * Identificador do procedimento ou função.
   */
  public final String identifier;
  /**
   * Parte de declaração de parâmetros (opcional).
   */
  public final Optional<VariableDeclarationPartNode> parameters;
  /**
   * Parte de declaração de variáveis locais (opcional).
   */
  public final Optional<VariableDeclarationPartNode> localVariables;
  /**
   * Instrução composta que representa o corpo do procedimento ou função.
   */
  public final CompoundStatementNode compoundStatement;

  /**
   * Construtor para criar um nó de declaração de procedimento ou função.
   *
   * @param id Identificador único para o nó.
   * @param identifier Identificador do procedimento ou função.
   * @param parameters Parte de declaração de parâmetros (opcional).
   * @param localVariables Parte de declaração de variáveis locais (opcional).
   * @param compoundStatement Instrução composta que representa o corpo do procedimento ou função.
   */
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
