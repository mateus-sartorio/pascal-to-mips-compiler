package ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ast.types.AstNode;
import ast.types.ProgramNode;
import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
import ast.types.declarations.implementations.FunctionDeclarationNode;
import ast.types.declarations.implementations.ProcedureAndFunctionDeclarationPartNode;
import ast.types.declarations.implementations.ProcedureDeclarationNode;
import ast.types.declarations.implementations.VariableDeclarationNode;
import ast.types.declarations.implementations.VariableDeclarationPartNode;
import ast.types.expressions.contracts.BinaryOperatorExpressionNode;
import ast.types.expressions.contracts.ExpressionNode;
import ast.types.expressions.implementations.ArithmeticOperatorExpressionNode;
import ast.types.expressions.implementations.CharToStringExpressionNode;
import ast.types.expressions.implementations.ComparisonOperatorExpressionNode;
import ast.types.expressions.implementations.FunctionCallExpressionNode;
import ast.types.expressions.implementations.IndexedVariableAccessExpressionNode;
import ast.types.expressions.implementations.IntegerToRealExpressionNode;
import ast.types.expressions.implementations.LogicOperatorExpressionNode;
import ast.types.expressions.implementations.NotOperatorExpressionNode;
import ast.types.expressions.implementations.PrimitiveTypeExpressionNode;
import ast.types.expressions.implementations.VariableAccessExpressionNode;
import ast.types.expressions.implementations.FunctionReturnAssignmentExpressionNode;
import ast.types.statements.contract.StatementNode;
import ast.types.statements.implementations.AssignmentStatementNode;
import ast.types.statements.implementations.CompoundStatementNode;
import ast.types.statements.implementations.ForStatementNode;
import ast.types.statements.implementations.IfStatementNode;
import ast.types.statements.implementations.ProcedureCallStatementNode;
import ast.types.statements.implementations.ReturnStatementNode;

import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser.Actual_parameterContext;
import parser.PascalParser.Actual_parameter_listContext;
import parser.PascalParser.Adding_operatorContext;
import parser.PascalParser.Assignment_statementContext;
import parser.PascalParser.BlockContext;
import parser.PascalParser.BooleanConstantContext;
import parser.PascalParser.Boolean_constantContext;
import parser.PascalParser.Compound_statementContext;
import parser.PascalParser.Empty_statementContext;
import parser.PascalParser.ExpressionContext;
import parser.PascalParser.For_statementContext;
import parser.PascalParser.FunctionCallContext;
import parser.PascalParser.Function_declarationContext;
import parser.PascalParser.Function_designatorContext;
import parser.PascalParser.Function_headingContext;
import parser.PascalParser.If_statementContext;
import parser.PascalParser.Multiplying_operatorContext;
import parser.PascalParser.NotFactorContext;
import parser.PascalParser.NumericConstantContext;
import parser.PascalParser.Numeric_constantContext;
import parser.PascalParser.ParenthesisExpressionContext;
import parser.PascalParser.Procedure_and_function_declaration_partContext;
import parser.PascalParser.Procedure_declarationContext;
import parser.PascalParser.Procedure_headingContext;
import parser.PascalParser.Procedure_statementContext;
import parser.PascalParser.ProgramContext;
import parser.PascalParser.Relational_operatorContext;
import parser.PascalParser.Simple_expressionContext;
import parser.PascalParser.Statement_sequenceContext;
import parser.PascalParser.StringConstantContext;
import parser.PascalParser.TermContext;
import parser.PascalParser.VariableAccessContext;
import parser.PascalParser.Variable_accessContext;
import parser.PascalParserBaseVisitor;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.BuiltInProceduresAndFunctionsTable.BuiltInProceduresAndFunctionsEntry;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class AstBuilder extends PascalParserBaseVisitor<AstNode> {
  private int currentId = 1;

  private ProgramNode programNode;

  private final String programIdentifier;
  private final VariablesTable globalVariablesTable;
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable;
  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable;

  public AstBuilder(
    String programIdentifier,
    VariablesTable globalVariablesTable,
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable,
    ProceduresAndFunctionsTable proceduresAndFunctionsTable
  ) {
    this.programIdentifier = programIdentifier;
    this.globalVariablesTable = globalVariablesTable;
    this.builtInProceduresAndFunctionsTable = builtInProceduresAndFunctionsTable;
    this.proceduresAndFunctionsTable = proceduresAndFunctionsTable;
  }

	// Imprime a árvore toda em stderr.
	public String toDotNotation() {
    StringBuilder sb = new StringBuilder();

    sb.append("digraph AST {\n");

    sb.append(programNode.toDotNotation());

    sb.append("}\n");

    return sb.toString();
	}

  @Override
  public ProgramNode visitProgram(ProgramContext context) {
    BlockContext block = context.block();

    Procedure_and_function_declaration_partContext procedureAndFunctionDeclarations = block.procedure_and_function_declaration_part();

    List<VariableDeclarationNode> globalVariablesDeclarations = globalVariablesTable
      .toList()
      .stream()
      .map(entry -> variableTableEntryToAstNode(currentId++, entry))
      .toList();

    Optional<VariableDeclarationPartNode> variableDeclarationPart = Optional.empty();
    if(!globalVariablesDeclarations.isEmpty()) {
      variableDeclarationPart = Optional.of(new VariableDeclarationPartNode(currentId++, globalVariablesDeclarations));
    }

    ProcedureAndFunctionDeclarationPartNode procedureAndFunctionDeclarationPartNode = (ProcedureAndFunctionDeclarationPartNode) visit(procedureAndFunctionDeclarations);
    Optional<ProcedureAndFunctionDeclarationPartNode> proceduresAndFunctionsDeclarations = Optional.empty();
    if(!procedureAndFunctionDeclarationPartNode.procedureOrFunctionDeclarations.isEmpty()) {
      proceduresAndFunctionsDeclarations = Optional.of(procedureAndFunctionDeclarationPartNode);
    }

    CompoundStatementNode compoundStatementNode = (CompoundStatementNode) visit(block.compound_statement());

    programNode = new ProgramNode(
      currentId++,
      programIdentifier,
      variableDeclarationPart,
      proceduresAndFunctionsDeclarations,
      compoundStatementNode
    );

    return programNode;
  }

  @Override
  public ProcedureAndFunctionDeclarationPartNode visitProcedure_and_function_declaration_part(Procedure_and_function_declaration_partContext context) {
    List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarationNodes = new ArrayList<>();

    for(Procedure_declarationContext procedureDeclaration : context.procedure_declaration()) {
      procedureOrFunctionDeclarationNodes.add((ProcedureDeclarationNode) visit(procedureDeclaration));
    }

    for(Function_declarationContext functionDeclaration : context.function_declaration()) {
      procedureOrFunctionDeclarationNodes.add((FunctionDeclarationNode) visit(functionDeclaration));
    }

    return new ProcedureAndFunctionDeclarationPartNode(currentId++, procedureOrFunctionDeclarationNodes);
  }

  @Override
  public ExpressionNode visitVariable_access(Variable_accessContext context) {
    String variableIdentifier;
    boolean isIndexedVariable = false;
    if(context.IDENTIFIER() == null) {
      variableIdentifier = context.indexed_variable().IDENTIFIER().getText();
      isIndexedVariable = true;
    } else {
      variableIdentifier = context.IDENTIFIER().getText();
    }

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(variableIdentifier);

    if(globalVariableEntry != null) {
      if(isIndexedVariable) {
        ExpressionNode indexExpressionNode = (ExpressionNode) visit(context.indexed_variable().expression());
        return new IndexedVariableAccessExpressionNode(currentId++, variableIdentifier, globalVariableEntry.type, indexExpressionNode);
      }

      return new VariableAccessExpressionNode(currentId++, variableIdentifier, globalVariableEntry.type);
    }

    VariableTableEntry procedureFunctionParameterOrLocalVariableEntry = proceduresAndFunctionsTable.getParameterOrLocalVariableFromAnyProcedureOrFunction(variableIdentifier);
    if(procedureFunctionParameterOrLocalVariableEntry != null) {
      if(isIndexedVariable) {
        ExpressionNode indexExpressionNode = (ExpressionNode) visit(context.indexed_variable().expression());
        return new IndexedVariableAccessExpressionNode(currentId++, variableIdentifier, procedureFunctionParameterOrLocalVariableEntry.type, indexExpressionNode);
      }

      return new VariableAccessExpressionNode(currentId++, variableIdentifier, procedureFunctionParameterOrLocalVariableEntry.type);
    }

    ProceduresAndFunctionsEntry functionEntry = proceduresAndFunctionsTable.get(variableIdentifier);
    if(functionEntry != null) {
      return new FunctionReturnAssignmentExpressionNode(currentId++, functionEntry.identifier, new PrimitiveVariableType(functionEntry.returnType));
    }

    throw new RuntimeException("Variable " + variableIdentifier + " not found in symbol tables");
  }

  @Override
  public ProcedureDeclarationNode visitProcedure_declaration(Procedure_declarationContext context) {
    Procedure_headingContext procedureHeading = context.procedure_heading();
    String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

    ProceduresAndFunctionsEntry entry = proceduresAndFunctionsTable.get(procedureIdentifier);

    List<VariableDeclarationNode> parameters = entry
      .parameters
      .toList()
      .stream()
      .map(e -> variableTableEntryToAstNode(currentId++, e))
      .toList();

    Optional<VariableDeclarationPartNode> parametersNode;
    if(parameters.isEmpty()) {
      parametersNode = Optional.empty();
    }
    else {
      parametersNode = Optional.of(new VariableDeclarationPartNode(currentId++, parameters));
    }
    

    List<VariableDeclarationNode> localVariables = entry
      .localVariables
      .toList()
      .stream()
      .map(e -> variableTableEntryToAstNode(currentId++, e))
      .toList();

    Optional<VariableDeclarationPartNode> localVariablesNode;
    if(localVariables.isEmpty()) {
      localVariablesNode = Optional.empty();
    }
    else {
      localVariablesNode = Optional.of(new VariableDeclarationPartNode(currentId++, localVariables));
    }

    CompoundStatementNode compoundStatement = (CompoundStatementNode) visit(context.compound_statement());

    return new ProcedureDeclarationNode(
      currentId++,
      procedureIdentifier,
      parametersNode,
      localVariablesNode,
      compoundStatement
    );
  }

  @Override
  public FunctionDeclarationNode visitFunction_declaration(Function_declarationContext context) {
    Function_headingContext procedureHeading = context.function_heading();
    String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

    ProceduresAndFunctionsEntry entry = proceduresAndFunctionsTable.get(procedureIdentifier);
    PrimitiveTypeEnum returnType = entry.returnType;

    List<VariableDeclarationNode> parameters = entry
      .parameters
      .toList()
      .stream()
      .map(e -> variableTableEntryToAstNode(currentId++, e))
      .toList();

    Optional<VariableDeclarationPartNode> parametersNode;
    if(parameters.isEmpty()) {
      parametersNode = Optional.empty();
    }
    else {
      parametersNode = Optional.of(new VariableDeclarationPartNode(currentId++, parameters));
    }

    List<VariableDeclarationNode> localVariables = entry
      .localVariables
      .toList()
      .stream()
      .map(e -> variableTableEntryToAstNode(currentId++, e))
      .toList();
    
    Optional<VariableDeclarationPartNode> localVariablesNode;
    if(localVariables.isEmpty()) {
      localVariablesNode = Optional.empty();
    }
    else {
      localVariablesNode = Optional.of(new VariableDeclarationPartNode(currentId++, localVariables));
    }

    CompoundStatementNode compoundStatement = (CompoundStatementNode) visit(context.compound_statement());

    return new FunctionDeclarationNode(
      currentId++,
      procedureIdentifier,
      parametersNode,
      localVariablesNode,
      returnType,
      compoundStatement
    );
  }

  @Override
  public ExpressionNode visitExpression(ExpressionContext context) {
    if(context.simple_expression().size() == 1) {
      return (ExpressionNode) visit(context.simple_expression(0));
    }

    ExpressionNode left = (ExpressionNode) visit(context.simple_expression(0));
    ExpressionNode right = (ExpressionNode) visit(context.simple_expression(1));

    String operator;
    Relational_operatorContext relationalOperatorContext = context.relational_operator();

    if(relationalOperatorContext.EQUAL_TO() != null) {
      operator = "=";
    }
    else if(relationalOperatorContext.NOT_EQUAL_TO() != null) {
      operator = "<>";
    }
    else if(relationalOperatorContext.LESS_THAN() != null) {
      operator = "<";
    }
    else if(relationalOperatorContext.LESS_THAN_OR_EQUAL_TO() != null) {
      operator = "<=";
    }
    else if(relationalOperatorContext.GREATER_THAN() != null) {
      operator = ">";
    }
    else {
      operator = ">=";
    }

    return new ComparisonOperatorExpressionNode(currentId++, left, right, operator);
  }

  @Override
  public ExpressionNode visitSimple_expression(Simple_expressionContext context) {
    if(context.term().size() == 1) {
      return (ExpressionNode) visit(context.term(0));
    }

    ExpressionNode leftExpression = (ExpressionNode) visit(context.term(0));
    BinaryOperatorExpressionNode returnExpression = null;

    int i = 1;
    for(Adding_operatorContext operator : context.adding_operator()) {
      ExpressionNode rightExpression = (ExpressionNode) visit(context.term(i));

      if(operator.PLUS() != null) {
        returnExpression = new ArithmeticOperatorExpressionNode(currentId++, leftExpression, rightExpression, "+");
      }
      else if(operator.MINUS() != null) {
        returnExpression = new ArithmeticOperatorExpressionNode(currentId++, leftExpression, rightExpression, "-");
      }
      else {
        returnExpression = new LogicOperatorExpressionNode(currentId++, leftExpression, rightExpression, "or");
      }

      leftExpression = returnExpression;
      i++;
    }

    return returnExpression;
  }

  @Override
  public ExpressionNode visitTerm(TermContext context) {
    if(context.factor().size() == 1) {
      return (ExpressionNode) visit(context.factor(0));
    }

    ExpressionNode leftExpression = (ExpressionNode) visit(context.factor(0));
    BinaryOperatorExpressionNode returnExpression = null;

    int i = 1;
    for(Multiplying_operatorContext operator : context.multiplying_operator()) {
      ExpressionNode rightExpression = (ExpressionNode) visit(context.factor(i));

      if(operator.MULTIPLICATION() != null) {
        returnExpression = new ArithmeticOperatorExpressionNode(currentId++, leftExpression, rightExpression, "*");
      }
      else if(operator.DIVISION() != null) {
        returnExpression = new ArithmeticOperatorExpressionNode(currentId++, leftExpression, rightExpression, "/");
      }
      else {
        returnExpression = new LogicOperatorExpressionNode(currentId++, leftExpression, rightExpression, "and");
      }

      leftExpression = returnExpression;
      i++;
    }

    return returnExpression;
  }

  @Override
  public ExpressionNode visitVariableAccess(VariableAccessContext context) {
    return (ExpressionNode) visit(context.variable_access());
  }

  @Override
  public PrimitiveTypeExpressionNode<?> visitStringConstant(StringConstantContext context) {
    String stringLiteral = context.CHARACTER_STRING().getText();
    String croppedStringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);

    if(croppedStringLiteral.length() == 1) {
      return new PrimitiveTypeExpressionNode<Character>(currentId++, croppedStringLiteral.charAt(0));
    }

    return new PrimitiveTypeExpressionNode<String>(currentId++, croppedStringLiteral);
  }

  @Override
  public PrimitiveTypeExpressionNode<?> visitNumeric_constant(Numeric_constantContext context) {
    TerminalNode minus = context.MINUS();

    TerminalNode integerValue = context.UNSIGNED_INTEGER();
    TerminalNode realValue = context.UNSIGNED_REAL();

    if(integerValue != null) {
      int value = Integer.parseInt(integerValue.getText());

      if(minus != null) {
        value = -value;
      }

      return new PrimitiveTypeExpressionNode<Integer>(currentId++, value);
    }

    double value = Double.parseDouble(realValue.getText());

    if(minus != null) {
      value = -value;
    }

    return new PrimitiveTypeExpressionNode<Double>(currentId++, value);
  }

  @Override
  public PrimitiveTypeExpressionNode<?> visitNumericConstant(NumericConstantContext context) {
    Numeric_constantContext numericConstantContext = context.numeric_constant();
    return visitNumeric_constant(numericConstantContext);
  }

  @Override
  public PrimitiveTypeExpressionNode<Boolean> visitBoolean_constant(Boolean_constantContext context) {
    if(context.FALSE() != null) {
      return new PrimitiveTypeExpressionNode<>(currentId++, false);
    }

    return new PrimitiveTypeExpressionNode<>(currentId++, true);
  }

  @Override
  public PrimitiveTypeExpressionNode<Boolean> visitBooleanConstant(BooleanConstantContext context) {
    var booleanConstantContext = context.boolean_constant();
    return visitBoolean_constant(booleanConstantContext);
  }

  @Override
  public FunctionCallExpressionNode visitFunctionCall(FunctionCallContext context) {
    Function_designatorContext functionDesignatorContext = context.function_designator();

    String functionIdentifier = functionDesignatorContext.IDENTIFIER().getText();

    Actual_parameter_listContext actualParameterList = functionDesignatorContext.actual_parameter_list();

    List<ExpressionNode> actualParameters;
    PrimitiveTypeEnum returnType;
    
    BuiltInProceduresAndFunctionsEntry builtInProcedureEntry = builtInProceduresAndFunctionsTable.get(functionIdentifier);
    if(builtInProcedureEntry != null) {
      actualParameters = createActualParameters(actualParameterList, builtInProcedureEntry.parameters.toList());
      returnType = builtInProcedureEntry.returnType;
    }
    else {
      ProceduresAndFunctionsEntry procedureEntry = proceduresAndFunctionsTable.get(functionIdentifier);
      actualParameters = createActualParameters(actualParameterList, procedureEntry.parameters.toList());
      returnType = procedureEntry.returnType;
    }
    
    return new FunctionCallExpressionNode(currentId++, functionIdentifier, actualParameters, returnType);
  }

  @Override
  public ExpressionNode visitParenthesisExpression(ParenthesisExpressionContext context) {
    return (ExpressionNode) visit(context.expression());
  }

  @Override
  public NotOperatorExpressionNode visitNotFactor(NotFactorContext context) {
    ExpressionNode expression = (ExpressionNode) visit(context.factor());
    return new NotOperatorExpressionNode(currentId++, expression);
  }

  @Override
  public CompoundStatementNode visitCompound_statement(Compound_statementContext context) {
    Statement_sequenceContext statementList = context.statement_sequence();

    List<StatementNode> statements = statementList
      .statement()
      .stream()
      .map(statement -> (StatementNode) visit(statement))
      .filter(statement -> statement != null)
      .toList();

    return new CompoundStatementNode(currentId++, statements);
  }

  @Override
  public StatementNode visitEmpty_statement(Empty_statementContext context) {
    return null;
  }

  @Override
  public StatementNode visitAssignment_statement(Assignment_statementContext context) {
    Variable_accessContext variableAccessContext = context.variable_access();

    ExpressionNode variableAccessExpressionNode = (ExpressionNode) visit(variableAccessContext);
    ExpressionNode expressionNode = (ExpressionNode) visit(context.expression());

    ExpressionNode finalExpressionNode = expressionNode;
    
    if(variableAccessExpressionNode.type.basePrimitiveType == PrimitiveTypeEnum.REAL && expressionNode.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      finalExpressionNode = new IntegerToRealExpressionNode(currentId++, expressionNode);
    }
    else if(variableAccessExpressionNode.type.basePrimitiveType == PrimitiveTypeEnum.STRING && expressionNode.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
      finalExpressionNode = new CharToStringExpressionNode(currentId++, expressionNode);
    }
    
    if(variableAccessExpressionNode instanceof FunctionReturnAssignmentExpressionNode) {
      return new ReturnStatementNode(currentId++, finalExpressionNode);
    }

    return new AssignmentStatementNode(currentId++, variableAccessExpressionNode, finalExpressionNode);
  }

  private List<ExpressionNode> createActualParameters(Actual_parameter_listContext actualParameterList, List<VariableTableEntry> argumentsList) {
    List<ExpressionNode> actualParameters = new LinkedList<>();
    
    int i = 0;
    for(Actual_parameterContext actualParameterContext : actualParameterList.actual_parameter()) {
      ExpressionNode actualParameterNode = (ExpressionNode) visit(actualParameterContext);
        
      VariableTableEntry procedureArgumentType = argumentsList.get(i);

      ExpressionNode finalExpressionNode = actualParameterNode;

      if(procedureArgumentType.type.basePrimitiveType == PrimitiveTypeEnum.REAL && actualParameterNode.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        finalExpressionNode = new IntegerToRealExpressionNode(currentId++, actualParameterNode);
      }
      else if(procedureArgumentType.type.basePrimitiveType == PrimitiveTypeEnum.STRING && actualParameterNode.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
        finalExpressionNode = new CharToStringExpressionNode(currentId++, actualParameterNode);
      }

      actualParameters.add(finalExpressionNode);
      i++;
    }

    return actualParameters;
  } 

  @Override
  public ProcedureCallStatementNode visitProcedure_statement(Procedure_statementContext context) {
    String procedureIdentifier = context.IDENTIFIER().getText();

    Actual_parameter_listContext actualParameterList = context.actual_parameter_list();

    List<ExpressionNode> actualParameters;
    
    ProceduresAndFunctionsEntry procedureEntry = proceduresAndFunctionsTable.get(procedureIdentifier);
    BuiltInProceduresAndFunctionsEntry builtInProcedureEntry = builtInProceduresAndFunctionsTable.get(procedureIdentifier);
    
    if(builtInProcedureEntry != null) {
      actualParameters = createActualParameters(actualParameterList, builtInProcedureEntry.parameters.toList());
    }
    else {
      actualParameters = createActualParameters(actualParameterList, procedureEntry.parameters.toList());
    }
    
    return new ProcedureCallStatementNode(currentId++, procedureIdentifier, actualParameters);
  }

  @Override
  public IfStatementNode visitIf_statement(If_statementContext context) {
    ExpressionNode condition = (ExpressionNode) visit(context.expression());

    StatementNode thenStatement = (StatementNode) visit(context.statement());

    Optional<StatementNode> elseStatement = Optional.empty();
    if(context.else_part() != null) {
      elseStatement = Optional.of((StatementNode) visit(context.else_part().statement()));
    }

    return new IfStatementNode(currentId++, condition, thenStatement, elseStatement);
  }

  @Override
  public ForStatementNode visitFor_statement(For_statementContext context) {
    String variableIdentifier = context.IDENTIFIER().getText();

    ExpressionNode beginExpression = (ExpressionNode) visit(context.expression(0));
    ExpressionNode endExpression = (ExpressionNode) visit(context.expression(1));

    PrimitiveVariableType controlVariableType;
    if(beginExpression instanceof PrimitiveTypeExpressionNode initialValue) {
      controlVariableType = switch (initialValue.value) {
        case Integer _ -> new PrimitiveVariableType(PrimitiveTypeEnum.INTEGER);
        case Double _ -> new PrimitiveVariableType(PrimitiveTypeEnum.REAL);
        case Character _ -> new PrimitiveVariableType(PrimitiveTypeEnum.CHAR);
        case Boolean _ -> new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN);
        default -> throw new RuntimeException("Control variable of for statement must be an ordinal type");
      };
    }
    else if(beginExpression instanceof VariableAccessExpressionNode beginExpressionVariable) {
      controlVariableType = new PrimitiveVariableType(beginExpressionVariable.type.basePrimitiveType);
    }
    else {
      throw new RuntimeException("Control variable of for statement must be an ordinal type");
    }

    var controlVariableNode = new VariableDeclarationNode(currentId++, variableIdentifier, controlVariableType);

    StatementNode statement = (StatementNode) visit(context.statement());

    boolean isDownto = context.DOWNTO() != null;

    return new ForStatementNode(
      currentId++,
      controlVariableNode,
      beginExpression,
      endExpression,
      isDownto,
      statement
    );
  }

  private static VariableDeclarationNode variableTableEntryToAstNode(int id, VariablesTable.VariableTableEntry entry) {
    return new VariableDeclarationNode(id, entry.identifier, entry.type);
  }
}
