package ast.types.statements.implementations;

import java.util.List;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

/**
 * Representa um nó de instrução de chamada de procedimento na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador do procedimento e os argumentos passados para ele.
 */
public class ProcedureCallStatementNode extends StatementNode {
  /**
   * Identificador do procedimento que está sendo chamado.
   */
  public final String procedureIdentifier;
  /**
   * Lista de expressões que representam os argumentos passados para o procedimento.
   */
  public final List<ExpressionNode> arguments;

  /**
   * Construtor para criar um nó de instrução de chamada de procedimento.
   *
   * @param id Identificador único para o nó.
   * @param procedureIdentifier Identificador do procedimento que está sendo chamado.
   * @param arguments Lista de expressões que representam os argumentos passados para o procedimento.
   */
  public ProcedureCallStatementNode(int id, String procedureIdentifier, List<ExpressionNode> arguments) {
    super(id);
    this.procedureIdentifier = procedureIdentifier;
    this.arguments = arguments;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("%s [label=\"%s()\"];\n".formatted(getDotNotationIdentifier(), procedureIdentifier));

    for (var argument : arguments) {
      sb.append(argument.toDotNotation());
      sb.append("%s -> %s [label=\"argument\"];\n".formatted(getDotNotationIdentifier(), argument.getDotNotationIdentifier()));
    }

    return sb.toString();
  }
}
