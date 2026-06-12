package ast.types;

import java.util.List;

public class ProcedureCallStatementNode extends StatementNode {
  String procedureIdentifier;
  List<ExpressionNode> arguments;

  public ProcedureCallStatementNode(String procedureIdentifier, List<ExpressionNode> arguments) {
    this.procedureIdentifier = procedureIdentifier;
    this.arguments = arguments;
  }
  
}
