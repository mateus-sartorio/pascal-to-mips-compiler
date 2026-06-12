package ast;

import java.util.ArrayList;
import java.util.List;

import ast.types.ArithmeticBinaryOperatorExpressionNode;
import ast.types.AssignmentStatementNode;
import ast.types.AstNode;
import ast.types.BinaryOperatorExpressionNode;
import ast.types.BooleanExpressionNode;
import ast.types.ComparisonOperatorExpression;
import ast.types.CompoundStatementNode;
import ast.types.ExpressionNode;
import ast.types.ForStatementNode;
import ast.types.FunctionDeclarationNode;
import ast.types.IfStatementStatementNode;
import ast.types.IntegerExpressionNode;
import ast.types.LogicOperatorExpressionNode;
import ast.types.ProcedureAndFunctionDeclarationPartNode;
import ast.types.ProcedureCallStatementNode;
import ast.types.FunctionCallExpressionNode;
import ast.types.ProcedureDeclarationNode;
import ast.types.ProcedureOrFunctionDeclarationNode;
import ast.types.ProgramNode;
import ast.types.RealExpressionNode;
import ast.types.StatementNode;
import ast.types.StringExpressionNode;
import ast.types.VariableDeclarationNode;
import parser.PascalParser.Assignment_statementContext;
import parser.PascalParser.Compound_statementContext;
import parser.PascalParser.Empty_statementContext;
import parser.PascalParser.ExpressionContext;
import parser.PascalParser.For_statementContext;
import parser.PascalParser.FunctionCallContext;
import parser.PascalParser.Function_declarationContext;
import parser.PascalParser.If_statementContext;
import parser.PascalParser.NotFactorContext;
import parser.PascalParser.NumericConstantContext;
import parser.PascalParser.ParenthesisExpressionContext;
import parser.PascalParser.Procedure_and_function_declaration_partContext;
import parser.PascalParser.Procedure_declarationContext;
import parser.PascalParser.Procedure_statementContext;
import parser.PascalParser.ProgramContext;
import parser.PascalParser.Simple_expressionContext;
import parser.PascalParser.StringConstantContext;
import parser.PascalParser.TermContext;
import parser.PascalParser.VariableAccessContext;
import parser.PascalParserBaseVisitor;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable;
import tables.StringLiteralsTable;
import tables.VariablesTable;

public class AstBuilderVisitor extends PascalParserBaseVisitor<AstNode> {
  // Table to store string literals found in the code

  private StringLiteralsTable stringLiteralsTable;

  // Symbol table to store pre-declared procedures and functions and their parameters
  private BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable;

  // Symbol table to store variables declared in the code
  private VariablesTable globalVariablesTable;

  // Symbol table to store declared procedures and functions, their local variables and parameters
  private ProceduresAndFunctionsTable proceduresAndFunctionsTable;

  private ProgramNode programNode;
  
  public AstBuilderVisitor(
    StringLiteralsTable stringLiteralsTable,
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable,
    VariablesTable globalVariablesTable,
    ProceduresAndFunctionsTable proceduresAndFunctionsTable
  ) {
    this.stringLiteralsTable = stringLiteralsTable;
    this.builtInProceduresAndFunctionsTable = builtInProceduresAndFunctionsTable;
    this.globalVariablesTable = globalVariablesTable;
    this.proceduresAndFunctionsTable = proceduresAndFunctionsTable;
  }

  @Override
  public ProgramNode visitProgram(ProgramContext context) {
    var block = context.block();

    var procedureAndFunctionDeclarations = block.procedure_and_function_declaration_part();

    var globalVariablesDeclarations = globalVariablesTable
      .toList()
      .stream()
      .map(AstBuilderVisitor::variableTableEntryToAstNode)
      .toList();
    
    var proceduresAndFunctionsDeclarations = (ProcedureAndFunctionDeclarationPartNode) visit(procedureAndFunctionDeclarations);

    var compoundStatement = block.compound_statement();

    var compoundStatementNode = (CompoundStatementNode) visit(compoundStatement);

    programNode = new ProgramNode(globalVariablesDeclarations, proceduresAndFunctionsDeclarations, compoundStatementNode);
    
    return programNode;
  }

  @Override
  public ProcedureAndFunctionDeclarationPartNode visitProcedure_and_function_declaration_part(Procedure_and_function_declaration_partContext context) {
    List<ProcedureOrFunctionDeclarationNode> procedureOrFunctionDeclarationNodes = new ArrayList<>();
    
    for(var procedureDeclaration : context.procedure_declaration()) {
      procedureOrFunctionDeclarationNodes.add((ProcedureDeclarationNode) visit(procedureDeclaration));
    }

    for(var functionDeclaration : context.function_declaration()) {
      procedureOrFunctionDeclarationNodes.add((FunctionDeclarationNode) visit(functionDeclaration));
    }

    return new ProcedureAndFunctionDeclarationPartNode(procedureOrFunctionDeclarationNodes);
  }

  @Override
  public ProcedureDeclarationNode visitProcedure_declaration(Procedure_declarationContext context) {
    var procedureHeading = context.procedure_heading();
    var procedureIdentifier = procedureHeading.IDENTIFIER().getText();

    var entry = proceduresAndFunctionsTable.get(procedureIdentifier);

    var parameters = entry
      .parameters
      .toList()
      .stream()
      .map(AstBuilderVisitor::variableTableEntryToAstNode)
      .toList();
    
    var localVariables = entry
      .localVariables
      .toList()
      .stream()
      .map(AstBuilderVisitor::variableTableEntryToAstNode)
      .toList();

    var compoundStatement = (CompoundStatementNode) visit(context.compound_statement());

    return new ProcedureDeclarationNode(
      procedureIdentifier,
      parameters,
      localVariables,
      compoundStatement
    );
  }

  @Override
  public FunctionDeclarationNode visitFunction_declaration(Function_declarationContext context) {
    var procedureHeading = context.function_heading();
    var procedureIdentifier = procedureHeading.IDENTIFIER().getText();
    
    var entry = proceduresAndFunctionsTable.get(procedureIdentifier);
    var returnType = entry.returnType;

    var parameters = entry
      .parameters
      .toList()
      .stream()
      .map(AstBuilderVisitor::variableTableEntryToAstNode)
      .toList();
    
    var localVariables = entry
      .localVariables
      .toList()
      .stream()
      .map(AstBuilderVisitor::variableTableEntryToAstNode)
      .toList();

    var compoundStatement = (CompoundStatementNode) visit(context.compound_statement());

    return new FunctionDeclarationNode(
      procedureIdentifier,
      parameters,
      localVariables,
      returnType,
      compoundStatement
    );
  }

  @Override
  public ExpressionNode visitExpression(ExpressionContext context) {
    if(context.simple_expression().size() == 1) {
      return (ExpressionNode) visit(context.simple_expression(0));
    }

    var left = (ExpressionNode) visit(context.simple_expression(0));
    var right = (ExpressionNode) visit(context.simple_expression(1));

    String operator;
    var relationalOperatorContext = context.relational_operator();
    
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
    else if(relationalOperatorContext.GREATER_THAN_OR_EQUAL_TO() != null) {
      operator = ">=";
    }
    else {
      throw new RuntimeException("Unknown operator");
    }

    return new ComparisonOperatorExpression(left, right, operator);
  }

  @Override
  public ExpressionNode visitSimple_expression(Simple_expressionContext context) {
    if(context.term().size() == 1) {
      return (ExpressionNode) visit(context.term(0));
    }

    var rightExpression = (ExpressionNode) visit(context.term(0));
    BinaryOperatorExpressionNode returnExpression = null;
    
    int i = 1;
    for(var operator : context.adding_operator()) {
      var leftExpression = (ExpressionNode) visit(context.term(i));

      if(operator.PLUS() != null) {
        returnExpression = new ArithmeticBinaryOperatorExpressionNode(leftExpression, rightExpression, "+");
      }
      else if(operator.MINUS() != null) {
        returnExpression = new ArithmeticBinaryOperatorExpressionNode(leftExpression, rightExpression, "-");
      }
      else {
        returnExpression = new LogicOperatorExpressionNode(leftExpression, rightExpression, "or");
      }

      rightExpression = returnExpression;
      i++;
    }

    return returnExpression;
  }

  @Override
  public ExpressionNode visitTerm(TermContext context) {
    if(context.factor().size() == 1) {
      return (ExpressionNode) visit(context.factor(0));
    }

    var rightExpression = (ExpressionNode) visit(context.factor(0));
    BinaryOperatorExpressionNode returnExpression = null;
    
    int i = 1;
    for(var operator : context.multiplying_operator()) {
      var leftExpression = (ExpressionNode) visit(context.factor(i));

      if(operator.MULTIPLICATION() != null) {
        returnExpression = new ArithmeticBinaryOperatorExpressionNode(leftExpression, rightExpression, "*");
      }
      else if(operator.DIVISION() != null) {
        returnExpression = new ArithmeticBinaryOperatorExpressionNode(leftExpression, rightExpression, "/");
      }
      else {
        returnExpression = new LogicOperatorExpressionNode(leftExpression, rightExpression, "and");
      }

      rightExpression = returnExpression;
      i++;
    }

    return returnExpression;
  }

  @Override
  public ExpressionNode visitVariableAccess(VariableAccessContext context) {
    var variableAccess = context.variable_access();

    String variableIdentifier;
    boolean isIndexedVariable = false;
    if(variableAccess.IDENTIFIER() == null) {
      variableIdentifier = variableAccess.indexed_variable().IDENTIFIER().getText();
      isIndexedVariable = true;
    } else {
      variableIdentifier = variableAccess.IDENTIFIER().getText();
    }

    
  }

  @Override
  public StringExpressionNode visitStringConstant(StringConstantContext context) {
    var stringLiteral = context.CHARACTER_STRING().getText();
    return new StringExpressionNode(stringLiteral.substring(1, stringLiteral.length() - 1));
  }

  @Override
  public ExpressionNode visitNumericConstant(NumericConstantContext context) {
    var numericConstantContext = context.numeric_constant();

    var minus = numericConstantContext.MINUS();

    var integerValue = numericConstantContext.UNSIGNED_INTEGER();
    var realValue = numericConstantContext.UNSIGNED_REAL();

    if(integerValue != null) {
      int value = Integer.parseInt(integerValue.getText());

      if(minus != null) {
        value = -value;
      }

      return new IntegerExpressionNode(value);
    }

    double value = Double.parseDouble(realValue.getText());

    if(minus != null) {
      value = -value;
    }

    return new RealExpressionNode(value);
  }

  @Override
  public FunctionCallExpressionNode visitFunctionCall(FunctionCallContext context) {
    var functionDesignatorContext = context.function_designator();

    var functionIdentifier = functionDesignatorContext.IDENTIFIER().getText();

    var actualParameterList = functionDesignatorContext.actual_parameter_list();
    
    var actualParameters = actualParameterList
      .actual_parameter()
      .stream()
      .map(actualParameter -> (ExpressionNode) visit(actualParameter))
      .toList();

    var entry = proceduresAndFunctionsTable.get(functionIdentifier);
    var returnType = entry.returnType;

    return new FunctionCallExpressionNode(functionIdentifier, actualParameters, returnType);
  }

  @Override
  public ExpressionNode visitParenthesisExpression(ParenthesisExpressionContext context) {
    return (ExpressionNode) visit(context.expression());
  }

  @Override
  public BooleanExpressionNode visitNotFactor(NotFactorContext context) {
    var factor = (BooleanExpressionNode) visit(context.factor());
    return new BooleanExpressionNode(!factor.value);
  }

  @Override
  public CompoundStatementNode visitCompound_statement(Compound_statementContext context) {
    var statementList = context.statement_sequence();

    var statements = statementList
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
    var variableAccess = context.variable_access();

    // TODO: deal with array types

    String variableIdentifier;
    boolean isIndexedVariable = false;
    if(variableAccess.IDENTIFIER() == null) {
      variableIdentifier = variableAccess.indexed_variable().IDENTIFIER().getText();
      isIndexedVariable = true;
    } else {
      variableIdentifier = variableAccess.IDENTIFIER().getText();
    }

    var expression = (ExpressionNode) visit(context.expression());

    return new AssignmentStatementNode(variableIdentifier, expression); 
  }

  @Override
  public ProcedureCallStatementNode visitProcedure_statement(Procedure_statementContext context) {
    var procedureIdentifier = context.IDENTIFIER().getText();

    var actualParameterList = context.actual_parameter_list();
    
    var actualParameters = actualParameterList
      .actual_parameter()
      .stream()
      .map(actualParameter -> (ExpressionNode) visit(actualParameter))
      .toList();

    return new ProcedureCallStatementNode(procedureIdentifier, actualParameters);
  }

  @Override
  public IfStatementStatementNode visitIf_statement(If_statementContext context) {
    var condition = (ExpressionNode) visit(context.expression());

    var thenStatement = (CompoundStatementNode) visit(context.statement());

    CompoundStatementNode elseStatement = null;
    if(context.else_part() != null) {
      elseStatement = (CompoundStatementNode) visit(context.else_part().statement());
    }

    return new IfStatementStatementNode(condition, thenStatement, elseStatement);
  }

  @Override
  public ForStatementNode visitFor_statement(For_statementContext context) {
    var variableIdentifier = context.IDENTIFIER().getText();

    var initialValue = (ExpressionNode) visit(context.expression(1));
    var finalValue = (ExpressionNode) visit(context.expression(2));

    var compoundStatement = (CompoundStatementNode) visit(context.statement());

    boolean isDownto = context.DOWNTO() != null;

    return new ForStatementNode(
      variableIdentifier,
      initialValue,
      finalValue,
      isDownto,
      compoundStatement
    );
  }

  private static VariableDeclarationNode variableTableEntryToAstNode(VariablesTable.VariableTableEntry entry) {
    return new VariableDeclarationNode(entry.identifier, entry.type);
  }
}
