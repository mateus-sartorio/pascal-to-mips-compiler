package interpreter;

import ast.types.AstNode;
import ast.types.ProgramNode;
import ast.types.declarations.contracts.ProcedureOrFunctionDeclarationNode;
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
import ast.types.statements.implementations.AssignmentStatementNode;
import ast.types.statements.implementations.CompoundStatementNode;
import ast.types.statements.implementations.ForStatementNode;
import ast.types.statements.implementations.IfStatementNode;
import ast.types.statements.implementations.ProcedureCallStatementNode;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.VariablesTable.VariableTableEntry;
import types.ArrayVariableType;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class Interpreter  {
  private final VariablesTable globalVariablesTable;
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable;
  private final StringLiteralsMemory stringLiteralsMemory;
  private final DataStack dataStack;
  private final Memory globalVariablesMemory;
  private final MemoryStack memoryStack;

  private final ProgramNode programNode;

  public Interpreter(
    VariablesTable globalVariablesTable,
    StringLiteralsTable stringLiteralsTable,
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable,
    ProceduresAndFunctionsTable proceduresAndFunctionsTable,
    ProgramNode programNode
  ) {
    this.globalVariablesTable = globalVariablesTable;
    this.builtInProceduresAndFunctionsTable = builtInProceduresAndFunctionsTable;
    this.stringLiteralsMemory = new StringLiteralsMemory(stringLiteralsTable);
    this.dataStack = new DataStack();
    this.globalVariablesMemory = new Memory(globalVariablesTable);
    this.memoryStack = new MemoryStack(proceduresAndFunctionsTable);

    this.programNode = programNode;
  }

  public void execute() {
    visit(this.programNode);
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
    boolean condition = dataStack.popInteger() == 1;

    if(condition) {
      visit(node.thenStatement);
      return;
    }

    if(node.elseStatement.isPresent() && !condition) {
      visit(node.elseStatement.get());
    }
  }

  private void visitForStatementNode(ForStatementNode node) {
    visit(node.finalValue);
    visit(node.initialValue);
    
    String controlVariable = node.controlVariable.identifier;

    int initialValue = dataStack.popInteger();
    int finalValue = dataStack.popInteger();
    
    boolean isConditionMet;
    do {
      if(node.isDownto) {
        isConditionMet = switch (node.finalValue.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER -> initialValue > finalValue;
          case PrimitiveTypeEnum.CHAR -> initialValue > finalValue;
          case PrimitiveTypeEnum.BOOLEAN -> initialValue > finalValue;
          default -> throw new RuntimeException("Unsupported primitive type");
        };

        globalVariablesMemory.storeInteger(controlVariable, initialValue);
        initialValue--;
      }
      else {
        isConditionMet = switch (node.finalValue.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER -> initialValue < finalValue;
          case PrimitiveTypeEnum.CHAR -> initialValue < finalValue;
          case PrimitiveTypeEnum.BOOLEAN -> initialValue < finalValue;
          default -> throw new RuntimeException("Unsupported primitive type");
        };

        globalVariablesMemory.storeInteger(controlVariable, initialValue);
        initialValue++;
      }

      if(isConditionMet) {
        visit(node.body);
      }
    }
    while(isConditionMet);
  }

  private void visitAssignmentStatementNode(AssignmentStatementNode node) {
    visit(node.expression);

    String variableIdentifier = node.variableAccessExpressionNode.identifier;

    Memory correctMemory;
    VariablesTable correctTable;

    if(globalVariablesTable.lookupVariable(variableIdentifier)) {
      correctMemory = globalVariablesMemory;
      correctTable = globalVariablesTable;
    }
    else {
      correctMemory = memoryStack.peek();
      correctTable = memoryStack.peekTable();
    }

    if(node.variableAccessExpressionNode instanceof IndexedVariableAccessExpressionNode) {
      var size = correctMemory.entryOf(variableIdentifier).size();
        
      if(node.variableAccessExpressionNode instanceof IndexedVariableAccessExpressionNode iven) {
        visit(iven.indexExpressionNode);
        var poppedInteger = dataStack.popInteger();

        var variableValue = correctTable.get(variableIdentifier);
        var arrayVariableType = (ArrayVariableType) variableValue.type;
        int offset = poppedInteger - arrayVariableType.startIndex;

        switch(node.variableAccessExpressionNode.type.basePrimitiveType) {
          case PrimitiveTypeEnum.REAL -> correctMemory.storeFloatAt(variableIdentifier, dataStack.popFloat(), offset);
          case PrimitiveTypeEnum.INTEGER -> correctMemory.storeIntegerAt(variableIdentifier, dataStack.popInteger(), offset);
          case PrimitiveTypeEnum.BOOLEAN -> correctMemory.storeIntegerAt(variableIdentifier, dataStack.popInteger(), offset);
          case PrimitiveTypeEnum.CHAR -> correctMemory.storeIntegerAt(variableIdentifier, dataStack.popInteger(), offset);
          case PrimitiveTypeEnum.STRING -> correctMemory.storeIntegerAt(variableIdentifier, dataStack.popInteger(), offset);
          default -> throw new RuntimeException("Unsupported primitive type");
        }
      }
      else {
        switch(node.variableAccessExpressionNode.type.basePrimitiveType) {
          case PrimitiveTypeEnum.REAL -> correctMemory.storeFloatArray(variableIdentifier, dataStack.popFloatArray(size));
          case PrimitiveTypeEnum.INTEGER -> correctMemory.storeIntegerArray(variableIdentifier, dataStack.popIntegerArray(size));
          case PrimitiveTypeEnum.BOOLEAN -> correctMemory.storeIntegerArray(variableIdentifier, dataStack.popIntegerArray(size));
          case PrimitiveTypeEnum.CHAR -> correctMemory.storeIntegerArray(variableIdentifier, dataStack.popIntegerArray(size));
          case PrimitiveTypeEnum.STRING -> correctMemory.storeIntegerArray(variableIdentifier, dataStack.popIntegerArray(size));
          default -> throw new RuntimeException("Unsupported primitive type");
        }
      }
    }
    else {
      switch(node.variableAccessExpressionNode.type.basePrimitiveType) {
        case PrimitiveTypeEnum.REAL -> correctMemory.storeFloat(variableIdentifier, dataStack.popFloat());
        case PrimitiveTypeEnum.INTEGER -> correctMemory.storeInteger(variableIdentifier, dataStack.popInteger());
        case PrimitiveTypeEnum.BOOLEAN -> correctMemory.storeInteger(variableIdentifier, dataStack.popInteger());
        case PrimitiveTypeEnum.CHAR -> correctMemory.storeInteger(variableIdentifier, dataStack.popInteger());
        case PrimitiveTypeEnum.STRING -> correctMemory.storeInteger(variableIdentifier, dataStack.popInteger());
        default -> throw new RuntimeException("Unsupported primitive type");
      }
    }
  }

  private void visitPrimitiveTypeExpressionNode(PrimitiveTypeExpressionNode<?> node) {
    switch (node.value) {
      case Integer value -> dataStack.pushInteger(value);
      case Double value -> dataStack.pushFloat((float) (double) value);
      case String value -> {
        var stringLiteralIndex = stringLiteralsMemory.indexOf(value);
        dataStack.pushInteger(stringLiteralIndex);
      }
      case Boolean value -> dataStack.pushInteger(value ? 1 : 0);
      case Character value -> dataStack.pushInteger((int) (char) value);
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handlePlus(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushInteger(dataStack.popInteger() + dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popInteger() + dataStack.popFloat());
      }
    }
    else if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushFloat(dataStack.popFloat() + dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popFloat() + dataStack.popFloat());
      }
    }
    else if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
        dataStack.pushInteger(stringLiteralsMemory.addEntry(String.valueOf((char) dataStack.popInteger()) + String.valueOf((char) dataStack.popInteger())));
      }
      else {
        var left = dataStack.popInteger();
        String leftCharToString = String.valueOf((char) left);
        var right = dataStack.popInteger();
        String stringLiteralEntry = stringLiteralsMemory.getEntry(right); 
        String finalString = leftCharToString + stringLiteralEntry;

        dataStack.pushInteger(stringLiteralsMemory.addEntry(finalString));
      }
    }
    else if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
        var rightIndex = dataStack.popInteger();
        var leftIndex = dataStack.popInteger();
        String rightString = stringLiteralsMemory.getEntry(rightIndex);
        String leftString = stringLiteralsMemory.getEntry(leftIndex);
        var resultIndex = stringLiteralsMemory.addEntry(rightString + leftString);
        dataStack.pushInteger(resultIndex);
      }
      else {
        var left = dataStack.popInteger();
        String str1 = stringLiteralsMemory.getEntry(left); 
        var right = dataStack.popInteger();
        String str2 = String.valueOf((char) right);
        String finalString = str1 + str2;

        dataStack.pushInteger(stringLiteralsMemory.addEntry(finalString));
      }
    }
    else {
      throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handleMinus(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushInteger(dataStack.popInteger() - dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popInteger() - dataStack.popFloat());
      }
    }
    else {
      if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushFloat(dataStack.popFloat() - dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popFloat() - dataStack.popFloat());
      }
    }
  }

  private void handleMultiplication(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushInteger(dataStack.popInteger() * dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popInteger() * dataStack.popFloat());
      }
    }
    else {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushFloat(dataStack.popFloat() * dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popFloat() * dataStack.popFloat());
      }
    }
  }

  private void handleRealDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushFloat(dataStack.popInteger() / dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popInteger() / dataStack.popFloat());
      }
    }
    else {
      if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        dataStack.pushFloat(dataStack.popFloat() / dataStack.popInteger());
      }
      else {
        dataStack.pushFloat(dataStack.popFloat() / dataStack.popFloat());
      }
    }
  }

  private void handleIntegerDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    dataStack.pushInteger(dataStack.popInteger() / dataStack.popInteger());
  }

  private void visitArithmeticOperatorExpressionNode(ArithmeticOperatorExpressionNode node) {
    String operator = node.operator;

    switch (operator) {
      case "+" -> handlePlus(node);
      case "-" -> handleMinus(node);
      case "*" -> handleMultiplication(node);
      case "/" -> handleRealDivision(node);
      case "div" -> handleIntegerDivision(node);
      default -> throw new RuntimeException("Unsupported operation");
    }
  }
  
  private void visitIndexedVariableAccessExpressionNode(IndexedVariableAccessExpressionNode node) {
    Memory correctMemory;
    VariablesTable correctTable;

    if(globalVariablesTable.lookupVariable(node.identifier)) {
      correctMemory = globalVariablesMemory;
      correctTable = globalVariablesTable;
    }
    else {
      correctMemory = memoryStack.peek();
      correctTable = memoryStack.peekTable();
    }

    visit(node.indexExpressionNode);
    int index = dataStack.popInteger();
    
    if(node.type instanceof PrimitiveVariableType && node.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
      int strIndex = correctMemory.loadInteger(node.identifier);
      String str = stringLiteralsMemory.getEntry(strIndex);
      char c = str.charAt(index);
      dataStack.pushInteger(c);
      return;
    }

    VariableTableEntry symbol = correctTable.get(node.identifier);
    ArrayVariableType ArrayType = (ArrayVariableType) symbol.type;
    int offset = index - ArrayType.startIndex;

    switch (node.type.basePrimitiveType) {
      case PrimitiveTypeEnum.INTEGER -> dataStack.pushInteger(correctMemory.loadIntegerAt(node.identifier, offset));
      case PrimitiveTypeEnum.REAL -> dataStack.pushFloat(correctMemory.loadFloatAt(node.identifier, offset));
      case PrimitiveTypeEnum.CHAR -> dataStack.pushInteger(correctMemory.loadIntegerAt(node.identifier, offset));
      case PrimitiveTypeEnum.BOOLEAN -> dataStack.pushInteger(correctMemory.loadIntegerAt(node.identifier, offset));
      case PrimitiveTypeEnum.STRING -> dataStack.pushInteger(correctMemory.loadIntegerAt(node.identifier, offset));
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void visitVariableAccessExpressionNode(VariableAccessExpressionNode node) {
    Memory correctMemory;

    if(globalVariablesTable.lookupVariable(node.identifier)) {
      correctMemory = globalVariablesMemory;
    }
    else {
      correctMemory = memoryStack.peek();
    }

    switch (node.type) {
      case PrimitiveVariableType _ -> {
        switch (node.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER -> dataStack.pushInteger(correctMemory.loadInteger(node.identifier));
          case PrimitiveTypeEnum.REAL -> dataStack.pushFloat(correctMemory.loadFloat(node.identifier));
          case PrimitiveTypeEnum.CHAR -> dataStack.pushInteger(correctMemory.loadInteger(node.identifier));
          case PrimitiveTypeEnum.BOOLEAN -> dataStack.pushInteger(correctMemory.loadInteger(node.identifier));
          case PrimitiveTypeEnum.STRING -> dataStack.pushInteger(correctMemory.loadInteger(node.identifier));
          default -> throw new RuntimeException("Unsupported primitive type");
        }
      }
      case ArrayVariableType _ -> {
        switch (node.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER -> dataStack.pushIntegerArray(correctMemory.loadIntegerArray(node.identifier));
          case PrimitiveTypeEnum.REAL -> dataStack.pushFloatArray(correctMemory.loadFloatArray(node.identifier));
          case PrimitiveTypeEnum.CHAR -> dataStack.pushIntegerArray(correctMemory.loadIntegerArray(node.identifier));
          case PrimitiveTypeEnum.BOOLEAN -> dataStack.pushIntegerArray(correctMemory.loadIntegerArray(node.identifier));
          case PrimitiveTypeEnum.STRING -> dataStack.pushIntegerArray(correctMemory.loadIntegerArray(node.identifier));
          default -> throw new RuntimeException("Unsupported primitive type of array type");
        }
      }
      default -> throw new RuntimeException("Unsupported type");
    }
  }

  private void visitLogicOperatorExpressionNode(LogicOperatorExpressionNode node) {
    String operator = node.operator;

    switch (operator) {
      case "and" -> {
        visit(node.right);
        visit(node.left);
        dataStack.pushInteger((dataStack.popInteger() == 1 && dataStack.popInteger() == 1) ? 1 : 0);
      }
      case "or" -> {
        visit(node.right);
        visit(node.left);
        dataStack.pushInteger((dataStack.popInteger() == 1 || dataStack.popInteger() == 1) ? 1 : 0);
      }
      default -> throw new RuntimeException("Unsupported operation");
    }
  }

  private void visitNotOperatorExpressionNode(NotOperatorExpressionNode node) {
    visit(node.expression);
    dataStack.pushInteger(dataStack.popInteger() == 1 ? 0 : 1);
  }

  private void visitIntegerToRealExpressionNode(IntegerToRealExpressionNode node) {
    visit(node.expression);
    int value = dataStack.popInteger();
    dataStack.pushFloat((float) value);
  }

  private void visitCharToStringExpressionNode(CharToStringExpressionNode node) {
    visit(node.expression);
    char value = (char) dataStack.popInteger();
    dataStack.pushInteger(stringLiteralsMemory.addEntry(String.valueOf(value)));
  }

  private void visitComparisonOperatorExpressionNode(ComparisonOperatorExpressionNode node) {
    String operator = node.operator;

    visit(node.left);
    visit(node.right);
    
    int rightValue = dataStack.popInteger();
    int leftValue = dataStack.popInteger();
    
    switch (operator) {
      case "="  -> dataStack.pushInteger(leftValue == rightValue ? 1 : 0);
      case "<>" -> dataStack.pushInteger(leftValue != rightValue ? 1 : 0);
      case "<"  -> dataStack.pushInteger(leftValue < rightValue ? 1 : 0);
      case ">"  -> dataStack.pushInteger(leftValue > rightValue ? 1 : 0);
      case "<=" -> dataStack.pushInteger(leftValue <= rightValue ? 1 : 0);
      case ">=" -> dataStack.pushInteger(leftValue >= rightValue ? 1 : 0);
      default   -> throw new RuntimeException("Unsupported operation");
    };
  }

  private void visitProcedureCallStatementNode(ProcedureCallStatementNode node) {
    if(builtInProceduresAndFunctionsTable.lookProcedureOrFunction(node.procedureIdentifier)) {
      for(var argument : node.arguments) {
        visit(argument);
      }
      
      executeBuiltInProcedureOrFunction(node.procedureIdentifier);
      return;
    }

    for(var argument : node.arguments) {
      visit(argument);
    }

    memoryStack.pushFrame(node.procedureIdentifier);
    Memory currentMemory = memoryStack.peek();

    ProcedureOrFunctionDeclarationNode procedureDeclaration = programNode.getDeclaration(node.procedureIdentifier);

    if(procedureDeclaration.parameters.isPresent()) {
      var parameters = procedureDeclaration.parameters.get().variables;
      for (int i = parameters.size() - 1; i >= 0; i--) {
        var parameter = parameters.get(i);
        
        if(parameter.type instanceof ArrayVariableType ap) {
          var size = ap.endIndex - ap.startIndex - 1;

          switch(ap.basePrimitiveType) {
            case PrimitiveTypeEnum.REAL -> currentMemory.storeFloatArray(parameter.identifier, dataStack.popFloatArray(size));
            case PrimitiveTypeEnum.INTEGER -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            case PrimitiveTypeEnum.BOOLEAN -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            case PrimitiveTypeEnum.CHAR -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            case PrimitiveTypeEnum.STRING -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            default -> throw new RuntimeException("Unsupported primitive type");
          }
        }
        else {
          switch(parameter.type.basePrimitiveType) {
            case PrimitiveTypeEnum.REAL -> currentMemory.storeFloat(parameter.identifier, dataStack.popFloat());
            case PrimitiveTypeEnum.INTEGER -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            case PrimitiveTypeEnum.BOOLEAN -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            case PrimitiveTypeEnum.CHAR -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            case PrimitiveTypeEnum.STRING -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            default -> throw new RuntimeException("Unsupported primitive type");
          }
        }
      }
    }

    visit(procedureDeclaration.compoundStatement);

    memoryStack.popFrame();
  }

  private void visitFunctionCallStatementNode(FunctionCallExpressionNode node) {
    if(builtInProceduresAndFunctionsTable.lookProcedureOrFunction(node.functionIdentifier)) {
      for(ExpressionNode argument : node.arguments) {
        visit(argument);
      }
      
      executeBuiltInProcedureOrFunction(node.functionIdentifier);
      return;
    }
    
    for(ExpressionNode argument : node.arguments) {
      visit(argument);
    }

    memoryStack.pushFrame(node.functionIdentifier);
    ProceduresAndFunctionsEntry topMemoryEntry = memoryStack.peekEntry();
    Memory currentMemory = memoryStack.peek();

    ProcedureOrFunctionDeclarationNode declarationNode = programNode.getDeclaration(node.functionIdentifier);

    if(declarationNode.parameters.isPresent()) {
      var parameters = declarationNode.parameters.get().variables;
      for (int i = parameters.size() - 1; i >= 0; i--) {
        var parameter = parameters.get(i);
        
        if(parameter.type instanceof ArrayVariableType ap) {
          int size = ap.endIndex - ap.startIndex - 1;

          switch(ap.basePrimitiveType) {
            case PrimitiveTypeEnum.REAL -> currentMemory.storeFloatArray(parameter.identifier, dataStack.popFloatArray(size));
            case PrimitiveTypeEnum.INTEGER -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            case PrimitiveTypeEnum.BOOLEAN -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            case PrimitiveTypeEnum.CHAR -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            case PrimitiveTypeEnum.STRING -> currentMemory.storeIntegerArray(parameter.identifier, dataStack.popIntegerArray(size));
            default -> throw new RuntimeException("Unsupported primitive type");
          }
        }
        else {
          switch(parameter.type.basePrimitiveType) {
            case PrimitiveTypeEnum.REAL -> currentMemory.storeFloat(parameter.identifier, dataStack.popFloat());
            case PrimitiveTypeEnum.INTEGER -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            case PrimitiveTypeEnum.BOOLEAN -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            case PrimitiveTypeEnum.CHAR -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            case PrimitiveTypeEnum.STRING -> currentMemory.storeInteger(parameter.identifier, dataStack.popInteger());
            default -> throw new RuntimeException("Unsupported primitive type");
          }
        }
      }
    }

    visit(declarationNode.compoundStatement);

    switch (topMemoryEntry.returnType) {
      case PrimitiveTypeEnum.INTEGER -> dataStack.pushInteger(currentMemory.loadInteger(node.functionIdentifier));
      case PrimitiveTypeEnum.REAL -> dataStack.pushFloat(currentMemory.loadFloat(node.functionIdentifier));
      case PrimitiveTypeEnum.CHAR -> dataStack.pushInteger(currentMemory.loadInteger(node.functionIdentifier));
      case PrimitiveTypeEnum.BOOLEAN -> dataStack.pushInteger(currentMemory.loadInteger(node.functionIdentifier));
      case PrimitiveTypeEnum.STRING -> dataStack.pushInteger(currentMemory.loadInteger(node.functionIdentifier));
      default -> throw new RuntimeException("Unsupported primitive type of array type");
    }

    memoryStack.popFrame();
  }

  private void visit(AstNode node) {
    switch (node) {
      case ProgramNode concreteTypeNode -> visitProgramNode(concreteTypeNode);
      case CompoundStatementNode concreteTypeNode -> visitCompoundStatementNode(concreteTypeNode);
      case IfStatementNode concreteTypeNode -> visitIfStatementNode(concreteTypeNode);
      case ForStatementNode concreteTypeNode -> visitForStatementNode(concreteTypeNode);
      case PrimitiveTypeExpressionNode<?> concreteTypeNode -> visitPrimitiveTypeExpressionNode(concreteTypeNode);
      case LogicOperatorExpressionNode concreteTypeNode -> visitLogicOperatorExpressionNode(concreteTypeNode);
      case NotOperatorExpressionNode concreteTypeNode -> visitNotOperatorExpressionNode(concreteTypeNode);
      case IntegerToRealExpressionNode concreteTypeNode -> visitIntegerToRealExpressionNode(concreteTypeNode);
      case CharToStringExpressionNode concreteTypeNode -> visitCharToStringExpressionNode(concreteTypeNode);
      case ComparisonOperatorExpressionNode concreteTypeNode -> visitComparisonOperatorExpressionNode(concreteTypeNode);
      case AssignmentStatementNode concreteTypeNode -> visitAssignmentStatementNode(concreteTypeNode);
      case IndexedVariableAccessExpressionNode concreteTypeNode -> visitIndexedVariableAccessExpressionNode(concreteTypeNode);
      case ArithmeticOperatorExpressionNode concreteTypeNode -> visitArithmeticOperatorExpressionNode(concreteTypeNode);
      case VariableAccessExpressionNode concreteTypeNode -> visitVariableAccessExpressionNode(concreteTypeNode);
      case ProcedureCallStatementNode concreteTypeNode -> visitProcedureCallStatementNode(concreteTypeNode);
      case FunctionCallExpressionNode concreteTypeNode -> visitFunctionCallStatementNode(concreteTypeNode);
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void executeBuiltInProcedureOrFunction(String identifier) {
    switch(identifier.toLowerCase()) {
      case "write" -> IO.print(stringLiteralsMemory.getEntry(dataStack.popInteger()));
      case "writeln" -> IO.println(stringLiteralsMemory.getEntry(dataStack.popInteger()));
      case "itos" -> dataStack.pushInteger(stringLiteralsMemory.addEntry(String.valueOf(dataStack.popInteger())));
      case "rtos" -> dataStack.pushInteger(stringLiteralsMemory.addEntry(String.valueOf(dataStack.popFloat())));
      case "btos" -> dataStack.pushInteger(stringLiteralsMemory.addEntry(String.valueOf(dataStack.popInteger() == 1 ? true : false)));
      default -> throw new RuntimeException("Unsupported built-in procedure or function");
    }
  }
}
