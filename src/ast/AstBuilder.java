package ast;

import java.util.ArrayList;
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
import ast.types.expressions.implementations.ComparisonOperatorExpressionNode;
import ast.types.expressions.implementations.FunctionCallExpressionNode;
import ast.types.expressions.implementations.IndexedVariableAccessExpressionNode;
import ast.types.expressions.implementations.LogicOperatorExpressionNode;
import ast.types.expressions.implementations.NotOperatorExpressionNode;
import ast.types.expressions.implementations.PrimitiveTypeExpressionNode;
import ast.types.expressions.implementations.VariableAccessExpressionNode;
import ast.types.statements.contract.StatementNode;
import ast.types.statements.implementations.AssignmentStatementNode;
import ast.types.statements.implementations.CompoundStatementNode;
import ast.types.statements.implementations.ForStatementNode;
import ast.types.statements.implementations.IfStatementNode;
import ast.types.statements.implementations.ProcedureCallStatementNode;
import org.antlr.v4.runtime.tree.TerminalNode;

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
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class AstBuilder extends PascalParserBaseVisitor<AstNode> {
  private ProgramNode programNode;

  private String programIdentifier;
  private VariablesTable globalVariablesTable;
  private ProceduresAndFunctionsTable proceduresAndFunctionsTable;


  public AstBuilder(
    String programIdentifier,
    VariablesTable globalVariablesTable,
    ProceduresAndFunctionsTable proceduresAndFunctionsTable
  ) {
    this.programIdentifier = programIdentifier;
    this.globalVariablesTable = globalVariablesTable;
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
      .map(AstBuilder::variableTableEntryToAstNode)
      .toList();

    Optional<VariableDeclarationPartNode> variableDeclarationPart = Optional.empty();
    if(!globalVariablesDeclarations.isEmpty()) {
      variableDeclarationPart = Optional.of(new VariableDeclarationPartNode(globalVariablesDeclarations));
    }

    ProcedureAndFunctionDeclarationPartNode procedureAndFunctionDeclarationPartNode = (ProcedureAndFunctionDeclarationPartNode) visit(procedureAndFunctionDeclarations);
    Optional<ProcedureAndFunctionDeclarationPartNode> proceduresAndFunctionsDeclarations = Optional.empty();
    if(!procedureAndFunctionDeclarationPartNode.procedureOrFunctionDeclarations.isEmpty()) {
      proceduresAndFunctionsDeclarations = Optional.of(procedureAndFunctionDeclarationPartNode);
    }

    CompoundStatementNode compoundStatementNode = (CompoundStatementNode) visit(block.compound_statement());

    programNode = new ProgramNode(
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

    return new ProcedureAndFunctionDeclarationPartNode(procedureOrFunctionDeclarationNodes);
  }

  @Override
  public VariableAccessExpressionNode visitVariable_access(Variable_accessContext context) {
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
        return new IndexedVariableAccessExpressionNode(variableIdentifier, globalVariableEntry.type, indexExpressionNode);
      }

      return new VariableAccessExpressionNode(variableIdentifier, globalVariableEntry.type);
    }

    VariableTableEntry procedureOrLocalVariableEntry = proceduresAndFunctionsTable.getParameterOrLocalVariableFromAnyProcedureOrFunction(variableIdentifier);
    if(procedureOrLocalVariableEntry != null) {
      if(procedureOrLocalVariableEntry != null) {
        if(isIndexedVariable) {
          ExpressionNode indexExpressionNode = (ExpressionNode) visit(context.indexed_variable().expression());
          return new IndexedVariableAccessExpressionNode(variableIdentifier, procedureOrLocalVariableEntry.type, indexExpressionNode);
        }

        return new VariableAccessExpressionNode(variableIdentifier, procedureOrLocalVariableEntry.type);
      }
    }

    ProceduresAndFunctionsEntry functionEntry = proceduresAndFunctionsTable.get(variableIdentifier);
    if(functionEntry != null) {
      return new VariableAccessExpressionNode(variableIdentifier, new PrimitiveVariableType(functionEntry.returnType));
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
      .map(AstBuilder::variableTableEntryToAstNode)
      .toList();

    VariableDeclarationPartNode parametersNode = new VariableDeclarationPartNode(parameters);

    List<VariableDeclarationNode> localVariables = entry
      .localVariables
      .toList()
      .stream()
      .map(AstBuilder::variableTableEntryToAstNode)
      .toList();

    VariableDeclarationPartNode localVariablesNode = new VariableDeclarationPartNode(localVariables);

    CompoundStatementNode compoundStatement = (CompoundStatementNode) visit(context.compound_statement());

    return new ProcedureDeclarationNode(
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
      .map(AstBuilder::variableTableEntryToAstNode)
      .toList();

    VariableDeclarationPartNode parametersNode = new VariableDeclarationPartNode(parameters);

    List<VariableDeclarationNode> localVariables = entry
      .localVariables
      .toList()
      .stream()
      .map(AstBuilder::variableTableEntryToAstNode)
      .toList();

    VariableDeclarationPartNode localVariablesNode = new VariableDeclarationPartNode(localVariables);

    CompoundStatementNode compoundStatement = (CompoundStatementNode) visit(context.compound_statement());

    return new FunctionDeclarationNode(
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

    return new ComparisonOperatorExpressionNode(left, right, operator);
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
        returnExpression = new ArithmeticOperatorExpressionNode(leftExpression, rightExpression, "+");
      }
      else if(operator.MINUS() != null) {
        returnExpression = new ArithmeticOperatorExpressionNode(leftExpression, rightExpression, "-");
      }
      else {
        returnExpression = new LogicOperatorExpressionNode(leftExpression, rightExpression, "or");
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
        returnExpression = new ArithmeticOperatorExpressionNode(leftExpression, rightExpression, "*");
      }
      else if(operator.DIVISION() != null) {
        returnExpression = new ArithmeticOperatorExpressionNode(leftExpression, rightExpression, "/");
      }
      else {
        returnExpression = new LogicOperatorExpressionNode(leftExpression, rightExpression, "and");
      }

      leftExpression = returnExpression;
      i++;
    }

    return returnExpression;
  }

  @Override
  public VariableAccessExpressionNode visitVariableAccess(VariableAccessContext context) {
    return (VariableAccessExpressionNode) visit(context.variable_access());
  }

  @Override
  public PrimitiveTypeExpressionNode<String> visitStringConstant(StringConstantContext context) {
    String stringLiteral = context.CHARACTER_STRING().getText();
    return new PrimitiveTypeExpressionNode<>(stringLiteral.substring(1, stringLiteral.length() - 1));
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

      return new PrimitiveTypeExpressionNode<Integer>(value);
    }

    double value = Double.parseDouble(realValue.getText());

    if(minus != null) {
      value = -value;
    }

    return new PrimitiveTypeExpressionNode<Double>(value);
  }

  @Override
  public PrimitiveTypeExpressionNode<?> visitNumericConstant(NumericConstantContext context) {
    Numeric_constantContext numericConstantContext = context.numeric_constant();
    return visitNumeric_constant(numericConstantContext);
  }

  @Override
  public PrimitiveTypeExpressionNode<Boolean> visitBoolean_constant(Boolean_constantContext context) {
    if(context.FALSE() != null) {
      return new PrimitiveTypeExpressionNode<>(false);
    }

    return new PrimitiveTypeExpressionNode<>(true);
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

    List<ExpressionNode> actualParameters = actualParameterList
      .actual_parameter()
      .stream()
      .map(actualParameter -> (ExpressionNode) visit(actualParameter))
      .toList();

    ProceduresAndFunctionsEntry entry = proceduresAndFunctionsTable.get(functionIdentifier);
    PrimitiveTypeEnum returnType = entry.returnType;

    return new FunctionCallExpressionNode(functionIdentifier, actualParameters, returnType);
  }

  @Override
  public ExpressionNode visitParenthesisExpression(ParenthesisExpressionContext context) {
    return (ExpressionNode) visit(context.expression());
  }

  @Override
  public NotOperatorExpressionNode visitNotFactor(NotFactorContext context) {
    ExpressionNode expression = (ExpressionNode) visit(context.factor());
    return new NotOperatorExpressionNode(expression);
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

    return new CompoundStatementNode(statements);
  }

  @Override
  public StatementNode visitEmpty_statement(Empty_statementContext context) {
    return null;
  }

  @Override
  public AssignmentStatementNode visitAssignment_statement(Assignment_statementContext context) {
    Variable_accessContext variableAccess = context.variable_access();

    VariableAccessExpressionNode variableAccessExpressionNode = (VariableAccessExpressionNode) visit(variableAccess);
    ExpressionNode expressionNode = (ExpressionNode) visit(context.expression());

    return new AssignmentStatementNode(variableAccessExpressionNode, expressionNode);
  }

  @Override
  public ProcedureCallStatementNode visitProcedure_statement(Procedure_statementContext context) {
    String procedureIdentifier = context.IDENTIFIER().getText();

    Actual_parameter_listContext actualParameterList = context.actual_parameter_list();

    List<ExpressionNode> actualParameters = actualParameterList
      .actual_parameter()
      .stream()
      .map(actualParameter -> (ExpressionNode) visit(actualParameter))
      .toList();

    return new ProcedureCallStatementNode(procedureIdentifier, actualParameters);
  }

  @Override
  public IfStatementNode visitIf_statement(If_statementContext context) {
    ExpressionNode condition = (ExpressionNode) visit(context.expression());

    StatementNode thenStatement = (StatementNode) visit(context.statement());

    Optional<StatementNode> elseStatement = Optional.empty();
    if(context.else_part() != null) {
      elseStatement = Optional.of((StatementNode) visit(context.else_part().statement()));
    }

    return new IfStatementNode(condition, thenStatement, elseStatement);
  }

  @Override
  public ForStatementNode visitFor_statement(For_statementContext context) {
    String variableIdentifier = context.IDENTIFIER().getText();

    ExpressionNode initialValue = (ExpressionNode) visit(context.expression(0));
    ExpressionNode finalValue = (ExpressionNode) visit(context.expression(1));

    StatementNode statement = (StatementNode) visit(context.statement());

    boolean isDownto = context.DOWNTO() != null;

    return new ForStatementNode(
      variableIdentifier,
      initialValue,
      finalValue,
      isDownto,
      statement
    );
  }

  private static VariableDeclarationNode variableTableEntryToAstNode(VariablesTable.VariableTableEntry entry) {
    return new VariableDeclarationNode(entry.identifier, entry.type);
  }
}
