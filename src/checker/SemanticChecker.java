package checker;

import java.util.List;

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
import parser.PascalParser.Type_denoterContext;
import parser.PascalParser.Unsigned_constantContext;
import parser.PascalParser.Value_parameter_speficiationContext;
import parser.PascalParser.Variable_accessContext;
import parser.PascalParser.Variable_declarationContext;
import parser.PascalParserBaseVisitor;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.BuiltInProceduresAndFunctionsTable.BuiltInProceduresAndFunctionsEntry;
import tables.VariablesTable.VariableTableEntry;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import types.ArrayVariableType;
import types.PrimitiveType;
import types.PrimitiveVariableType;
import types.VariableType;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {
  String programHeadingIdentifier;

  // Tabela de literais para armazenar as strings literais encontradas no código
  private final StringLiteralsTable stringLiteralsTable = new StringLiteralsTable();

  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = new BuiltInProceduresAndFunctionsTable();

  // Tabela de símbolos para armazenar as variáveis declaradas no código
  private final VariablesTable globalVariablesTable = new VariablesTable();

  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable = new ProceduresAndFunctionsTable();

  public SemanticChecker() {
    registerPreDeclaredProceduresAndFunctions();
  }

  public void printLiteralsTable() {
    if (stringLiteralsTable.isEmpty()) {
      return;
    }

    System.out.println("STRING LITERALS TABLE:\n");
    System.out.println(stringLiteralsTable);
  }

  public void printGlobalVariablesTable() {
    if (globalVariablesTable.isEmpty()) {
      return;
    }

    System.out.println("\nGLOBAL VARIABLES TABLE:\n");
    System.out.println(globalVariablesTable);
  }

  public void printProceduresAndFunctionsTable() {
    if (proceduresAndFunctionsTable.isEmpty()) {
      return;
    }

    System.out.println("\nPROCEDURES AND FUNCTIONS TABLE:\n");
    System.out.println(proceduresAndFunctionsTable);
  }

  public void printBuiltInProceduresAndFunctionsTable() {
    if (builtInProceduresAndFunctionsTable.isEmpty()) {
      return;
    }

    System.out.println("\nBUILT-IN PROCEDURES AND FUNCTIONS TABLE:\n");
    System.out.println(builtInProceduresAndFunctionsTable);
  }

  // HELPER METHODS

  private void checkGlobalIdentifierIsNotDefined(Token identifierToken) {
    String identifier = identifierToken.getText();

    if (identifier.equals(programHeadingIdentifier)) {
      System.out.printf("SEMANTIC ERROR (%d): Program heading '%s' cannot be used.\n", identifierToken.getLine(), programHeadingIdentifier);

      System.exit(1);
    }

    List<BuiltInProceduresAndFunctionsEntry> builtInProceduresAndFunctionsEntry = builtInProceduresAndFunctionsTable.get(identifier);

    if (builtInProceduresAndFunctionsEntry != null) {
      BuiltInProceduresAndFunctionsEntry firstEntry = builtInProceduresAndFunctionsEntry.getFirst();

      System.out.printf("SEMANTIC ERROR (%d): '%s' is a built-in %s.\n", identifierToken.getLine(), firstEntry.identifier, firstEntry.type.toString());

      System.exit(1);
    }

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier);

    if (globalVariableEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Global variable '%s' was already declared at line %d.\n", identifierToken.getLine(), globalVariableEntry.identifier, globalVariableEntry.line);

      System.exit(1);
    }

    ProceduresAndFunctionsEntry proceduresAndFunctionsEntry = proceduresAndFunctionsTable.get(identifier);

    if (proceduresAndFunctionsEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): %s '%s' was already declared at line %d.\n", identifierToken.getLine(), proceduresAndFunctionsEntry.type.toString(), proceduresAndFunctionsEntry.identifier, proceduresAndFunctionsEntry.line);

      System.exit(1);
    }
  }

  private void checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(Token identifierToken, String procedureOrFunctionIdentifier) {
    String identifier = identifierToken.getText();

    ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable.get(identifier);

    if (procedureOrFunctionEntry == null) {
      return;
    }

    VariableTableEntry variableEntry = procedureOrFunctionEntry.localVariables.get(identifier);
    VariableTableEntry parameterEntry = procedureOrFunctionEntry.parameters.get(identifier);

    if (parameterEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Parameter '%s' of %s '%s' was already declared at line %d.\n", identifierToken.getLine(), procedureOrFunctionEntry.type.toString(), identifier, procedureOrFunctionIdentifier, parameterEntry.line);

      System.exit(1);
    }

    // Validação de duplicidade de declaração de variável
    if (variableEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Local variable '%s' of %s '%s' was already declared at line %d.\n", identifierToken.getLine(), procedureOrFunctionEntry.type.toString(), identifier, procedureOrFunctionIdentifier, variableEntry.line);

      System.exit(1);
    }
  }

  // Auxiliar reutilizável para cadastrar variáveis na tabela e checar duplicados
  private void registerGlobalVariables(Identifier_listContext context, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token identifierToken = identifierNode.getSymbol();
      String variableIdentifier = identifierToken.getText();

      checkGlobalIdentifierIsNotDefined(identifierToken);

      var varLine = identifierToken.getLine();
      globalVariablesTable.addVariable(variableIdentifier, varLine, variableType);
    }
  }

  private void registerProcedureOrFunctionLocalVariables(Identifier_listContext context, String procedureOrFunctionIdentifier, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();

      checkGlobalIdentifierIsNotDefined(token);
      checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(token, procedureOrFunctionIdentifier);

      String variableIdentifier = token.getText();
      var variableLine = token.getLine();

      proceduresAndFunctionsTable.addProcedlureOrFunctionVariable(procedureOrFunctionIdentifier, variableIdentifier, variableLine, variableType);
    }
  }

  private void registerProcedureOrFunctionParameters(Identifier_listContext context, String procedureOrFunctionIdentifier, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();

      checkGlobalIdentifierIsNotDefined(token);
      checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(token, procedureOrFunctionIdentifier);

      String variableIdentifier = token.getText();
      var variableLine = token.getLine();

      proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(procedureOrFunctionIdentifier, variableIdentifier, variableLine, variableType);
    }
  }

  private VariableType extractVariableTypeFromTypeDenoter(Type_denoterContext context) {
    VariableType type;

    if (context.primitive_type() != null) {
      var primitiveType = context.primitive_type();
      type = new PrimitiveVariableType(PrimitiveType.getType(primitiveType.getText()));
    }
    else {
      var arrayType = context.array_type();
      var primitiveType = PrimitiveType.getType(arrayType.primitive_type().getText());
      var subrangeType = arrayType.subrange_type();
      var startIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      var endIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());
      type = new ArrayVariableType(primitiveType, startIndex, endIndex);
    }

    return type;
  }

  // PROGRAM HEADING VISITOR

  @Override
  public Void visitProgram_heading(Program_headingContext context) {
    programHeadingIdentifier = context.IDENTIFIER().getText();
    return visitChildren(context);
  }

  // VISITORS DE DECLARAÇÃO DE VARIÁVEIS GLOBAIS E LOCAIS

  @Override
  public Void visitVariable_declaration(Variable_declarationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    var parent = context.parent.parent;

    if (parent instanceof Procedure_headingContext) {
      Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) parent;
      var identifier = procedureHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerProcedureOrFunctionLocalVariables(context.identifier_list(), identifier, type);
    }
    else
      if (parent instanceof Function_headingContext) {
        Function_headingContext functionHeadingContext = (Function_headingContext) parent;
        var identifier = functionHeadingContext.IDENTIFIER().getText();
        assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
        registerProcedureOrFunctionLocalVariables(context.identifier_list(), identifier, type);

      }
      else {
        registerGlobalVariables(context.identifier_list(), type);
      }

    return visitChildren(context);
  }

  // VISITORS DE DECLARAÇÃO DE PROCEDURES E FUNCTIONS

  @Override
  public Void visitProcedure_heading(Procedure_headingContext context) {
    var identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    var procedureIdentifier = identifierToken.getText();
    var line = identifierToken.getLine();

    proceduresAndFunctionsTable.addProcedure(procedureIdentifier, line);

    return visitChildren(context);
  }

  @Override
  public Void visitFunction_heading(Function_headingContext context) {
    var identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    var functionIdentifier = identifierToken.getText();
    var line = identifierToken.getLine();

    proceduresAndFunctionsTable.addProcedure(functionIdentifier, line);

    return visitChildren(context);
  }

  // VISITORS DE DECLARAÇÃO DE PARÂMETROS DE PROCEDURES E FUNCTIONS

  @Override
  public Void visitValue_parameter_speficiation(Value_parameter_speficiationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    var declaration = context.parent.parent;

    if (declaration instanceof Function_headingContext) {
      Function_headingContext functionHeadingContext = (Function_headingContext) declaration;
      var identifier = functionHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerProcedureOrFunctionParameters(context.identifier_list(), identifier, type);
    }
    else
      if (declaration instanceof Procedure_headingContext) {
        Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) declaration;
        var identifier = procedureHeadingContext.IDENTIFIER().getText();
        assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
        registerProcedureOrFunctionParameters(context.identifier_list(), identifier, type);
      }

    return visitChildren(context);
  }

  // CHECAGEM DE USO DE VARIÁVEIS LOCAIS E GLOBAIS

  @Override
  public Void visitVariable_access(Variable_accessContext context) {
    TerminalNode identifier;

    if (context.IDENTIFIER() != null) {
      identifier = context.IDENTIFIER();
    }
    else {
      var indexedVariable = context.indexed_variable();
      identifier = indexedVariable.IDENTIFIER();
    }

    String variableIdentifier = identifier.getSymbol().getText();

    if (globalVariablesTable.lookupVariable(identifier.getSymbol().getText())) {
      return visitChildren(context);
    }

    var parent = context.parent;

    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext) {
        Function_declarationContext functionDeclarationContext = (Function_declarationContext) parent;
        var functionHeading = functionDeclarationContext.function_heading();
        var functionIdentifier = functionHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(functionIdentifier, variableIdentifier) | proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(functionIdentifier, variableIdentifier) | variableIdentifier.equals(functionIdentifier)) {
          return visitChildren(context);
        }

        break;
      }
      else
        if (parent instanceof Procedure_declarationContext) {
          Procedure_declarationContext procedureDeclarationContext = (Procedure_declarationContext) parent;
          var procedureHeading = procedureDeclarationContext.procedure_heading();
          var procedureIdentifier = procedureHeading.IDENTIFIER().getText();

          if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(procedureIdentifier, variableIdentifier) | proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(procedureIdentifier, variableIdentifier)) {
            return visitChildren(context);
          }

          break;
        }

      parent = parent.parent;
    }

    System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", identifier.getSymbol().getLine(), variableIdentifier);

    System.exit(1);

    return visitChildren(context);
  }

  @Override
  public Void visitFor_statement(For_statementContext context) {
    var identifier = context.IDENTIFIER();

    String variableIdentifier = identifier.getSymbol().getText();

    if (globalVariablesTable.lookupVariable(identifier.getSymbol().getText())) {
      return visitChildren(context);
    }

    var parent = context.parent;
    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext) {
        Function_declarationContext functionDeclarationContext = (Function_declarationContext) parent;
        var functionHeading = functionDeclarationContext.function_heading();
        var functionIdentifier = functionHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(functionIdentifier, variableIdentifier) | proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(functionIdentifier, variableIdentifier)) {
          return visitChildren(context);
        }

        break;
      }
      else
        if (parent instanceof Procedure_declarationContext) {
          Procedure_declarationContext procedureDeclarationContext = (Procedure_declarationContext) parent;
          var procedureHeading = procedureDeclarationContext.procedure_heading();
          var procedureIdentifier = procedureHeading.IDENTIFIER().getText();

          if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(procedureIdentifier, variableIdentifier) | proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(procedureIdentifier, variableIdentifier)) {
            return visitChildren(context);
          }

          break;
        }

      parent = parent.parent;
    }

    System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", identifier.getSymbol().getLine(), variableIdentifier);

    System.exit(1);

    return visitChildren(context);
  }

  // CHECAGEM DE USO DE PROCEDURES E FUNCTIONS

  @Override
  public Void visitProcedure_statement(Procedure_statementContext context) {
    var identifier = context.IDENTIFIER();

    if (!proceduresAndFunctionsTable.lookProcedureOrFunction(identifier.getText())) {
      System.out.printf("SEMANTIC ERROR (%d): Procedure '%s' is not defined.", identifier.getSymbol().getLine(), identifier.getText());

      System.exit(1);
    }

    return visitChildren(context);
  }

  @Override
  public Void visitFunction_designator(Function_designatorContext context) {
    var identifier = context.IDENTIFIER();

    if (!proceduresAndFunctionsTable.lookProcedureOrFunction(identifier.getText())) {
      System.out.printf("SEMANTIC ERROR (%d): Function '%s' is not defined.", identifier.getSymbol().getLine(), identifier.getText());

      System.exit(1);
    }

    return visitChildren(context);
  }

  // CHECAGEM DE USO DE LITERAIS

  @Override
  public Void visitUnsigned_constant(Unsigned_constantContext context) {
    // Como unsigned_constant aceita números, precisamos isolar apenas a String
    if (context.CHARACTER_STRING() != null) {
      String stringLiteral = context.CHARACTER_STRING().getText();
      // Remove as aspas simples de início e fim ('texto' -> texto)
      stringLiteralsTable.addStringLiteral(stringLiteral.substring(1, stringLiteral.length() - 1));
    }

    return null;
  }

  // PRE DECLARED PROCEDURES AND FUNCTIONS

  private void registerPreDeclaredProceduresAndFunctions() {
    var stringParameterType = new VariableTableEntry("str", new PrimitiveVariableType(PrimitiveType.STRING));

    builtInProceduresAndFunctionsTable.addProcedure("write", List.of(stringParameterType));
    builtInProceduresAndFunctionsTable.addProcedure("writeln", List.of(stringParameterType));
    builtInProceduresAndFunctionsTable.addProcedure("read", List.of(stringParameterType));
    builtInProceduresAndFunctionsTable.addProcedure("readln", List.of(stringParameterType));
  }
}
