package codegenerator;

import ast.types.AstNode;
import ast.types.ProgramNode;
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
import ast.types.statements.contract.StatementNode;
import ast.types.statements.implementations.AssignmentStatementNode;
import ast.types.statements.implementations.CompoundStatementNode;
import ast.types.statements.implementations.ExitStatementNode;
import ast.types.statements.implementations.ForStatementNode;
import ast.types.statements.implementations.IfStatementNode;
import ast.types.statements.implementations.ProcedureCallStatementNode;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveTypeEnum;

public class CodeGenerator {
  private final StringBuilder mipsTargetCode ;
  private final ProgramNode programNode;

  private final StringLiteralsTable stringLiteralsTable;
  private final VariablesTable globalVariablesTable;

  public CodeGenerator(
    ProgramNode programNode,
    VariablesTable globalVariablesTable,
    StringLiteralsTable stringLiteralsTable
  ) {
    this.mipsTargetCode = new StringBuilder();
    this.programNode = programNode;

    this.globalVariablesTable = globalVariablesTable;
    this.stringLiteralsTable = stringLiteralsTable;
  }

  public String generate() {
    emitHeader(); 
    visit(this.programNode);
    emitFooter();

    return mipsTargetCode.toString();
  }

  private void emitHeader() {
    mipsTargetCode.append(".data\n");

    for(Integer key : stringLiteralsTable.keySet()) {
      mipsTargetCode.append("string%d: .asciiz \"%s\"\n".formatted(key, stringLiteralsTable.get(key)));
    }

    for(VariableTableEntry variable : globalVariablesTable.toList()) {
      mipsTargetCode.append(variable.identifier + ": .word 0\n");
    }

    mipsTargetCode.append("\n.text\n.globl %s\n%s:\n".formatted(programNode.programIdentifier, programNode.programIdentifier));
  }

  private void emitFooter() {
    mipsTargetCode.append("li $v0, 10\n");
    mipsTargetCode.append("syscall\n");
  }

  // Usamos $t0 como acumulador temporário para operações
  // Stack operations: push/pop temporários
  private void emitPushTemp(String reg) {
    // $sp é o registrador que aponta para o topo da pilha
    // vamos emitir uma instrução para subtrair $sp em 4 bytes (para apontar para a próxima posição vaga - a pilha
    // cresce de "cima" (endereço mais alto) para "baixo" (emdereço mais baixo)
    // subu é "unsigned subtraction", ou seja $sp=$sp-4
    mipsTargetCode.append("subu $sp, $sp, 4\n");
    mipsTargetCode.append("sw " + reg + ", 0($sp)\n");
  }

  private void emitPopTemp(String reg) {
    // vamos emitir uma instrução para carregar da pilha no registrador
    mipsTargetCode.append("lw " + reg + ", 0($sp)\n");
    // addu é "unsigned addition", ou seja $sp=$sp+4
    mipsTargetCode.append("addu $sp, $sp, 4\n");
  }

  private void visitProgramNode(ProgramNode node) {
    visit(node.compoundStatement);
  }

  private void visitCompoundStatementNode(CompoundStatementNode node) {
    for (StatementNode statement : node.statements) {
      visit(statement);
    }
  }

  private void visitIfStatementNode(IfStatementNode node) {
  }

  private void visitForStatementNode(ForStatementNode node) {
  }

  private void visitAssignmentStatementNode(AssignmentStatementNode node) {
  }

  private void visitPrimitiveTypeExpressionNode(PrimitiveTypeExpressionNode<?> node) {
    
    switch (node.value) {
      case Integer value -> {
        mipsTargetCode.append("li $t0, " + value + "\n");
        emitPushTemp("$t0");
      }
      case Double value -> {
        mipsTargetCode.append("li $t0, " + value + "\n");
        emitPushTemp("$t0");
      }
      case String value -> {
        // TODO
      }
      case Boolean value -> {
        mipsTargetCode.append("li $t0, " + (value ? 1 : 0) + "\n");
        emitPushTemp("$t0");
      }
      case Character value -> {
        mipsTargetCode.append("li $t0, " + value + "\n");
        emitPushTemp("$t0");
      }
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handlePlus(ArithmeticOperatorExpressionNode node) {
  }

  private void handleMinus(ArithmeticOperatorExpressionNode node) {
  }

  private void handleMultiplication(ArithmeticOperatorExpressionNode node) {
  }

  private void handleRealDivision(ArithmeticOperatorExpressionNode node) {
  }

  private void handleIntegerDivision(ArithmeticOperatorExpressionNode node) {
  }

  private void visitArithmeticOperatorExpressionNode(ArithmeticOperatorExpressionNode node) {
  }

  private void visitIndexedVariableAccessExpressionNode(IndexedVariableAccessExpressionNode node) {
  }

  private void visitVariableAccessExpressionNode(VariableAccessExpressionNode node) {
  }

  private void visitLogicOperatorExpressionNode(LogicOperatorExpressionNode node) {
  }

  private void visitNotOperatorExpressionNode(NotOperatorExpressionNode node) {
  }

  private void visitIntegerToRealExpressionNode(IntegerToRealExpressionNode node) {
  }

  private void visitCharToStringExpressionNode(CharToStringExpressionNode node) {
  }

  private void visitComparisonOperatorExpressionNode(ComparisonOperatorExpressionNode node) {
  }

  private void visitProcedureCallStatementNode(ProcedureCallStatementNode node) {
  }

  private void visitFunctionCallStatementNode(FunctionCallExpressionNode node) {
  }

  private void visitExitStatementNode(ExitStatementNode node) {
  }

  private void visit(AstNode node) {
    switch (node) {
      case StatementNode statementNode -> {
        switch(statementNode) {
          case CompoundStatementNode concreteTypeNode -> visitCompoundStatementNode(concreteTypeNode);
          case IfStatementNode concreteTypeNode -> visitIfStatementNode(concreteTypeNode);
          case ForStatementNode concreteTypeNode -> visitForStatementNode(concreteTypeNode);
          case AssignmentStatementNode concreteTypeNode -> visitAssignmentStatementNode(concreteTypeNode);
          case ProcedureCallStatementNode concreteTypeNode -> visitProcedureCallStatementNode(concreteTypeNode);
          case ExitStatementNode concreteTypeNode -> visitExitStatementNode(concreteTypeNode);
          default -> throw new RuntimeException("Unsupported statement node type");
        }
      }
      case ProgramNode concreteTypeNode -> visitProgramNode(concreteTypeNode);
      case PrimitiveTypeExpressionNode<?> concreteTypeNode -> visitPrimitiveTypeExpressionNode(concreteTypeNode);
      case LogicOperatorExpressionNode concreteTypeNode -> visitLogicOperatorExpressionNode(concreteTypeNode);
      case NotOperatorExpressionNode concreteTypeNode -> visitNotOperatorExpressionNode(concreteTypeNode);
      case IntegerToRealExpressionNode concreteTypeNode -> visitIntegerToRealExpressionNode(concreteTypeNode);
      case CharToStringExpressionNode concreteTypeNode -> visitCharToStringExpressionNode(concreteTypeNode);
      case ComparisonOperatorExpressionNode concreteTypeNode -> visitComparisonOperatorExpressionNode(concreteTypeNode);
      case IndexedVariableAccessExpressionNode concreteTypeNode -> visitIndexedVariableAccessExpressionNode(concreteTypeNode);
      case ArithmeticOperatorExpressionNode concreteTypeNode -> visitArithmeticOperatorExpressionNode(concreteTypeNode);
      case VariableAccessExpressionNode concreteTypeNode -> visitVariableAccessExpressionNode(concreteTypeNode);
      case FunctionCallExpressionNode concreteTypeNode -> visitFunctionCallStatementNode(concreteTypeNode);
      default -> throw new RuntimeException("Unsupported node type");
    }
  }

  private void executeBuiltInProcedureOrFunction(String identifier, PrimitiveTypeEnum argType) {
    switch (identifier.toLowerCase()) {
      case "write" -> IO.print("");
      case "writeln" -> IO.print("");
      case "itos" -> IO.print("");
      case "rtos" -> IO.print("");
      case "btos" -> IO.print("");
      case "abs" -> IO.print("");
      case "sqr" -> IO.print("");
      case "sqrt" -> IO.print("");
      case "trunc" -> IO.print("");
      case "round" -> IO.print("");
      case "ord", "chr" -> IO.print("");
      case "succ" -> IO.print("");
      case "pred" -> IO.print("");
      case "length" -> IO.print("");
      case "upcase" -> IO.print("");
      default -> IO.print("");
    }
  }
}
