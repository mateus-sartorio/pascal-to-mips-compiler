package interpreter;

import ast.types.AstNode;
import ast.types.ProgramNode;
import ast.types.expressions.implementations.ArithmeticOperatorExpressionNode;
import ast.types.expressions.implementations.CharToStringExpressionNode;
import ast.types.expressions.implementations.ComparisonOperatorExpressionNode;
import ast.types.expressions.implementations.IntegerToRealExpressionNode;
import ast.types.expressions.implementations.LogicOperatorExpressionNode;
import ast.types.expressions.implementations.PrimitiveTypeExpressionNode;
import ast.types.statements.implementations.AssignmentStatementNode;
import ast.types.statements.implementations.CompoundStatementNode;
import ast.types.statements.implementations.ForStatementNode;
import ast.types.statements.implementations.IfStatementNode;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import types.PrimitiveTypeEnum;

public class Interpreter  {
  private final DataStack dataStack;
  private final Memory memory;
  private final VariablesTable globalVariablesTable;
  private final StringLiteralsMemory stringLiteralsMemory;

  public Interpreter(VariablesTable globalVariablesTable, StringLiteralsTable stringLiteralsTable) {
    this.dataStack = new DataStack();
    this.memory = new Memory(globalVariablesTable);
    this.globalVariablesTable = globalVariablesTable;
    this.stringLiteralsMemory = new StringLiteralsMemory(stringLiteralsTable);
  }

  private void visitProgramNode(ProgramNode node) {
    visit(node.compoundStatement);
  }

  private void visitCompoundStatementNode(CompoundStatementNode node) {
    for(var statement : node.statements) {
      visit(statement);
    }
  }

  private void visitIfStatementNode(IfStatementNode node) {
    visit(node.condition);
    boolean condition = dataStack.popi() == 1;

    if(condition) {
      visit(node.thenStatement);
      return;
    }

    if(node.elseStatement.isPresent()) {
      visit(node.elseStatement.get());
    }
  }

  // TODO : update control variable
  private void visitForStatementNode(ForStatementNode node) {
    visit(node.finalValue);
    visit(node.initialValue);
    
    var initialValue = dataStack.popi();
    var finalValue = dataStack.popi();
    
    boolean isConditionMet;
    do {
      if(node.isDownto) {
        isConditionMet = switch (node.finalValue.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER -> initialValue > finalValue;
          case PrimitiveTypeEnum.CHAR -> initialValue > finalValue;
          case PrimitiveTypeEnum.BOOLEAN -> initialValue > finalValue;
          default -> throw new RuntimeException("Unsupported primitive type");
        };

        initialValue--;
      }
      else {
        isConditionMet = switch (node.finalValue.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER -> initialValue < finalValue;
          case PrimitiveTypeEnum.CHAR -> initialValue < finalValue;
          case PrimitiveTypeEnum.BOOLEAN -> initialValue < finalValue;
          default -> throw new RuntimeException("Unsupported primitive type");
        };

        initialValue++;
      }

      if(isConditionMet) {
        visit(node.body);
      }
    } while(isConditionMet);
  }

  private void visitAssignmentStatementNode(AssignmentStatementNode node) {
    visit(node.expression);

    switch(node.variableAccessExpressionNode.type.basePrimitiveType) {
      case PrimitiveTypeEnum.REAL -> memory.storef(0, dataStack.popf());
      case PrimitiveTypeEnum.INTEGER -> memory.storei(0, dataStack.popi());
      case PrimitiveTypeEnum.BOOLEAN -> memory.storei(0, dataStack.popi());
      case PrimitiveTypeEnum.CHAR -> memory.storei(0, dataStack.popi());
      case PrimitiveTypeEnum.STRING -> throw new RuntimeException("Unsupported primitive type");
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void visitPrimitiveTypeExpressionNode(PrimitiveTypeExpressionNode<?> node) {
    switch (node.value) {
      case Integer value -> dataStack.pushi(value);
      case Double value -> dataStack.pushf((float) (double) value);
      case String value -> dataStack.pushi(stringLiteralsMemory.getIndex(value));
      case Boolean value -> dataStack.pushi(value ? 1 : 0);
      case Character value -> dataStack.pushi((int) (char) value);
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handlePlus(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    switch (node.type.basePrimitiveType) {
      case PrimitiveTypeEnum.REAL -> dataStack.pushf(dataStack.popf() + dataStack.popf());
      case PrimitiveTypeEnum.INTEGER -> dataStack.pushi(dataStack.popi() + dataStack.popi());
      case PrimitiveTypeEnum.CHAR -> dataStack.pushi(stringLiteralsMemory.addEntry(String.valueOf((char) dataStack.popi()) + String.valueOf((char) dataStack.popi())));
      case PrimitiveTypeEnum.STRING -> dataStack.pushi(stringLiteralsMemory.addEntry(stringLiteralsMemory.getEntry(dataStack.popi()) + stringLiteralsMemory.getEntry(dataStack.popi())));
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handleMinus(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    switch (node.type.basePrimitiveType) {
      case PrimitiveTypeEnum.REAL -> dataStack.pushf(dataStack.popf() - dataStack.popf());
      case PrimitiveTypeEnum.INTEGER -> dataStack.pushi(dataStack.popi() - dataStack.popi());
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handleMultiplication(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    switch (node.type.basePrimitiveType) {
      case PrimitiveTypeEnum.REAL -> dataStack.pushf(dataStack.popf() / dataStack.popf());
      case PrimitiveTypeEnum.INTEGER -> dataStack.pushi(dataStack.popi() / dataStack.popi());
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handleDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    switch (node.type.basePrimitiveType) {
      case PrimitiveTypeEnum.REAL -> dataStack.pushf(dataStack.popf() / dataStack.popf());
      case PrimitiveTypeEnum.INTEGER -> dataStack.pushi(dataStack.popi() / dataStack.popi());
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  // private void handleMultiplca

  private void visitArithmeticOperatorExpressionNode(ArithmeticOperatorExpressionNode node) {
    String operator = node.operator;

    switch (operator) {
      case "+" -> handlePlus(node);
      case "-" -> handleMinus(node);
      case "*" -> handleMultiplication(node);
      case "/" -> handleDivision(node);
      default -> throw new RuntimeException("Unsupported operation");
    }
  }

  private void visitLogicOperatorExpressionNode(LogicOperatorExpressionNode node) {
    String operator = node.operator;

    switch (operator) {
      case "and" -> {
        visit(node.left);
        visit(node.right);
        dataStack.pushi((dataStack.popi() == 1 && dataStack.popi() == 1) ? 1 : 0);
      }
      case "or" -> {
        visit(node.left);
        visit(node.right);
        dataStack.pushi((dataStack.popi() == 1 || dataStack.popi() == 1) ? 1 : 0);
      }

      default -> throw new RuntimeException("Unsupported operation");
    }

      
  }

  private void visitIntegerToRealExpressionNode(IntegerToRealExpressionNode node) {
    visit(node.expression);
    int value = dataStack.popi();
    dataStack.pushf((float) value);
  }

  private void visitCharToStringExpressionNode(CharToStringExpressionNode node) {
    visit(node.expression);
    char value = (char) dataStack.popi();
    dataStack.pushi(value);
  }

  private void visitComparisonOperatorExpressionNode(ComparisonOperatorExpressionNode node) {
    visit(node.left);
    visit(node.right);
  }

  public void visit(AstNode node) {
    switch (node) {
      case ProgramNode concreteTypeNode -> visitProgramNode(concreteTypeNode);
      case CompoundStatementNode concreteTypeNode -> visitCompoundStatementNode(concreteTypeNode);
      case IfStatementNode concreteTypeNode -> visitIfStatementNode(concreteTypeNode);
      case ForStatementNode concreteTypeNode -> visitForStatementNode(concreteTypeNode);
      case PrimitiveTypeExpressionNode<?> concreteTypeNode -> visitPrimitiveTypeExpressionNode(concreteTypeNode);
      case LogicOperatorExpressionNode concreteTypeNode -> visitLogicOperatorExpressionNode(concreteTypeNode);
      case IntegerToRealExpressionNode concreteTypeNode -> visitIntegerToRealExpressionNode(concreteTypeNode);
      case CharToStringExpressionNode concreteTypeNode -> visitCharToStringExpressionNode(concreteTypeNode);
      case ComparisonOperatorExpressionNode concreteTypeNode -> visitComparisonOperatorExpressionNode(concreteTypeNode);
      case AssignmentStatementNode concreteTypeNode -> visitAssignmentStatementNode(concreteTypeNode);
      case ArithmeticOperatorExpressionNode concreteTypeNode -> visitArithmeticOperatorExpressionNode(concreteTypeNode);

      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void executeBuiltInProcedureOrFunction(String identifier) {
    switch(identifier) {
      case "write" -> IO.print(dataStack.popi());
    }
  }
}
