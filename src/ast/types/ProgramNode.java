package ast.types;

import java.util.Optional;

import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.declarations.implementations.ProcedureAndFunctionDeclarationPartNode;
import ast.types.declarations.implementations.VariableDeclarationPartNode;
import ast.types.statements.implementations.CompoundStatementNode;

/**
 * Representa um nó de programa na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador do programa, partes de declaração de variáveis,
 * declarações de procedimentos e funções, e a instrução composta principal.
 */
public class ProgramNode extends AstNode {
  /**
   * Identificador do programa.
   */
  public final String programIdentifier;
  /**
   * Parte de declaração de variáveis (opcional).
   */
  public final Optional<VariableDeclarationPartNode> variableDeclarationPart;
  /**
   * Parte de declarações de procedimentos e funções (opcional).
   */
  public final Optional<ProcedureAndFunctionDeclarationPartNode> proceduresAndFunctions;
  /**
   * Instrução composta principal do programa.
   */
  public final CompoundStatementNode compoundStatement;

  /**
   * Construtor para criar um nó de programa.
   *
   * @param id Identificador único para o nó.
   * @param programIdentifier Identificador do programa.
   * @param variableDeclarationPart Parte de declaração de variáveis (opcional).
   * @param proceduresAndFunctions Parte de declarações de procedimentos e funções (opcional).
   * @param compoundStatement Instrução composta principal do programa.
   */
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

  /**
   * Retorna o identificador do nó no formato de notação DOT.
   *
   * @return Identificador do nó no formato de notação DOT.
   */
  @Override
  public String getDotNotationIdentifier() {
    return "\"ProgramNode\"";
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
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

  /**
   * Retorna a declaração de procedimento ou função correspondente ao identificador fornecido.
   *
   * @param identifier Identificador do procedimento ou função.
   * @return Nó de declaração de procedimento ou função correspondente, ou null se não encontrado.
   */
  public ProcedureOrFunctionDeclarationNode getDeclaration(String identifier) {
    for(var p : proceduresAndFunctions.get().procedureOrFunctionDeclarations) {
      if(p.identifier.equals(identifier)) {
        return p;
      }
    }

    return null;
  }
}
