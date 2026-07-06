package checker;

import java.util.List;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser.Actual_parameterContext;
import parser.PascalParser.Actual_parameter_listContext;
import parser.PascalParser.Adding_operatorContext;
import parser.PascalParser.Array_typeContext;
import parser.PascalParser.Assignment_statementContext;
import parser.PascalParser.BooleanConstantContext;
import parser.PascalParser.Boolean_constantContext;
import parser.PascalParser.ExpressionContext;
import parser.PascalParser.FactorContext;
import parser.PascalParser.For_statementContext;
import parser.PascalParser.FunctionCallContext;
import parser.PascalParser.Function_declarationContext;
import parser.PascalParser.Function_designatorContext;
import parser.PascalParser.Function_headingContext;
import parser.PascalParser.Identifier_listContext;
import parser.PascalParser.If_statementContext;
import parser.PascalParser.Indexed_variableContext;
import parser.PascalParser.Multiplying_operatorContext;
import parser.PascalParser.NotFactorContext;
import parser.PascalParser.NumericConstantContext;
import parser.PascalParser.Numeric_constantContext;
import parser.PascalParser.ParenthesisExpressionContext;
import parser.PascalParser.Primitive_typeContext;
import parser.PascalParser.Procedure_declarationContext;
import parser.PascalParser.Procedure_headingContext;
import parser.PascalParser.Procedure_statementContext;
import parser.PascalParser.ProgramContext;
import parser.PascalParser.Program_headingContext;
import parser.PascalParser.Simple_expressionContext;
import parser.PascalParser.StringConstantContext;
import parser.PascalParser.Subrange_typeContext;
import parser.PascalParser.TermContext;
import parser.PascalParser.Type_denoterContext;
import parser.PascalParser.Value_parameter_speficiationContext;
import parser.PascalParser.VariableAccessContext;
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
import types.ConstantPrimitiveVariableType;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;
import types.ProcedureOrFunctionEnum;
import types.TypeRules;
import types.VariableType;

public class SemanticChecker extends PascalParserBaseVisitor<VariableType> {
  // Program identifier
  String programIdentifier;

  // Table to store string literals found in the code
  private final StringLiteralsTable stringLiteralsTable = new StringLiteralsTable();
  
  // Symbol table to store variables declared in the code
  private final VariablesTable globalVariablesTable = new VariablesTable();
  
  // Symbol table to store pre-declared procedures and functions and their parameters
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = new BuiltInProceduresAndFunctionsTable();

  // Symbol table to store declared procedures and functions, their local variables and parameters
  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable = new ProceduresAndFunctionsTable();

  public String getProgramIdentifier() {
    return programIdentifier;
  }

  public StringLiteralsTable getStringLiteralsTable() {
    return stringLiteralsTable;
  }

  public VariablesTable getGlobalVariablesTable() {
    return globalVariablesTable;
  }

  public BuiltInProceduresAndFunctionsTable getBuiltInProceduresAndFunctionsTable() {
    return builtInProceduresAndFunctionsTable;
  }

  public ProceduresAndFunctionsTable getProceduresAndFunctionsTable() {
    return proceduresAndFunctionsTable;
  }

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
    VariableType typeString = new PrimitiveVariableType(PrimitiveTypeEnum.STRING);
    VariableType typeInteger = new PrimitiveVariableType(PrimitiveTypeEnum.INTEGER);
    VariableType typeReal = new PrimitiveVariableType(PrimitiveTypeEnum.REAL);
    VariableType typeChar = new PrimitiveVariableType(PrimitiveTypeEnum.CHAR);
    VariableType typeBoolean = new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN);

    // Standard parameter entries
    VariableTableEntry stringParam = new VariableTableEntry("str", typeString);
    VariableTableEntry integerParam = new VariableTableEntry("n", typeInteger);
    VariableTableEntry realParam = new VariableTableEntry("n", typeReal);
    VariableTableEntry charParam = new VariableTableEntry("c", typeChar);
    VariableTableEntry booleanParam = new VariableTableEntry("b", typeBoolean);

    // --- PROCEDURES ---
    // I/O
    builtInProceduresAndFunctionsTable.addProcedure("write", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("writeln", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("read", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("readln", List.of(stringParam));

    // --- FUNCTIONS ---
    // Math
    builtInProceduresAndFunctionsTable.addFunction("abs", List.of(realParam), PrimitiveTypeEnum.REAL);
    builtInProceduresAndFunctionsTable.addFunction("sqr", List.of(realParam), PrimitiveTypeEnum.REAL);
    builtInProceduresAndFunctionsTable.addFunction("sqrt", List.of(realParam), PrimitiveTypeEnum.REAL);
    builtInProceduresAndFunctionsTable.addFunction("trunc", List.of(realParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("round", List.of(realParam), PrimitiveTypeEnum.INTEGER);

    // Ordinal & Character
    builtInProceduresAndFunctionsTable.addFunction("ord", List.of(charParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("chr", List.of(integerParam), PrimitiveTypeEnum.CHAR);
    builtInProceduresAndFunctionsTable.addFunction("succ", List.of(integerParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("pred", List.of(integerParam), PrimitiveTypeEnum.INTEGER);

    // String
    builtInProceduresAndFunctionsTable.addFunction("length", List.of(stringParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("upcase", List.of(charParam), PrimitiveTypeEnum.CHAR);
    builtInProceduresAndFunctionsTable.addFunction("itos", List.of(integerParam), PrimitiveTypeEnum.STRING);
    builtInProceduresAndFunctionsTable.addFunction("rtos", List.of(realParam), PrimitiveTypeEnum.STRING);
    builtInProceduresAndFunctionsTable.addFunction("btos", List.of(booleanParam), PrimitiveTypeEnum.STRING);
  }

  private void checkGlobalIdentifierIsNotDefined(Token identifierToken) {
    String identifier = identifierToken.getText();

    if (identifier.equalsIgnoreCase(programIdentifier)) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Program heading '%s' cannot be used.\n",
        identifierToken.getLine(),
        programIdentifier
      );

      System.exit(1);
    }

    BuiltInProceduresAndFunctionsEntry builtInProceduresAndFunctionsEntry = builtInProceduresAndFunctionsTable.get(identifier);

    if (builtInProceduresAndFunctionsEntry != null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): '%s' is a built-in %s.\n",
        identifierToken.getLine(),
        builtInProceduresAndFunctionsEntry.identifier,
        builtInProceduresAndFunctionsEntry.type.toString()
      );

      System.exit(1);
    }

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier);

    if (globalVariableEntry != null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Global variable '%s' was already declared at line %d.\n",
        identifierToken.getLine(),
        globalVariableEntry.identifier,
        globalVariableEntry.line
      );

      System.exit(1);
    }

    ProceduresAndFunctionsEntry proceduresAndFunctionsEntry = proceduresAndFunctionsTable.get(identifier);

    if (proceduresAndFunctionsEntry != null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): %s '%s' was already declared at line %d.\n",
        identifierToken.getLine(),
        proceduresAndFunctionsEntry.type.toString(),
        proceduresAndFunctionsEntry.identifier,
        proceduresAndFunctionsEntry.line
      );

      System.exit(1);
    }
  }

  private void checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(Token identifierToken, String procedureOrFunctionIdentifier) {
    String identifier = identifierToken.getText();

    ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable.get(procedureOrFunctionIdentifier);

    if (procedureOrFunctionEntry == null) {
      return;
    }

    VariableTableEntry variableEntry = procedureOrFunctionEntry.localVariables.get(identifier);
    VariableTableEntry parameterEntry = procedureOrFunctionEntry.parameters.get(identifier);

    if (parameterEntry != null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Parameter '%s' of %s '%s' was already declared at line %d.\n",
        identifierToken.getLine(),
        identifier,
        procedureOrFunctionEntry.type.toString(),
        procedureOrFunctionIdentifier,
        parameterEntry.line
      );

      System.exit(1);
    }

    // Validate duplicate variable declaration
    if (variableEntry != null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Local variable '%s' of %s '%s' was already declared at line %d.\n",
        identifierToken.getLine(),
        identifier,
        procedureOrFunctionEntry.type.toString(),
        procedureOrFunctionIdentifier,
        variableEntry.line
      );

      System.exit(1);
    }
  }

  private void unaryOperationTypeError(int line, String operation, VariableType type) {
    System.out.printf(
    "SEMANTIC ERROR (%d): incompatible type for operator '%s', type is '%s'.\n",
      line,
      operation,
      type.toString()
    );

    System.exit(1);
  }

  private void binaryOperationTypeError(int line, String operation, VariableType leftType, VariableType rightType) {
    System.out.printf(
    "SEMANTIC ERROR (%d): incompatible types for operator '%s', LHS is '%s' and RHS is '%s'.\n",
      line,
      operation,
      leftType.toString(),
      rightType.toString()
    );

    System.exit(1);
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
      type = new PrimitiveVariableType(PrimitiveTypeEnum.getType(primitiveType.getText()));
    }
    else {
      Array_typeContext arrayType = context.array_type();
      PrimitiveTypeEnum primitiveType = PrimitiveTypeEnum.getType(arrayType.primitive_type().getText());
      Subrange_typeContext subrangeType = arrayType.subrange_type();
      int startIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      int endIndex = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());

      if(endIndex <= startIndex) {
        System.out.printf(
          "SEMANTIC ERROR (%d): End index (%d) must be bigger than start index (%d) in declaration of array variables.\n",
          arrayType.ARRAY().getSymbol().getLine(),
          endIndex,
          startIndex
        );

        System.exit(1);
      }

      type = new ArrayVariableType(primitiveType, startIndex, endIndex);
    }

    return type;
  }

  // Program heading visitor

  @Override
  public VariableType visitProgram_heading(Program_headingContext context) {
    programIdentifier = context.IDENTIFIER().getText();
    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  // Visitors for global and local variable declarations

  @Override
  public VariableType visitVariable_declaration(Variable_declarationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    RuleContext parent = context.parent.parent;

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

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  // Visitors for procedure and function declarations

  @Override
  public VariableType visitProcedure_heading(Procedure_headingContext context) {
    Token identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    String procedureIdentifier = identifierToken.getText();
    int line = identifierToken.getLine();

    proceduresAndFunctionsTable.addProcedure(procedureIdentifier, line);

    return visitChildren(context);
  }

  @Override
  public VariableType visitFunction_heading(Function_headingContext context) {
    Token identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    String functionIdentifier = identifierToken.getText();
    int line = identifierToken.getLine();
    VariableType returnType = extractVariableTypeFromTypeDenoter(context.type_denoter());

    if(!(returnType instanceof PrimitiveVariableType)) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Function '%s' return type should be a primitive type.\n",
        line,
        functionIdentifier
      );

      System.exit(1);
    }

    proceduresAndFunctionsTable.addFunction(functionIdentifier, line, returnType.basePrimitiveType);

    return visitChildren(context);
  }

  // Visitors for procedure/function parameter declarations

  @Override
  public VariableType visitValue_parameter_speficiation(Value_parameter_speficiationContext context) {
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

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  // Checking usage of local and global variables

  @Override
  public VariableType visitVariable_access(Variable_accessContext context) {
    TerminalNode identifier;
    boolean isIndexedVariable = false;

    if (context.IDENTIFIER() != null) {
      identifier = context.IDENTIFIER();
    }
    else {
      Indexed_variableContext indexedVariable = context.indexed_variable();

      var expression = indexedVariable.expression();
      var expressionReturnType = visit(expression);

      if(!(expressionReturnType instanceof PrimitiveVariableType) || !(expressionReturnType.basePrimitiveType == PrimitiveTypeEnum.INTEGER)) {
        System.out.printf(
          "SEMANTIC ERROR (%d): indexing expression must be an integer.\n",
          expression.start.getLine()
        );

        System.exit(1);
      }

      identifier = indexedVariable.IDENTIFIER();
      isIndexedVariable = true;
    }

    String variableIdentifier = identifier.getSymbol().getText();

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier.getSymbol().getText()); 
    if (globalVariableEntry != null) {
      if(!isIndexedVariable) {
        return globalVariableEntry.type;
      }

      if(!(globalVariableEntry.type instanceof ArrayVariableType || (globalVariableEntry.type instanceof PrimitiveVariableType && globalVariableEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
        System.out.printf(
          "SEMANTIC ERROR (%d): expression must be indexable.\n",
          context.start.getLine()
        );

        System.exit(1);
      }

      if(globalVariableEntry.type instanceof PrimitiveVariableType && globalVariableEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
        return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
      }

      return new PrimitiveVariableType(((ArrayVariableType) globalVariableEntry.type).basePrimitiveType);
    }

    RuleContext parent = context.parent;

    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext functionDeclarationContext) {
        Function_headingContext functionHeading = functionDeclarationContext.function_heading();
        String functionIdentifier = functionHeading.IDENTIFIER().getText();

        ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable.get(functionIdentifier);
        
        if(variableIdentifier.equalsIgnoreCase(functionIdentifier)) {
          return new PrimitiveVariableType(procedureOrFunctionEntry.returnType);
        }

        VariableTableEntry parameterEntry = procedureOrFunctionEntry.parameters.get(variableIdentifier);

        if (parameterEntry != null) {
          if(!isIndexedVariable) {
            return parameterEntry.type;
          }

          if(!(parameterEntry.type instanceof ArrayVariableType || (parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
            System.out.printf(
              "SEMANTIC ERROR (%d): expression must be indexable.\n",
              context.start.getLine()
            );

            System.exit(1);
          }

          if(parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
          }

          return new PrimitiveVariableType(((ArrayVariableType) parameterEntry.type).basePrimitiveType);
        }

        VariableTableEntry localEntry = procedureOrFunctionEntry.localVariables.get(variableIdentifier);

        if (localEntry != null) {
          if(!isIndexedVariable) {
            return localEntry.type;
          }

          if(!(localEntry.type instanceof ArrayVariableType || (localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
            System.out.printf(
              "SEMANTIC ERROR (%d): expression must be indexable.\n",
              context.start.getLine()
            );
    
            System.exit(1);
          }
          
          if(localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
          }

          return new PrimitiveVariableType(((ArrayVariableType) localEntry.type).basePrimitiveType);
        }

        break;
      }
      else if (parent instanceof Procedure_declarationContext procedureDeclarationContext) {
          Procedure_headingContext procedureHeading = procedureDeclarationContext.procedure_heading();
          String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

          ProceduresAndFunctionsEntry paramterEntry = proceduresAndFunctionsTable.get(procedureIdentifier);

          VariableTableEntry parameterEntry = paramterEntry.parameters.get(variableIdentifier);

          if (parameterEntry != null) {
            if(!isIndexedVariable) {
              return parameterEntry.type;
            }

            if(!(parameterEntry.type instanceof ArrayVariableType || (parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
              System.out.printf(
                "SEMANTIC ERROR (%d): expression must be indexable.\n",
                context.start.getLine()
              );
      
              System.exit(1);
            }

            if(parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
              return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
            }

            return new PrimitiveVariableType(((ArrayVariableType) parameterEntry.type).basePrimitiveType);
          }

          VariableTableEntry localEntry = paramterEntry.localVariables.get(variableIdentifier);

          if (localEntry != null) {
            if(!isIndexedVariable) {
              return localEntry.type;
            }

            if(!(localEntry.type instanceof ArrayVariableType || (localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
              System.out.printf(
                "SEMANTIC ERROR (%d): expression must be indexable.\n",
                context.start.getLine()
              );
      
              System.exit(1);
            }

            if(localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
              return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
            }

            return new PrimitiveVariableType(((ArrayVariableType) localEntry.type).basePrimitiveType);
          }

          break;
        }

      parent = parent.parent;
    }

    System.out.printf(
      "SEMANTIC ERROR (%d): Variable '%s' was not declared.\n",
      identifier.getSymbol().getLine(),
      variableIdentifier
    );

    System.exit(1);

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  @Override
  public VariableType visitIndexed_variable(Indexed_variableContext context) {
    ExpressionContext expression = context.expression();
    VariableType expressionType = visit(expression);
    
    if(!(expressionType instanceof PrimitiveVariableType) || expressionType.basePrimitiveType != PrimitiveTypeEnum.INTEGER) {
      TerminalNode bracket = context.OPEN_BRACKET();
      unaryOperationTypeError(bracket.getSymbol().getLine(), "[]", expressionType);
    }

    TerminalNode identifier = context.IDENTIFIER();

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier.getSymbol().getText()); 
    
    if (globalVariableEntry != null) {
      return new PrimitiveVariableType(globalVariableEntry.type.basePrimitiveType);
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  private static void checkVariableIsOrdinal(Token identifierToken, VariableType variableType) {
    if(!variableType.isOrdinal()) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Variable '%s' should be an ordinal type.\n",
        identifierToken.getLine(),
        identifierToken.getText()
      );

      System.exit(1);
    }
  }

  private static void checkPrimitiveTypesAreEqual(int line, PrimitiveVariableType leftType, PrimitiveVariableType rightType) {
    if(leftType.basePrimitiveType != rightType.basePrimitiveType) {
      System.out.printf(
        "SEMANTIC ERROR (%d): control variable type is '%s' and loop bounds types are '%s'.\n",
        line,
        leftType.toString(),
        rightType.toString()
      );

      System.exit(1);
    }
  }

  @Override
  public VariableType visitFor_statement(For_statementContext context) {
    visit(context.statement());

    TerminalNode identifier = context.IDENTIFIER();

    String variableIdentifier = identifier.getSymbol().getText();

    VariableType beginExpression = visit(context.expression(0));
    VariableType endExpression = visit(context.expression(1));

    if(!(beginExpression instanceof PrimitiveVariableType) || (beginExpression.basePrimitiveType != endExpression.basePrimitiveType)) {
      System.out.printf(
        "SEMANTIC ERROR (%d): incompatible begin and end variable types: %s and %s.\n",
        context.FOR().getSymbol().getLine(),
        beginExpression.toString(),
        endExpression.toString()
      );

      System.exit(1);
    }

    boolean isDownTo = context.DOWNTO() != null;
    
    if(
      beginExpression instanceof ConstantPrimitiveVariableType beginExpressionWithValue &&
      endExpression instanceof ConstantPrimitiveVariableType endExpressionWithValue
    ) {
      boolean isBeginSmallerThanEnd = switch (beginExpressionWithValue.value) {
        case Integer _ -> (int) beginExpressionWithValue.value < (int) endExpressionWithValue.value;
        case Double _ -> (double) beginExpressionWithValue.value < (double) endExpressionWithValue.value;
        case Character _ -> (char) beginExpressionWithValue.value < (char) endExpressionWithValue.value;
        case Boolean _ -> !((boolean) beginExpressionWithValue.value) && (boolean) endExpressionWithValue.value;
        default -> throw new RuntimeException("Control variable of for statement must be an ordinal type");
      };
  
      boolean isBeginBiggerThanEnd = switch (beginExpressionWithValue.value) {
        case Integer _ -> (int) beginExpressionWithValue.value > (int) endExpressionWithValue.value;
        case Double _ -> (double) beginExpressionWithValue.value > (double) endExpressionWithValue.value;
        case Character _ -> (char) beginExpressionWithValue.value > (char) endExpressionWithValue.value;
        case Boolean _ -> (boolean) beginExpressionWithValue.value && !((boolean) endExpressionWithValue.value);
        default -> throw new RuntimeException("Control variable of for statement must be an ordinal type");
      };
  
      if(isDownTo && !isBeginBiggerThanEnd) {
        System.out.printf(
          "SEMANTIC ERROR (%d): incompatible begin and end variable values: %s downto %s.\n",
          context.FOR().getSymbol().getLine(),
          beginExpressionWithValue.value.toString(),
          endExpressionWithValue.value.toString()
        );
  
        System.exit(1);
      }
      else if(!isDownTo  && !isBeginSmallerThanEnd) {
        System.out.printf(
          "SEMANTIC ERROR (%d): incompatible begin and end variable values: %s to %s.\n",
          context.FOR().getSymbol().getLine(),
          beginExpressionWithValue.value.toString(),
          endExpressionWithValue.value.toString()
        );
  
        System.exit(1);
      }
    }

    PrimitiveVariableType forLoopType = (PrimitiveVariableType) beginExpression;

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier.getSymbol().getText()); 
    if (globalVariableEntry != null) {
      checkVariableIsOrdinal(identifier.getSymbol(), globalVariableEntry.type);
      checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) globalVariableEntry.type, forLoopType);

      return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
    }

    RuleContext parent = context.parent;
    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext functionDeclarationContext) {
        Function_headingContext functionHeading = functionDeclarationContext.function_heading();
        String functionIdentifier = functionHeading.IDENTIFIER().getText();

        ProceduresAndFunctionsEntry paramterEntry = proceduresAndFunctionsTable.get(functionIdentifier);

        VariableTableEntry parameterEntry = paramterEntry.parameters.get(variableIdentifier);
        checkVariableIsOrdinal(identifier.getSymbol(), parameterEntry.type);
        checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) parameterEntry.type, forLoopType);

        VariableTableEntry localEntry = paramterEntry.localVariables.get(variableIdentifier);
        checkVariableIsOrdinal(identifier.getSymbol(), localEntry.type);
        checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) localEntry.type, forLoopType);

        return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
      }
      else
        if (parent instanceof Procedure_declarationContext procedureDeclarationContext) {
          Procedure_headingContext procedureHeading = procedureDeclarationContext.procedure_heading();
          String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

          ProceduresAndFunctionsEntry paramterEntry = proceduresAndFunctionsTable.get(procedureIdentifier);

          VariableTableEntry parameterEntry = paramterEntry.parameters.get(variableIdentifier);
          checkVariableIsOrdinal(identifier.getSymbol(), parameterEntry.type);
          checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) parameterEntry.type, forLoopType);

          VariableTableEntry localEntry = paramterEntry.localVariables.get(variableIdentifier);
          checkVariableIsOrdinal(identifier.getSymbol(), localEntry.type);
          checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) localEntry.type, forLoopType);


          return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
        }

      parent = parent.parent;
    }

    System.out.printf(
      "SEMANTIC ERROR (%d): Variable '%s' was not declared.\n",
      identifier.getSymbol().getLine(),
      variableIdentifier
    );

    System.exit(1);

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  @Override
  public VariableType visitAssignment_statement(Assignment_statementContext context) {
    VariableType leftType = visit(context.variable_access());
    VariableType rightType = visit(context.expression());

    if(leftType instanceof PrimitiveVariableType && leftType.basePrimitiveType == PrimitiveTypeEnum.CHAR && leftType.isIndexed) {
      System.out.printf(
        "SEMANTIC ERROR (%d): string cannot be indexed in lhs expression!\n",
        context.ASSIGNMENT().getSymbol().getLine()
      );
      
      System.exit(1);
    }

    if(leftType instanceof ArrayVariableType || rightType instanceof ArrayVariableType) {
      if(!rightType.isEquivalent(leftType)) {
        System.out.printf(
          "SEMANTIC ERROR (%d): incompatible type: type expected is %s, and the type is %s!\n",
          context.ASSIGNMENT().getSymbol().getLine(),
          leftType.toString(),
          rightType.toString()
        );
        
        System.exit(1);
      }

      return leftType;
    }

    PrimitiveTypeEnum returnType = TypeRules.getResultType(TypeRules.ASSIGNMENT_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
    
    if(returnType == PrimitiveTypeEnum.NO_TYPE) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Assignment statement has incompatible types.\n",
        context.ASSIGNMENT().getSymbol().getLine()
      );
      
      System.exit(1);
    }

    return new PrimitiveVariableType(returnType);
  }

  // Checking usage of procedures and functions

  private PrimitiveVariableType checkParameterList(
    Actual_parameter_listContext actualParameterList,
    List<VariableTableEntry> parametersList,
    PrimitiveTypeEnum returnType,
    String entryIdentifier,
    int line,
    ProcedureOrFunctionEnum type
  ) {
    if(actualParameterList == null && parametersList.isEmpty()) {
      return new PrimitiveVariableType(returnType);
    }

    List<Actual_parameterContext> actualParameters = actualParameterList.actual_parameter();

    if(actualParameters.size() != parametersList.size()) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Invalid number of arguments to procedure '%s'.\n",
        line,
        entryIdentifier
      );

      System.exit(1);
    }

    int i = 0;
    for(VariableTableEntry parameter : parametersList) {
      Actual_parameterContext actualParameter = actualParameters.get(i);
      VariableType actualParameterType = visit(actualParameter);

      if(!actualParameterType.isEquivalent(parameter.type)) {
        System.out.printf(
          "SEMANTIC ERROR (%d): Invalid type '%s' for parameter '%s' of %s '%s'.\n",
          line,
          actualParameterType.toString(),
          parameter.identifier,
          type.toString(),
          entryIdentifier
        );

        System.exit(1);
      }

      i++;
    }

    return new PrimitiveVariableType(returnType);
  }

  @Override
  public PrimitiveVariableType visitProcedure_statement(Procedure_statementContext context) {
    TerminalNode identifier = context.IDENTIFIER();
    Actual_parameter_listContext actualParameterList = context.actual_parameter_list();

    BuiltInProceduresAndFunctionsEntry builtInProcedureEntry = builtInProceduresAndFunctionsTable.get(identifier.getText());

    if (builtInProcedureEntry != null) {
      return checkParameterList(
        actualParameterList,
        builtInProcedureEntry.parameters.toList(),
        builtInProcedureEntry.returnType,
        builtInProcedureEntry.identifier,
        identifier.getSymbol().getLine(),
        ProcedureOrFunctionEnum.PROCEDURE
      );
    }

    ProceduresAndFunctionsEntry procedureEntry = proceduresAndFunctionsTable.get(identifier.getText());

    if (procedureEntry == null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Procedure '%s' is not defined.\n",
        identifier.getSymbol().getLine(),
        identifier.getText()
      );
      
      System.exit(1);
    }

    return checkParameterList(
      actualParameterList,
      procedureEntry.parameters.toList(),
      procedureEntry.returnType,
      procedureEntry.identifier,
      identifier.getSymbol().getLine(),
      ProcedureOrFunctionEnum.PROCEDURE
    );
  }

  @Override
  public VariableType visitFunction_designator(Function_designatorContext context) {
    TerminalNode identifier = context.IDENTIFIER();
    Actual_parameter_listContext actualParameterList = context.actual_parameter_list();

    BuiltInProceduresAndFunctionsEntry builtInFunctionEntry = builtInProceduresAndFunctionsTable.get(identifier.getText());
    
    if (builtInFunctionEntry != null) {
    if(builtInFunctionEntry.type == ProcedureOrFunctionEnum.PROCEDURE) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Built-in procedure '%s' is not an expression.\n",
        identifier.getSymbol().getLine(),
        identifier.getText()
      );
      
      System.exit(1);
    }
      
      return checkParameterList(
        actualParameterList,
        builtInFunctionEntry.parameters.toList(),
        builtInFunctionEntry.returnType,
        builtInFunctionEntry.identifier,
        identifier.getSymbol().getLine(),
        ProcedureOrFunctionEnum.FUNCTION
      );
    }

    ProceduresAndFunctionsEntry functionEntry = proceduresAndFunctionsTable.get(identifier.getText());

    if (functionEntry == null) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Function '%s' is not defined.\n",
        identifier.getSymbol().getLine(),
        identifier.getText()
      );
      
      System.exit(1);
    }
    else if(functionEntry.type == ProcedureOrFunctionEnum.PROCEDURE) {
      System.out.printf(
        "SEMANTIC ERROR (%d): Procedure '%s' is not an expression.\n",
        identifier.getSymbol().getLine(),
        identifier.getText()
      );
      
      System.exit(1);
    }

    return checkParameterList(
      actualParameterList,
      functionEntry.parameters.toList(),
      functionEntry.returnType,
      functionEntry.identifier,
      identifier.getSymbol().getLine(),
      ProcedureOrFunctionEnum.FUNCTION
    );
  }

  // Checking usage of literals

  @Override
  public VariableType visitExpression(ExpressionContext context) {
    VariableType leftType = visit(context.simple_expression(0));

    if(context.relational_operator() == null) {
      return leftType;
    }

    VariableType rightType = visit(context.simple_expression(1));

    if(leftType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE || rightType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
      return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
    }

    PrimitiveTypeEnum returnType = TypeRules.getResultType(TypeRules.RELATIONAL_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);

    return new PrimitiveVariableType(returnType);
  }

  @Override
  public VariableType visitSimple_expression(Simple_expressionContext context) {
    VariableType firstType = visit(context.term(0));

    if(context.adding_operator() == null) {
      return firstType;
    }

    int i = 1;
    VariableType leftType = firstType;

    for(Adding_operatorContext operator : context.adding_operator()) {
      VariableType rightType = visit(context.term(i));

      TerminalNode concreteOperator;
      VariableType returnType;

      if(operator.PLUS() != null) {
        concreteOperator = operator.PLUS();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.PLUS_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else if(operator.MINUS() != null) {
        concreteOperator = operator.MINUS();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.MATH_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else {
        concreteOperator = operator.OR();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.LOGICAL_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }

      if(!(leftType instanceof PrimitiveVariableType) || !(rightType instanceof PrimitiveVariableType)) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      if(leftType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE || rightType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
      }

      if(returnType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      leftType = returnType;
      i++;
    }

    return leftType;
  }

  @Override
  public VariableType visitTerm(TermContext context) {
    VariableType firstType = visit(context.factor(0));

    if(context.multiplying_operator() == null) {
      return firstType;
    }

    int i = 1;
    VariableType leftType = firstType;

    for(Multiplying_operatorContext operator : context.multiplying_operator()) {
      VariableType rightType = visit(context.factor(i));

      TerminalNode concreteOperator;
      VariableType returnType;

      if(operator.MULTIPLICATION() != null) {
        concreteOperator = operator.MULTIPLICATION();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.MATH_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else if(operator.DIVISION() != null) {
        concreteOperator = operator.DIVISION();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.REAL_DIVISION_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else if(operator.DIV() != null) {
        concreteOperator = operator.DIV();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.INTEGER_DIVISION_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else {
        concreteOperator = operator.AND();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.LOGICAL_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }

      if(!(leftType instanceof PrimitiveVariableType) || !(rightType instanceof PrimitiveVariableType)) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      if(leftType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE || rightType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
      }

      if(returnType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      leftType = returnType;
      i++;
    }

    return leftType;
  }

  @Override
  public VariableType visitVariableAccess(VariableAccessContext context) {
    return visit(context.variable_access());
  }

  @Override
  public ConstantPrimitiveVariableType<?> visitStringConstant(StringConstantContext context) {
    String stringLiteral = context.CHARACTER_STRING().getText();

    String croppedStringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);

    stringLiteralsTable.addStringLiteral(croppedStringLiteral);

    if(croppedStringLiteral.length() == 1) {
      return new ConstantPrimitiveVariableType<Character>(PrimitiveTypeEnum.CHAR, croppedStringLiteral.charAt(0));
    }

    return new ConstantPrimitiveVariableType<String>(PrimitiveTypeEnum.STRING, croppedStringLiteral);
  }

  @Override
  public ConstantPrimitiveVariableType<?> visitNumeric_constant(Numeric_constantContext context) {
    boolean signal = context.MINUS() != null;
    
    if(context.UNSIGNED_INTEGER() != null) {
      int unsignedInteger = Integer.parseInt(context.UNSIGNED_INTEGER().getText());
      return new ConstantPrimitiveVariableType<Integer>(PrimitiveTypeEnum.INTEGER, signal ? -unsignedInteger : unsignedInteger);
    }

    double unsignedReal = Double.parseDouble(context.UNSIGNED_REAL().getText());
    return new ConstantPrimitiveVariableType<Double>(PrimitiveTypeEnum.REAL, signal ? -unsignedReal : unsignedReal);
  }

  @Override
  public ConstantPrimitiveVariableType<Boolean> visitBoolean_constant(Boolean_constantContext context) {
    if(context.TRUE() != null) {
      return new ConstantPrimitiveVariableType<Boolean>(PrimitiveTypeEnum.BOOLEAN, true);
    }

    return new ConstantPrimitiveVariableType<Boolean>(PrimitiveTypeEnum.BOOLEAN, false);
  }

  @Override
  public ConstantPrimitiveVariableType<?> visitNumericConstant(NumericConstantContext context) {
    Numeric_constantContext numericConstant = context.numeric_constant();
    return (ConstantPrimitiveVariableType<?>) visit(numericConstant);
  }

  @Override
  public ConstantPrimitiveVariableType<Boolean> visitBooleanConstant(BooleanConstantContext context) {
    Boolean_constantContext booleanConstant = context.boolean_constant();
    return visitBoolean_constant(booleanConstant);
  }

  @Override
  public VariableType visitFunctionCall(FunctionCallContext context) {
    Function_designatorContext functionDesignator = context.function_designator();
    return visit(functionDesignator);
  }

  @Override
  public VariableType visitParenthesisExpression(ParenthesisExpressionContext context) {
    ExpressionContext expression = context.expression();
    return visit(expression);
  }

  @Override
  public VariableType visitNotFactor(NotFactorContext context) {
    FactorContext factor = context.factor();

    VariableType returnType = visit(factor);

    if(!(returnType instanceof PrimitiveVariableType && returnType.basePrimitiveType == PrimitiveTypeEnum.BOOLEAN)) {
      TerminalNode not = context.NOT();
      unaryOperationTypeError(not.getSymbol().getLine(), not.getText(), returnType);
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN);
  }

  @Override
  public VariableType visitIf_statement(If_statementContext context) {
    visit(context.statement());

    if(context.else_part() != null) {
      visit(context.else_part());
    }

    VariableType expressionType = visit(context.expression());

    if(!(expressionType instanceof PrimitiveVariableType && expressionType.basePrimitiveType == PrimitiveTypeEnum.BOOLEAN)) {
      System.out.printf(
        "SEMANTIC ERROR (%d): if expression must be boolean.\n",
        context.IF().getSymbol().getLine()
      );

      System.exit(1);
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }
}
