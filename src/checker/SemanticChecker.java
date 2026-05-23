package checker;

import java.util.List;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser.Array_typeContext;
import parser.PascalParser.For_statementContext;
import parser.PascalParser.Function_declarationContext;
import parser.PascalParser.Function_designatorContext;
import parser.PascalParser.Function_headingContext;
import parser.PascalParser.Identifier_listContext;
import parser.PascalParser.Indexed_variableContext;
import parser.PascalParser.Primitive_typeContext;
import parser.PascalParser.Procedure_declarationContext;
import parser.PascalParser.Procedure_headingContext;
import parser.PascalParser.Procedure_statementContext;
import parser.PascalParser.ProgramContext;
import parser.PascalParser.Program_headingContext;
import parser.PascalParser.Subrange_typeContext;
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

  // Table to store string literals found in the code
  private final StringLiteralsTable stringLiteralsTable = new StringLiteralsTable();

  // Symbol table to store pre-declared procedures and functions and their parameters
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = new BuiltInProceduresAndFunctionsTable();

  // Symbol table to store variables declared in the code
  private final VariablesTable globalVariablesTable = new VariablesTable();

  // Symbol table to store declared procedures and functions, their local variables and parameters
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

  // Helper methods

  // Pre-declared procedures and functions
  private void registerPreDeclaredProceduresAndFunctions() {
    // Reusable primitive types
    VariableType typeString = new PrimitiveVariableType(PrimitiveType.STRING);
    VariableType typeInt = new PrimitiveVariableType(PrimitiveType.INTEGER);
    VariableType typeReal = new PrimitiveVariableType(PrimitiveType.REAL);
    VariableType typeChar = new PrimitiveVariableType(PrimitiveType.CHAR);

    // Standard parameter entries
    VariableTableEntry stringParam = new VariableTableEntry("str", typeString);
    VariableTableEntry intParam = new VariableTableEntry("n", typeInt);
    VariableTableEntry realParam = new VariableTableEntry("n", typeReal);
    VariableTableEntry charParam = new VariableTableEntry("c", typeChar);

    // --- PROCEDURES ---
    // I/O
    builtInProceduresAndFunctionsTable.addProcedure("write", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("writeln", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("read", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("readln", List.of(stringParam));

    // --- FUNCTIONS ---
    // Math
    builtInProceduresAndFunctionsTable.addFunction("abs", List.of(intParam), PrimitiveType.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("sqr", List.of(intParam), PrimitiveType.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("sqrt", List.of(realParam), PrimitiveType.REAL);
    builtInProceduresAndFunctionsTable.addFunction("trunc", List.of(realParam), PrimitiveType.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("round", List.of(realParam), PrimitiveType.INTEGER);

    // Ordinal & Character
    builtInProceduresAndFunctionsTable.addFunction("ord", List.of(charParam), PrimitiveType.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("chr", List.of(intParam), PrimitiveType.CHAR);
    builtInProceduresAndFunctionsTable.addFunction("succ", List.of(intParam), PrimitiveType.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("pred", List.of(intParam), PrimitiveType.INTEGER);

    // String
    builtInProceduresAndFunctionsTable.addFunction("length", List.of(stringParam), PrimitiveType.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("upcase", List.of(charParam), PrimitiveType.CHAR);
  }

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

    // Validate duplicate variable declaration
    if (variableEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Local variable '%s' of %s '%s' was already declared at line %d.\n", identifierToken.getLine(), procedureOrFunctionEntry.type.toString(), identifier, procedureOrFunctionIdentifier, variableEntry.line);

      System.exit(1);
    }
  }

  // Reusable helper to register variables in the table and check duplicates
  private void registerGlobalVariables(Identifier_listContext context, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token identifierToken = identifierNode.getSymbol();
      String variableIdentifier = identifierToken.getText();

      checkGlobalIdentifierIsNotDefined(identifierToken);

      int varLine = identifierToken.getLine();
      globalVariablesTable.addVariable(variableIdentifier, varLine, variableType);
    }
  }

  private void registerProcedureOrFunctionLocalVariables(Identifier_listContext context, String procedureOrFunctionIdentifier, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();

      checkGlobalIdentifierIsNotDefined(token);
      checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(token, procedureOrFunctionIdentifier);

      String variableIdentifier = token.getText();
      int variableLine = token.getLine();

      proceduresAndFunctionsTable.addProcedlureOrFunctionVariable(procedureOrFunctionIdentifier, variableIdentifier, variableLine, variableType);
    }
  }

  private void registerProcedureOrFunctionParameters(Identifier_listContext context, String procedureOrFunctionIdentifier, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();

      checkGlobalIdentifierIsNotDefined(token);
      checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(token, procedureOrFunctionIdentifier);

      String variableIdentifier = token.getText();
      int variableLine = token.getLine();

      proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(procedureOrFunctionIdentifier, variableIdentifier, variableLine, variableType);
    }
  }

  private VariableType extractVariableTypeFromTypeDenoter(Type_denoterContext context) {
    VariableType type;

    if (context.primitive_type() != null) {
      Primitive_typeContext primitiveType = context.primitive_type();
      type = new PrimitiveVariableType(PrimitiveType.getType(primitiveType.getText()));
    }
    else {
      Array_typeContext arrayType = context.array_type();
      PrimitiveType primitiveType = PrimitiveType.getType(arrayType.primitive_type().getText());
      Subrange_typeContext subrangeType = arrayType.subrange_type();
      int startIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      int endIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());
      type = new ArrayVariableType(primitiveType, startIndex, endIndex);
    }

    return type;
  }

  // Program heading visitor

  @Override
  public Void visitProgram_heading(Program_headingContext context) {
    programHeadingIdentifier = context.IDENTIFIER().getText();
    return visitChildren(context);
  }

  // Visitors for global and local variable declarations

  @Override
  public Void visitVariable_declaration(Variable_declarationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    org.antlr.v4.runtime.RuleContext parent = context.parent.parent;

    if (parent instanceof Procedure_headingContext) {
      Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) parent;
      String identifier = procedureHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerProcedureOrFunctionLocalVariables(context.identifier_list(), identifier, type);
    }
    else
      if (parent instanceof Function_headingContext) {
        Function_headingContext functionHeadingContext = (Function_headingContext) parent;
        String identifier = functionHeadingContext.IDENTIFIER().getText();
        assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
        registerProcedureOrFunctionLocalVariables(context.identifier_list(), identifier, type);

      }
      else {
        registerGlobalVariables(context.identifier_list(), type);
      }

    return visitChildren(context);
  }

  // Visitors for procedure and function declarations

  @Override
  public Void visitProcedure_heading(Procedure_headingContext context) {
    Token identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    String procedureIdentifier = identifierToken.getText();
    int line = identifierToken.getLine();

    proceduresAndFunctionsTable.addProcedure(procedureIdentifier, line);

    return visitChildren(context);
  }

  @Override
  public Void visitFunction_heading(Function_headingContext context) {
    Token identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    String functionIdentifier = identifierToken.getText();
    int line = identifierToken.getLine();

    proceduresAndFunctionsTable.addProcedure(functionIdentifier, line);

    return visitChildren(context);
  }

  // Visitors for procedure/function parameter declarations

  @Override
  public Void visitValue_parameter_speficiation(Value_parameter_speficiationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    RuleContext declaration = context.parent.parent;

    if (declaration instanceof Function_headingContext) {
      Function_headingContext functionHeadingContext = (Function_headingContext) declaration;
      String identifier = functionHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerProcedureOrFunctionParameters(context.identifier_list(), identifier, type);
    }
    else
      if (declaration instanceof Procedure_headingContext) {
        Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) declaration;
        String identifier = procedureHeadingContext.IDENTIFIER().getText();
        assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
        registerProcedureOrFunctionParameters(context.identifier_list(), identifier, type);
      }

    return visitChildren(context);
  }

  // Checking usage of local and global variables

  @Override
  public Void visitVariable_access(Variable_accessContext context) {
    TerminalNode identifier;

    if (context.IDENTIFIER() != null) {
      identifier = context.IDENTIFIER();
    }
    else {
      Indexed_variableContext indexedVariable = context.indexed_variable();
      identifier = indexedVariable.IDENTIFIER();
    }

    String variableIdentifier = identifier.getSymbol().getText();

    if (globalVariablesTable.lookupVariable(identifier.getSymbol().getText())) {
      return visitChildren(context);
    }

    RuleContext parent = context.parent;

    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext) {
        Function_declarationContext functionDeclarationContext = (Function_declarationContext) parent;
        Function_headingContext functionHeading = functionDeclarationContext.function_heading();
        String functionIdentifier = functionHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(functionIdentifier, variableIdentifier) | proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(functionIdentifier, variableIdentifier) | variableIdentifier.equals(functionIdentifier)) {
          return visitChildren(context);
        }

        break;
      }
      else
        if (parent instanceof Procedure_declarationContext) {
          Procedure_declarationContext procedureDeclarationContext = (Procedure_declarationContext) parent;
          Procedure_headingContext procedureHeading = procedureDeclarationContext.procedure_heading();
          String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

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
    TerminalNode identifier = context.IDENTIFIER();

    String variableIdentifier = identifier.getSymbol().getText();

    if (globalVariablesTable.lookupVariable(identifier.getSymbol().getText())) {
      return visitChildren(context);
    }

    org.antlr.v4.runtime.RuleContext parent = context.parent;
    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext) {
        Function_declarationContext functionDeclarationContext = (Function_declarationContext) parent;
        Function_headingContext functionHeading = functionDeclarationContext.function_heading();
        String functionIdentifier = functionHeading.IDENTIFIER().getText();

        if (proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalVariable(functionIdentifier, variableIdentifier) | proceduresAndFunctionsTable.lookupProcedureOrFunctionLocalParameter(functionIdentifier, variableIdentifier)) {
          return visitChildren(context);
        }

        break;
      }
      else
        if (parent instanceof Procedure_declarationContext) {
          Procedure_declarationContext procedureDeclarationContext = (Procedure_declarationContext) parent;
          Procedure_headingContext procedureHeading = procedureDeclarationContext.procedure_heading();
          String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

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

  // Checking usage of procedures and functions

  @Override
  public Void visitProcedure_statement(Procedure_statementContext context) {
    TerminalNode identifier = context.IDENTIFIER();

    if (!proceduresAndFunctionsTable.lookProcedureOrFunction(identifier.getText())) {
      System.out.printf("SEMANTIC ERROR (%d): Procedure '%s' is not defined.", identifier.getSymbol().getLine(), identifier.getText());

      System.exit(1);
    }

    return visitChildren(context);
  }

  @Override
  public Void visitFunction_designator(Function_designatorContext context) {
    TerminalNode identifier = context.IDENTIFIER();

    if (!proceduresAndFunctionsTable.lookProcedureOrFunction(identifier.getText())) {
      System.out.printf("SEMANTIC ERROR (%d): Function '%s' is not defined.", identifier.getSymbol().getLine(), identifier.getText());

      System.exit(1);
    }

    return visitChildren(context);
  }

  // Checking usage of literals

  @Override
  public Void visitUnsigned_constant(Unsigned_constantContext context) {
    // Since unsigned_constant accepts numbers, isolate only the string literal
    if (context.CHARACTER_STRING() != null) {
      String stringLiteral = context.CHARACTER_STRING().getText();
      // Remove the surrounding single quotes ('text' -> text)
      stringLiteralsTable.addStringLiteral(stringLiteral.substring(1, stringLiteral.length() - 1));
    }

    return null;
  }
}
