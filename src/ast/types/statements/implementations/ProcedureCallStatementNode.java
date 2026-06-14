package ast.types.statements.implementations;

import java.util.List;

import ast.types.expressions.contracts.ExpressionNode;
import ast.types.statements.contract.StatementNode;

public class ProcedureCallStatementNode extends StatementNode {
  String procedureIdentifier;
  List<ExpressionNode> arguments;

  public ProcedureCallStatementNode(String procedureIdentifier, List<ExpressionNode> arguments) {
    this.procedureIdentifier = procedureIdentifier;
    this.arguments = arguments;
  }

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
