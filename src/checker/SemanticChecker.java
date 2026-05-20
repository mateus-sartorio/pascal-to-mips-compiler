package checker;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser.ConstantContext;
import parser.PascalParser.For_statementContext;
import parser.PascalParser.Formal_parameter_listContext;
import parser.PascalParser.Function_headingContext;
import parser.PascalParser.Identifier_listContext;
import parser.PascalParser.Indexed_variableContext;
import parser.PascalParser.Procedure_headingContext;
import parser.PascalParser.Unsigned_constantContext;
import parser.PascalParser.Value_parameter_speficiationContext;
import parser.PascalParser.Variable_accessContext;
import parser.PascalParser.Variable_declarationContext;
import parser.PascalParserBaseVisitor;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.ProceduresAndFunctionsTable;
import types.PrimitiveType;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {  
  // Tabela de literais para armazenar as strings literais encontradas no código
  private final StringLiteralsTable stringLiteralsTable = new StringLiteralsTable();

  // Tabela de símbolos para armazenar as variáveis declaradas no código
  private final VariablesTable variablesTable = new VariablesTable();

  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable = new ProceduresAndFunctionsTable();

  public void printLiteralsTable() {
    System.out.println(stringLiteralsTable);
  }

  public void printSymbolsTable() {
    System.out.println(variablesTable);
  }

  // Método auxiliar para verificar se uma variável foi declarada antes de ser usada
  private void checkVariable(Token token) {
    String varID = token.getText();

    if (variablesTable.lookupVariable(varID) == -1) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", token.getLine(), varID);
      return;
    }
  }

  // Auxiliar reutilizável para cadastrar variáveis na tabela e checar duplicados
  private void registerGlobalVariables(Identifier_listContext ctx, String lexerTokenType) {
    for (TerminalNode identifierNode : ctx.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();
      String variableId = token.getText();

      var i = variablesTable.lookupVariable(variableId);

      // Validação de duplicidade de declaração de variável
      if (i != -1) {
        System.err.printf("SEMANTIC ERROR (%d): Variable '%s' already declared at line %d.\n", token.getLine(), variableId, variablesTable.getLine(i));
        System.exit(1);
      }

      var varLine = token.getLine();
      var varType = new VariablesTable.Type(PrimitiveType.getVarType(lexerTokenType));

      variablesTable.addVariable(variableId, varLine, varType);
    }
  }

  private void registerProcedureOrFunctionParameters(Formal_parameter_listContext ctx, String functionOrProcedureName) {
    ctx
  }

  private void registerProceduresAndFunctions(String procedureOrFunctionName, Formal_parameter_listContext ctx) {
    int i = proceduresAndFunctionsTable.lookupVariable(procedureOrFunctionName);
    
    if(i != -1) {
      // TODO: see what to do here
      return;
    }

    // proceduresAndFunctionsTable.addVariable(procedureOrFunctionName, i, null, null);
  }




  // ------------------ VISITORS DE DECLARAÇÃO DE VARIÁVEIS GLOBAIS -----------------------

  @Override
  public Void visitVariable_declaration(Variable_declarationContext ctx) {
    registerGlobalVariables(ctx.identifier_list(), ctx.type_denoter().getText());
    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------



  // ------------------ VISITORS DE DECLARAÇÃO DE VARIÁVEIS DE PROCEDURES -----------------------

  @Override
  public Void visitProcedure_heading(Procedure_headingContext ctx) {
    var identifier = ctx.IDENTIFIER();
    var identifierName = identifier.getText();
    var identifierLine = identifier.getSymbol().getLine();
    
    int index = proceduresAndFunctionsTable.lookProcedureOrFunction(identifierName);
    
    if(index != -1) {
      // Da erro
    }

    proceduresAndFunctionsTable.addProcedure(identifierName, identifierLine);

    return visitChildren(ctx);
  }

  @Override
  public Void visitValue_parameter_specification(Value_parameter_speficiationContext ctx) {
    var declaration = ctx.parent.parent;

    if(declaration instanceof Function_headingContext functionHeadingContext) {
      var identifier = functionHeadingContext.IDENTIFIER().getText();
      proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(identifier, )
    }
    else if(declaration instanceof Procedure_headingContext procedureHeadingContext) {
      var identifier = procedureHeadingContext.IDENTIFIER();
    }
  }

  // --------------------------------------------------------------------------------------



  // @Override
  // public Void visitValue_parameter_speficiation(Value_parameter_speficiationContext ctx) {
  //   if (ctx.type_denoter() != null) {
  //     registerVariables(ctx.identifier_list(), ctx.type_denoter().getText());
  //   }

  //   return visitChildren(ctx);
  // }

  // @Override
  // public Void visitVariable_parameter_specification(Variable_parameter_specificationContext ctx) {
  //   if (ctx.type_denoter() != null) {
  //     registerVariables(ctx.identifier_list(), ctx.type_denoter().getText());
  //   }

  //   return visitChildren(ctx);
  // }


  // ------------------- CHECAGEM DE USO DE VARIÁVEIS ------------------

  @Override
  public Void visitVariable_access(Variable_accessContext ctx) {
    if (ctx.IDENTIFIER() != null) {
      checkVariable(ctx.IDENTIFIER().getSymbol());
    }
    return visitChildren(ctx);
  }

  // @Override
  // public Void visitIndexed_variable(Indexed_variableContext ctx) {
  //   if (ctx.IDENTIFIER() != null) {
  //     checkVariable(ctx.IDENTIFIER().getSymbol());
  //   }
  //   return visitChildren(ctx);
  // }

  // @Override
  // public Void visitFor_statement(For_statementContext ctx) {
  //   if (ctx.IDENTIFIER() != null) {
  //     checkVariable(ctx.IDENTIFIER().getSymbol());
  //   }
  //   return visitChildren(ctx);
  // }


  // ------------------- CHECAGEM DE USO DE LITERAIS ------------------

  @Override
  public Void visitConstant(ConstantContext ctx) {
    // Como unsigned_constant aceita números, precisamos isolar apenas a String
    if (ctx.CHARACTER_STRING() != null) {
      String strVal = ctx.CHARACTER_STRING().getText();
      // Remove as aspas simples de início e fim ('texto' -> texto)
      stringLiteralsTable.add(strVal.substring(1, strVal.length() - 1));
    }

    return null;
  }

  @Override
  public Void visitUnsigned_constant(Unsigned_constantContext ctx) {
    // Como unsigned_constant aceita números, precisamos isolar apenas a String
    if (ctx.CHARACTER_STRING() != null) {
      String strVal = ctx.CHARACTER_STRING().getText();
      // Remove as aspas simples de início e fim ('texto' -> texto)
      stringLiteralsTable.add(strVal.substring(1, strVal.length() - 1));
    }
    
    return null;
  }
}
