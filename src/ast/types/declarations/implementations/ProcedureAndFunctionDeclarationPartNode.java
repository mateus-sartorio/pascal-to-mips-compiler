package ast.types.declarations.implementations;

import java.util.List;

import ast.types.AstNode;
import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;

/**
 * Representa um nó de parte de declaração de procedimentos e funções na árvore de sintaxe abstrata (AST).
 * Este nó contém uma lista de declarações de procedimentos e funções.
 */
public class ProcedureAndFunctionDeclarationPartNode extends AstNode {
  /**
   * Lista de declarações de procedimentos e funções.
   */
  public final List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarations;

  /**
   * Construtor para criar um nó de parte de declaração de procedimentos e funções.
   *
   * @param id Identificador único para o nó.
   * @param procedureOrFunctionDeclarations Lista de declarações de procedimentos e funções.
   */
  public ProcedureAndFunctionDeclarationPartNode(int id, List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarations) {
    super(id);
    this.procedureOrFunctionDeclarations = procedureOrFunctionDeclarations;
  }

  /**
   * Retorna o identificador do nó no formato de notação DOT.
   *
   * @return Identificador do nó no formato de notação DOT.
   */
  @Override
  public String getDotNotationIdentifier() {
    return "\"ProcedureAndFunctionDeclarationPartNode\"";
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("%s [label=\"procedures and functions\"];\n".formatted(getDotNotationIdentifier()));
    
    for (ProcedureOrFunctionDeclarationNode procedureOrFunction : procedureOrFunctionDeclarations) {
      sb.append(procedureOrFunction.toDotNotation());
      sb.append("%s -> %s;\n".formatted(getDotNotationIdentifier(), procedureOrFunction.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
