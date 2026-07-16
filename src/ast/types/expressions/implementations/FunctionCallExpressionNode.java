package ast.types.expressions.implementations;

import java.util.List;

import ast.types.expressions.contracts.ExpressionNode;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

/**
 * Representa um nó de expressão de chamada de função na árvore de sintaxe abstrata (AST).
 * Este nó contém informações sobre o identificador da função, os argumentos passados para ela e o tipo de retorno.
 */
public class FunctionCallExpressionNode extends ExpressionNode {
  /**
   * Identificador da função que está sendo chamada.
   */
  public final String functionIdentifier;
  /**
   * Lista de expressões que representam os argumentos passados para a função.
   */
  public final List<ExpressionNode> arguments;

  /**
   * Construtor para criar um nó de expressão de chamada de função.
   *
   * @param id Identificador único para o nó.
   * @param procedureIdentifier Identificador da função que está sendo chamada.
   * @param arguments Lista de expressões que representam os argumentos passados para a função.
   * @param returnType Tipo de retorno da função.
   */
  public FunctionCallExpressionNode(int id, String procedureIdentifier, List<ExpressionNode> arguments, PrimitiveTypeEnum returnType) {
    super(id, new PrimitiveVariableType(returnType));
    this.functionIdentifier = procedureIdentifier;
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
    
    sb.append("%s [label=\"(%s) %s()\"];\n".formatted(getDotNotationIdentifier(), type.basePrimitiveType, functionIdentifier));

    for(var argument : arguments) {
      sb.append(argument.toDotNotation());
      sb.append("%s -> %s [label=\"argument\"];\n".formatted(getDotNotationIdentifier(), argument.getDotNotationIdentifier()));
    }
    
    return sb.toString();
  }
}
