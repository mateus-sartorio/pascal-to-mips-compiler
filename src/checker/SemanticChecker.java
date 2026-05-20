package checker;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser.For_statementContext;
import parser.PascalParser.Function_declarationContext;
import parser.PascalParser.Function_designatorContext;
import parser.PascalParser.Function_headingContext;
import parser.PascalParser.Identifier_listContext;
import parser.PascalParser.Procedure_declarationContext;
import parser.PascalParser.Procedure_headingContext;
import parser.PascalParser.Procedure_statementContext;
import parser.PascalParser.ProgramContext;
import parser.PascalParser.Program_headingContext;
import parser.PascalParser.Unsigned_constantContext;
import parser.PascalParser.Value_parameter_speficiationContext;
import parser.PascalParser.Variable_accessContext;
import parser.PascalParser.Variable_declarationContext;
import parser.PascalParserBaseVisitor;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.VariablesTable.VariableType;
import tables.ProceduresAndFunctionsTable;
import types.PrimitiveType;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {
  String programHeading = null;

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

  // Método auxiliar para verificar se uma variável global foi declarada antes de ser usada
  private void checkGlobalVariable(Token token) {
    String varID = token.getText();

    if (variablesTable.lookupVariable(varID) == -1) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", token.getLine(), varID);
      return;
    }
  }

  private void checkProcedureOrFunctionIdentifier(Token token) {
    String varID = token.getText();

    if (proceduresAndFunctionsTable.lookupProcedureOrFunction(varID) == -1) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", token.getLine(), varID);
      return;
    }
  }

  // Método auxiliar para verificar se uma variável local de function ou procedure foi declarada antes de ser usada
  private void checkProcedureOrFunctionVariable(Token token, String functionOrVariableIdentifier) {
    String variableIdentifier = token.getText();
    int line = token.getLine();

    if (variablesTable.lookupVariable(variableIdentifier) == -1) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", line, variableIdentifier);
      return;
    }

    if (variablesTable.lookupVariable(variableIdentifier) == -1) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", line, variableIdentifier);
      return;
    }
  }

  // Auxiliar reutilizável para cadastrar variáveis na tabela e checar duplicados
  private void registerGlobalVariables(Identifier_listContext ctx, VariableType variableType) {
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

      variablesTable.addVariable(variableId, varLine, variableType);
    }
  }

    private void registerFunctionOrProcedureLocalVariables(
      Identifier_listContext ctx,
      String functionOrProcedureIdentifier,
      VariableType variableType
    ) {
    for (TerminalNode identifierNode : ctx.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();
      String variableIdentifier = token.getText();

      var i = variablesTable.lookupVariable(variableIdentifier);

      // Validação de duplicidade de declaração de variável
      if (i != -1) {
        System.err.printf("SEMANTIC ERROR (%d): Variable '%s' already declared at line %d.\n", token.getLine(), variableIdentifier, variablesTable.getLine(i));
        System.exit(1);
      }

      var varLine = token.getLine();
      proceduresAndFunctionsTable.addProcedlureOrFunctionVariable(
        functionOrProcedureIdentifier,
        variableIdentifier,
        varLine,
        variableType
      );
    }
  }


  // ------------------ PROGRAM HEADING -----------------------

  @Override
  public Void visitProgram_heading(Program_headingContext ctx) {
    programHeading = ctx.IDENTIFIER().getText();
    return null;
  }

  // --------------------------------------------------------------------------------------






  // ------------------ VISITORS DE DECLARAÇÃO DE VARIÁVEIS GLOBAIS -----------------------

  @Override
  public Void visitVariable_declaration(Variable_declarationContext ctx) {
    var parent = ctx.parent.parent;

    VariableType type;
    var typeDenoter = ctx.type_denoter();
    if(typeDenoter.primitive_type() != null) {
      var pT = typeDenoter.primitive_type();
      type = new VariableType(PrimitiveType.getVarType(pT.getText()));
    }
    else {
      var aT = typeDenoter.array_type();
      var primitiveType = PrimitiveType.getVarType(aT.primitive_type().getText());
      var subrangeType = aT.subrange_type();
      var c1 = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      var c2 = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());
      type = new VariableType(false, primitiveType, c1, c2);
    }

    if(parent instanceof Procedure_headingContext procedureHeadingContext) {
      var identifier = procedureHeadingContext.IDENTIFIER().getText();
      registerFunctionOrProcedureLocalVariables(ctx.identifier_list(), identifier, type);
    }
    else if(parent instanceof Function_headingContext functionHeadingContext) {
      var identifier = functionHeadingContext.IDENTIFIER().getText();
      registerFunctionOrProcedureLocalVariables(ctx.identifier_list(), identifier, type);

    }
    else {
      registerGlobalVariables(ctx.identifier_list(), type);
    }

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------



  // ------------------ VISITORS DE DECLARAÇÃO DE PROCEDURES E FUNCTIONS -----------------------

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

  // --------------------------------------------------------------------------------------





  // ------------------ VISITORS DE DECLARAÇÃO DE PARÂMETROS DE PROCEDURES E FUNCTIONS -----------------------

  @Override
  public Void visitValue_parameter_speficiation(Value_parameter_speficiationContext ctx) {
    var declaration = ctx.parent.parent;

    var line = ctx.start.getLine();
    var typeDenoter = ctx.type_denoter();

    VariableType type;
    if(typeDenoter.primitive_type() != null) {
      var pT = typeDenoter.primitive_type();
      type = new VariableType(PrimitiveType.getVarType(pT.getText()));
    }
    else {
      var aT = typeDenoter.array_type();
      var primitiveType = PrimitiveType.getVarType(aT.primitive_type().getText());
      var subrangeType = aT.subrange_type();
      var c1 = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      var c2 = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());
      type = new VariableType(false, primitiveType, c1, c2);
    }

    if(declaration instanceof Function_headingContext functionHeadingContext) {
      var identifier = functionHeadingContext.IDENTIFIER().getText();
      var identifierList = ctx.identifier_list();

      for(var a : identifierList.IDENTIFIER()) {
        proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(identifier, a.getText(), line, type);
      }
    }
    else if(declaration instanceof Procedure_headingContext procedureHeadingContext) {
      var identifier = procedureHeadingContext.IDENTIFIER().getText();
      var identifierList = ctx.identifier_list();

      for(var a : identifierList.IDENTIFIER()) {
        proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(identifier, a.getText(), line, type);
      }
    }

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------






  // ------------------- CHECAGEM DE USO DE VARIÁVEIS LOCAIS E GLOBAIS ------------------

  @Override
  public Void visitVariable_access(Variable_accessContext ctx) {
    TerminalNode identifier;
    if (ctx.IDENTIFIER() != null) {
      identifier = ctx.IDENTIFIER();
    }
    else {
      var indexedVariable = ctx.indexed_variable();
      identifier = indexedVariable.IDENTIFIER();
    }
    
    checkGlobalVariable(identifier.getSymbol());

    var parent = ctx.parent;
    while(!(parent instanceof ProgramContext)) {
      if(parent instanceof Function_declarationContext functionDeclarationContext) {
        var functionHeading = functionDeclarationContext.function_heading();
        var functionIdentifier = functionHeading.IDENTIFIER().getText();
        checkProcedureOrFunctionVariable(identifier.getSymbol(), functionIdentifier);
        break;
      }
      else if(parent instanceof Procedure_declarationContext procedureDeclarationContext) {
        var procedureHeading = procedureDeclarationContext.procedure_heading();
        var procedureIdentifier = procedureHeading.IDENTIFIER().getText();
        checkProcedureOrFunctionVariable(identifier.getSymbol(), procedureIdentifier);
        break;
      }

      parent = ctx.parent;
    }

    return visitChildren(ctx);
  }

  @Override
  public Void visitFor_statement(For_statementContext ctx) {
    var identifier = ctx.IDENTIFIER();
    checkGlobalVariable(identifier.getSymbol());

    var parent = ctx.parent;
    while(!(parent instanceof ProgramContext)) {
      if(parent instanceof Function_declarationContext functionDeclarationContext) {
        var functionHeading = functionDeclarationContext.function_heading();
        var functionIdentifier = functionHeading.IDENTIFIER().getText();
        checkProcedureOrFunctionVariable(identifier.getSymbol(), functionIdentifier);
        break;
      }
      else if(parent instanceof Procedure_declarationContext procedureDeclarationContext) {
        var procedureHeading = procedureDeclarationContext.procedure_heading();
        var procedureIdentifier = procedureHeading.IDENTIFIER().getText();
        checkProcedureOrFunctionVariable(identifier.getSymbol(), procedureIdentifier);
        break;
      }

      parent = ctx.parent;
    }

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------


  // ------------------- CHECAGEM DE USO DE PROCEDURES E FUNCTIONS ------------------

  @Override
  public Void visitProcedure_statement(Procedure_statementContext ctx) {
    var identifier = ctx.IDENTIFIER();
    checkProcedureOrFunctionIdentifier(identifier.getSymbol());
    return visitChildren(ctx);
  }

  @Override
  public Void visitFunction_designator(Function_designatorContext ctx) {
    var identifier = ctx.IDENTIFIER();
    checkProcedureOrFunctionIdentifier(identifier.getSymbol());
    return visitChildren(ctx);
  }


  // --------------------------------------------------------------------------------------


  // ------------------- CHECAGEM DE USO DE LITERAIS ------------------

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

  // --------------------------------------------------------------------------------------
}
