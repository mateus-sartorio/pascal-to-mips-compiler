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
import tables.VariablesTable.VariableTableEntry;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import types.PrimitiveType;
import types.ProcedureOrFunctionEnum;
import types.VariableType;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {
  String programHeadingIdentifier = null;

  // Tabela de literais para armazenar as strings literais encontradas no código
  private final StringLiteralsTable stringLiteralsTable = new StringLiteralsTable();

  // Tabela de símbolos para armazenar as variáveis declaradas no código
  private final VariablesTable variablesTable = new VariablesTable();

  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable = new ProceduresAndFunctionsTable();

  public SemanticChecker() {
    registerPreDeclaredProceduresAndFunctions();
  }

  public void printLiteralsTable() {
    System.out.println(stringLiteralsTable);
  }

  public void printGlobalVariablesTable() {
    System.out.println(variablesTable);
  }

  public void printProceduresAndFunctionsTable() {
    System.out.println(proceduresAndFunctionsTable);
  }

  public void checkProgramHeadingIdentifier(Token token) {
    String variableIdentifier = token.getText();

    if (variableIdentifier.equals(programHeadingIdentifier)) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", token.getLine(), variableIdentifier);
      return;
    }
  }

  // Método auxiliar para verificar se uma variável global foi declarada antes de
  // ser usada
  // private boolean checkGlobalVariable(Token token) {
  // String variableIdentifier = token.getText();

  // if (!variablesTable.lookupVariable(variableIdentifier)) {
  // System.out.printf(
  // "SEMANTIC ERROR (%d): Variable '%s' was not declared.",
  // token.getLine(),
  // variableIdentifier
  // );

  // System.exit(1);
  // }
  // }

  private void checkProcedureOrFunctionIdentifier(Token token, ProcedureOrFunctionEnum type) {
    String identifier = token.getText();
    int line = token.getLine();

    if (proceduresAndFunctionsTable.lookProcedureOrFunction(identifier)) {
      System.out.printf(
          "SEMANTIC ERROR (%d): %s '%s' was already declared at line %d.\n",
          token.getLine(),
          type == ProcedureOrFunctionEnum.FUNCTION ? "FUNCTION" : "PROCEDURE",
          identifier,
          line);

      System.exit(1);
    }

  }

  // Auxiliar reutilizável para cadastrar variáveis na tabela e checar duplicados
  private void registerGlobalVariables(Identifier_listContext ctx, VariableType variableType) {
    for (TerminalNode identifierNode : ctx.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();
      String variableIdentifier = token.getText();

      VariableTableEntry entry = variablesTable.get(variableIdentifier);

      // Validação de duplicidade de declaração de variável
      if (entry != null) {
        System.out.printf(
            "SEMANTIC ERROR (%d): Global variable '%s' already declared at line %d.\n",
            token.getLine(),
            variableIdentifier,
            entry.line);

        System.exit(1);
      }

      var varLine = token.getLine();
      variablesTable.addVariable(variableIdentifier, varLine, variableType);
    }
  }

  private void registerFunctionOrProcedureLocalVariables(
      Identifier_listContext ctx,
      String functionOrProcedureIdentifier,
      VariableType variableType) {
    for (TerminalNode identifierNode : ctx.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();
      String variableIdentifier = token.getText();

      ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable
          .get(functionOrProcedureIdentifier);

      assert procedureOrFunctionEntry != null;

      VariableTableEntry variableEntry = procedureOrFunctionEntry.localVariables.get(variableIdentifier);
      VariableTableEntry parameterEntry = procedureOrFunctionEntry.parameters.get(variableIdentifier);

      if (parameterEntry != null) {
        System.out.printf(
            "SEMANTIC ERROR (%d): Local variable '%s' of function '%s' was already declared at line %d.\n",
            token.getLine(),
            variableIdentifier,
            functionOrProcedureIdentifier,
            parameterEntry.line);

        System.exit(1);
      }

      // Validação de duplicidade de declaração de variável
      if (variableEntry != null) {
        System.out.printf(
            "SEMANTIC ERROR (%d): Local variable '%s' of function '%s' was already declared at line %d.\n",
            token.getLine(),
            variableIdentifier,
            functionOrProcedureIdentifier,
            variableEntry.line);

        System.exit(1);
      }

      var varLine = token.getLine();
      proceduresAndFunctionsTable.addProcedlureOrFunctionVariable(
          functionOrProcedureIdentifier,
          variableIdentifier,
          varLine,
          variableType);
    }
  }

  private void registerFunctionOrProcedureParameters(
      Identifier_listContext ctx,
      String functionOrProcedureIdentifier,
      VariableType variableType) {
    for (TerminalNode identifierNode : ctx.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();
      String variableIdentifier = token.getText();

      ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable
          .get(functionOrProcedureIdentifier);

      assert procedureOrFunctionEntry != null;

      VariableTableEntry variableEntry = procedureOrFunctionEntry.parameters.get(variableIdentifier);

      // Validação de duplicidade de declaração de variável
      if (variableEntry != null) {
        System.out.printf(
            "SEMANTIC ERROR (%d): Parameter '%s' of function '%s' was already declared at line %d.\n",
            token.getLine(),
            variableIdentifier,
            functionOrProcedureIdentifier,
            variableEntry.line);

        System.exit(1);
      }

      var varLine = token.getLine();
      proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(
          functionOrProcedureIdentifier,
          variableIdentifier,
          varLine,
          variableType);
    }
  }

  // ------------------ PROGRAM HEADING -----------------------

  @Override
  public Void visitProgram_heading(Program_headingContext ctx) {
    programHeadingIdentifier = ctx.IDENTIFIER().getText();
    return null;
  }

  // --------------------------------------------------------------------------------------

  // ------------------ VISITORS DE DECLARAÇÃO DE VARIÁVEIS GLOBAIS E LOCAIS
  // -----------------------

  @Override
  public Void visitVariable_declaration(Variable_declarationContext ctx) {
    VariableType type;
    var typeDenoter = ctx.type_denoter();

    if (typeDenoter.primitive_type() != null) {
      var primitiveType = typeDenoter.primitive_type();
      type = new VariableType(PrimitiveType.getType(primitiveType.getText()));
    } else {
      var arrayType = typeDenoter.array_type();
      var primitiveType = PrimitiveType.getType(arrayType.primitive_type().getText());
      var subrangeType = arrayType.subrange_type();
      var startIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      var endIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());
      type = new VariableType(primitiveType, startIndex, endIndex);
    }

    var parent = ctx.parent.parent;

    if (parent instanceof Procedure_headingContext) {
      Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) parent;
      var identifier = procedureHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerFunctionOrProcedureLocalVariables(ctx.identifier_list(), identifier, type);
    } else if (parent instanceof Function_headingContext) {
      Function_headingContext functionHeadingContext = (Function_headingContext) parent;
      var identifier = functionHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerFunctionOrProcedureLocalVariables(ctx.identifier_list(), identifier, type);

    } else {
      registerGlobalVariables(ctx.identifier_list(), type);
    }

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------

  // ------------------ VISITORS DE DECLARAÇÃO DE PROCEDURES E FUNCTIONS
  // -----------------------

  @Override
  public Void visitProcedure_heading(Procedure_headingContext ctx) {
    var identifier = ctx.IDENTIFIER().getSymbol();
    var procedureIdentifier = identifier.getText();
    var line = identifier.getLine();

    checkProcedureOrFunctionIdentifier(identifier, ProcedureOrFunctionEnum.PROCEDURE);
    proceduresAndFunctionsTable.addProcedure(procedureIdentifier, line);

    return visitChildren(ctx);
  }

  @Override
  public Void visitFunction_heading(Function_headingContext ctx) {
    var identifier = ctx.IDENTIFIER().getSymbol();
    var functionIdentifier = identifier.getText();
    var line = identifier.getLine();

    checkProcedureOrFunctionIdentifier(identifier, ProcedureOrFunctionEnum.FUNCTION);
    proceduresAndFunctionsTable.addProcedure(functionIdentifier, line);

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------

  // ------------------ VISITORS DE DECLARAÇÃO DE PARÂMETROS DE PROCEDURES E
  // FUNCTIONS -----------------------

  @Override
  public Void visitValue_parameter_speficiation(Value_parameter_speficiationContext ctx) {
    VariableType type;
    var typeDenoter = ctx.type_denoter();

    if (typeDenoter.primitive_type() != null) {
      var primitiveType = typeDenoter.primitive_type();
      type = new VariableType(PrimitiveType.getType(primitiveType.getText()));
    } else {
      var arrayType = typeDenoter.array_type();
      var primitiveType = PrimitiveType.getType(arrayType.primitive_type().getText());
      var subrangeType = arrayType.subrange_type();
      var startIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      var endIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());
      type = new VariableType(primitiveType, startIndex, endIndex);
    }

    var declaration = ctx.parent.parent;

    if (declaration instanceof Function_headingContext) {
      Function_headingContext functionHeadingContext = (Function_headingContext) declaration;
      var identifier = functionHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerFunctionOrProcedureParameters(ctx.identifier_list(), identifier, type);
    } else if (declaration instanceof Procedure_headingContext) {
      Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) declaration;
      var identifier = procedureHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerFunctionOrProcedureParameters(ctx.identifier_list(), identifier, type);
    }

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------

  // ------------------- CHECAGEM DE USO DE VARIÁVEIS LOCAIS E GLOBAIS
  // ------------------

  @Override
  public Void visitVariable_access(Variable_accessContext ctx) {
    TerminalNode identifier;

    if (ctx.IDENTIFIER() != null) {
      identifier = ctx.IDENTIFIER();
    } else {
      var indexedVariable = ctx.indexed_variable();
      identifier = indexedVariable.IDENTIFIER();
    }

    String variableIdentifier = identifier.getSymbol().getText();

    if (variablesTable.lookupVariable(identifier.getSymbol().getText())) {
      return visitChildren(ctx);
    }

    var parent = ctx.parent;

    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext) {
        Function_declarationContext functionDeclarationContext = (Function_declarationContext) parent;
        var functionHeading = functionDeclarationContext.function_heading();
        var functionIdentifier = functionHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(functionIdentifier, variableIdentifier)
            |
            proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(functionIdentifier, variableIdentifier) |
            variableIdentifier.equals(functionIdentifier)) {
          return visitChildren(ctx);
        }
        ;

        break;
      } else if (parent instanceof Procedure_declarationContext) {
        Procedure_declarationContext procedureDeclarationContext = (Procedure_declarationContext) parent;
        var procedureHeading = procedureDeclarationContext.procedure_heading();
        var procedureIdentifier = procedureHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(procedureIdentifier, variableIdentifier)
            |
            proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(procedureIdentifier,
                variableIdentifier)) {
          return visitChildren(ctx);
        }
        ;

        break;
      }

      parent = parent.parent;
    }

    System.out.printf(
        "SEMANTIC ERROR (%d): Variable '%s' was not declared.",
        identifier.getSymbol().getLine(),
        variableIdentifier);

    System.exit(1);

    return visitChildren(ctx);
  }

  @Override
  public Void visitFor_statement(For_statementContext ctx) {
    var identifier = ctx.IDENTIFIER();

    String variableIdentifier = identifier.getSymbol().getText();

    if (variablesTable.lookupVariable(identifier.getSymbol().getText())) {
      return visitChildren(ctx);
    }

    var parent = ctx.parent;
    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext) {
        Function_declarationContext functionDeclarationContext = (Function_declarationContext) parent;
        var functionHeading = functionDeclarationContext.function_heading();
        var functionIdentifier = functionHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(functionIdentifier, variableIdentifier) |
            proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(functionIdentifier,
                variableIdentifier)) {
          return visitChildren(ctx);
        }
        ;

        break;
      } else if (parent instanceof Procedure_declarationContext) {
        Procedure_declarationContext procedureDeclarationContext = (Procedure_declarationContext) parent;
        var procedureHeading = procedureDeclarationContext.procedure_heading();
        var procedureIdentifier = procedureHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(procedureIdentifier, variableIdentifier)
            |
            proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(procedureIdentifier,
                variableIdentifier)) {
          return visitChildren(ctx);
        }
        ;

        break;
      }

      parent = parent.parent;
    }

    System.out.printf(
        "SEMANTIC ERROR (%d): Variable '%s' was not declared.",
        identifier.getSymbol().getLine(),
        variableIdentifier);

    System.exit(1);

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------

  // ------------------- CHECAGEM DE USO DE PROCEDURES E FUNCTIONS
  // ------------------

  @Override
  public Void visitProcedure_statement(Procedure_statementContext ctx) {
    var identifier = ctx.IDENTIFIER();

    if (!proceduresAndFunctionsTable.lookProcedureOrFunction(identifier.getText())) {
      System.out.printf(
          "SEMANTIC ERROR (%d): Procedure '%s' is not defined.",
          identifier.getSymbol().getLine(),
          identifier.getText());

      System.exit(1);
    }
    ;

    return visitChildren(ctx);
  }

  @Override
  public Void visitFunction_designator(Function_designatorContext ctx) {
    var identifier = ctx.IDENTIFIER();

    if (!proceduresAndFunctionsTable.lookProcedureOrFunction(identifier.getText())) {
      System.out.printf(
          "SEMANTIC ERROR (%d): Function '%s' is not defined.",
          identifier.getSymbol().getLine(),
          identifier.getText());

      System.exit(1);
    }
    ;

    return visitChildren(ctx);
  }

  // --------------------------------------------------------------------------------------

  // ------------------- CHECAGEM DE USO DE LITERAIS ------------------

  @Override
  public Void visitUnsigned_constant(Unsigned_constantContext ctx) {
    // Como unsigned_constant aceita números, precisamos isolar apenas a String
    if (ctx.CHARACTER_STRING() != null) {
      String stringLiteral = ctx.CHARACTER_STRING().getText();
      // Remove as aspas simples de início e fim ('texto' -> texto)
      stringLiteralsTable.addStringLiteral(stringLiteral.substring(1, stringLiteral.length() - 1));
    }

    return null;
  }

  // --------------------------------------------------------------------------------------

  // ------------------- PRE DECLARED PROCEDURES AND FUNCTIONS ------------------

  private void registerPreDeclaredProceduresAndFunctions() {
    proceduresAndFunctionsTable.addProcedure("write", -1);
    proceduresAndFunctionsTable.addProcedure("writeln", -1);
    proceduresAndFunctionsTable.addProcedure("read", -1);
    proceduresAndFunctionsTable.addProcedure("readln", -1);
  }
}
