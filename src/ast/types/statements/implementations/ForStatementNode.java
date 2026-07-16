package ast.types.statements.implementations;

import ast.types.declarations.implementations.VariableDeclarationNode;
import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

/**
 * Representa um nó de instrução de loop "for" na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre a variável de controle, os valores inicial e final,
 * a direção do loop (incremento ou decremento) e o corpo do loop.
 */
public class ForStatementNode extends StatementNode {
  /**
   * Variável de controle do loop "for".
   */
  public final VariableDeclarationNode controlVariable;
  /**
   * Valor inicial da variável de controle.
   */
  public final ExpressionNode initialValue;
  /**
   * Valor final da variável de controle.
   */
  public final ExpressionNode finalValue;
  /**
   * Indica se o loop é decremental (downto) ou incremental (to).
   */
  public final boolean isDownto;
  /**
   * Corpo do loop "for", que é a instrução a ser executada em cada iteração.
   */
  public final StatementNode body;

  /**
   * Construtor para criar um nó de instrução de loop "for".
   *
   * @param id Identificador único para o nó.
   * @param controlVariable Variável de controle do loop "for".
   * @param initialValue Valor inicial da variável de controle.
   * @param finalValue Valor final da variável de controle.
   * @param isDownto Indica se o loop é decremental (downto) ou incremental (to).
   * @param body Corpo do loop "for", que é a instrução a ser executada em cada iteração.
   */
  public ForStatementNode(
    int id,
    VariableDeclarationNode controlVariable,
    ExpressionNode initialValue,
    ExpressionNode finalValue,
    boolean isDownto,
    StatementNode body
  ) {
    super(id);
    this.controlVariable = controlVariable;
    this.initialValue = initialValue;
    this.finalValue = finalValue;
    this.isDownto = isDownto;
    this.body = body;
  }

  /**
   * Retorna a representação do nó em notação DOT.
   *
   * @return Representação do nó em notação DOT.
   */
  @Override
  public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    String direction = isDownto ? "downto" : "to";
    sb.append("%s [label=\"for (%s)\"];\n".formatted(getDotNotationIdentifier(), direction));

    sb.append("%s [label=\"(%s) %s\"];\n".formatted(controlVariable.getDotNotationIdentifier(), controlVariable.type.toString(), controlVariable.identifier));
    sb.append("%s -> %s [label=\"controlVariable\"];\n".formatted(getDotNotationIdentifier(), controlVariable.getDotNotationIdentifier()));

    sb.append(initialValue.toDotNotation());
    sb.append("%s -> %s [label=\"initialValue\"];\n".formatted(getDotNotationIdentifier(), initialValue.getDotNotationIdentifier()));

    sb.append(finalValue.toDotNotation());
    sb.append("%s -> %s [label=\"finalValue\"];\n".formatted(getDotNotationIdentifier(), finalValue.getDotNotationIdentifier()));

    sb.append(body.toDotNotation());
    sb.append("%s -> %s [label=\"body\"];\n".formatted(getDotNotationIdentifier(), body.getDotNotationIdentifier()));

    return sb.toString();
  }
}
