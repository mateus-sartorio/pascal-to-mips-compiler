package codegenerator;

import ast.types.AstNode;
import ast.types.ProgramNode;
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
import ast.types.statements.contract.StatementNode;
import ast.types.statements.implementations.AssignmentStatementNode;
import ast.types.statements.implementations.CompoundStatementNode;
import ast.types.statements.implementations.ExitStatementNode;
import ast.types.statements.implementations.ForStatementNode;
import ast.types.statements.implementations.IfStatementNode;
import ast.types.statements.implementations.ProcedureCallStatementNode;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.ArrayVariableType;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;

public class CodeGenerator {
  private final StringBuilder mipsTargetCode ;
  private final ProgramNode programNode;

  private final StringLiteralsTable stringLiteralsTable;
  private final VariablesTable globalVariablesTable;
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable;

  private int labelCounter;

  public CodeGenerator(
    ProgramNode programNode,
    VariablesTable globalVariablesTable,
    StringLiteralsTable stringLiteralsTable,
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable
  ) {
    this.mipsTargetCode = new StringBuilder();
    this.programNode = programNode;

    this.globalVariablesTable = globalVariablesTable;
    this.stringLiteralsTable = stringLiteralsTable;
    this.builtInProceduresAndFunctionsTable = builtInProceduresAndFunctionsTable;

    this.labelCounter = 0;
  }

  public String generate() {
    emitHeader(); 
    visit(this.programNode);
    emitFooter();
    emitIntegerToStringConversionFunction();
    emitRealToStringConversionFunction();
    emitBooleanToStringConversionFunction();

    return mipsTargetCode.toString();
  }

  private void emitHeader() {
    mipsTargetCode.append(".data\n");

    for(Integer key : stringLiteralsTable.keySet()) {
      mipsTargetCode.append("string%d: .asciiz \"%s\"\n".formatted(key, stringLiteralsTable.get(key)));
    }

    mipsTargetCode.append("__bool_true: .asciiz \"true\"\n");
    mipsTargetCode.append("__bool_false: .asciiz \"false\"\n");

    for(VariableTableEntry variable : globalVariablesTable.toList()) {
      mipsTargetCode.append(variable.identifier.toLowerCase() + ": .word 0\n");
    }

    mipsTargetCode.append("\n.text\n.globl %s\n%s:\n".formatted(programNode.programIdentifier, programNode.programIdentifier));
  }

  private void emitFooter() {
    mipsTargetCode.append("li $v0, 10\n");
    mipsTargetCode.append("syscall\n");
  }

  private void emitIntegerToStringConversionFunction() {
    mipsTargetCode.append("""
__itoa:                       # $a0 = integer
    move $t0, $a0             # $t0 = n  (save it; syscall 9 needs $a0)
    li $v0, 9                 # allocate a fixed 12-byte buffer
    li $a0, 12                #   max is "-2147483648\\0" = 12 bytes exactly
    syscall                   # $v0 = buffer base
    addiu $t2, $v0, 11        # $t2 -> last byte
    sb $zero, 0($t2)          # null terminator at the end
    addiu $t2, $t2, -1        # $t2 -> first digit slot (offset 10)

    li $t5, 0                 # negative flag
    bgez $t0, __itoa_zero
    li $t5, 1
    subu $t0, $zero, $t0      # n = -n, work with positive

__itoa_zero:
    bnez $t0, __itoa_loop     # special-case n == 0 (loop would emit nothing)
    li $t6, 48                # '0'
    sb $t6, 0($t2)
    move $v0, $t2
    jr $ra

__itoa_loop:
    beqz $t0, __itoa_sign
    li $t3, 10
    divu $t0, $t3             # LO = n/10, HI = n%10
    mfhi $t6                  # remainder = this digit (0..9)
    mflo $t0                  # n = n/10  for next iteration
    addiu $t6, $t6, 48        # digit -> ASCII ('0' = 48)
    sb $t6, 0($t2)            # write it
    addiu $t2, $t2, -1        # step left
    j __itoa_loop

__itoa_sign:
    beqz $t5, __itoa_done
    li $t6, 45                # '-'
    sb $t6, 0($t2)
    addiu $t2, $t2, -1

__itoa_done:
    addiu $v0, $t2, 1         # $t2 sits one before the leftmost char; +1 = start
    jr $ra
""");
  }

  private void emitRealToStringConversionFunction() {
    mipsTargetCode.append("""
__rtoa:                       # $a0 = real (float bits)
    addiu $sp, $sp, -20       # frame: save $ra + $s0-$s3
    sw $ra, 0($sp)
    sw $s0, 4($sp)
    sw $s1, 8($sp)
    sw $s2, 12($sp)
    sw $s3, 16($sp)

    mtc1 $a0, $f20            # $f20 = x  (survives syscall 9 below)

    li $v0, 9                 # allocate a fixed 32-byte buffer
    li $a0, 32
    syscall                   # $v0 = buffer base
    move $s0, $v0             # $s0 = write pointer
    move $s1, $v0             # $s1 = buffer base (return value)

    mtc1 $zero, $f22          # $f22 = 0.0
    c.lt.s $f20, $f22         # cc = (x < 0.0)
    bc1f __rtoa_int           # if x >= 0, skip the sign
    li $t0, 45                # '-'
    sb $t0, 0($s0)
    addiu $s0, $s0, 1
    neg.s $f20, $f20          # x = -x, work with positive

__rtoa_int:
    trunc.w.s $f22, $f20      # truncate toward zero
    mfc1 $s2, $f22            # $s2 = integer part
    cvt.s.w $f24, $f22        # $f24 = (float) integer part
    sub.s $f26, $f20, $f24    # $f26 = fractional part (0 <= frac < 1)

    li $t0, 1000000           # scale the fraction to 6 decimals
    mtc1 $t0, $f28
    cvt.s.w $f28, $f28        # $f28 = 1000000.0
    mul.s $f26, $f26, $f28    # frac *= 1e6
    li $t0, 0x3F000000        # 0.5 in IEEE-754 single
    mtc1 $t0, $f28
    add.s $f26, $f26, $f28    # round to nearest
    trunc.w.s $f26, $f26
    mfc1 $s3, $f26            # $s3 = fractional integer (0..1000000)

    li $t0, 1000000           # if the fraction rounded up to 1.0, carry it
    blt $s3, $t0, __rtoa_emit_int
    subu $s3, $s3, $t0        # frac = 0
    addiu $s2, $s2, 1         # carry into the integer part

__rtoa_emit_int:
    move $a0, $s2             # convert the integer part via __itoa
    jal __itoa
    move $t0, $v0             # $t0 = source pointer (itoa result)

__rtoa_copy_int:
    lb $t1, 0($t0)            # copy the integer digits into our buffer
    beqz $t1, __rtoa_dot
    sb $t1, 0($s0)
    addiu $s0, $s0, 1
    addiu $t0, $t0, 1
    j __rtoa_copy_int

__rtoa_dot:
    li $t1, 46                # '.'
    sb $t1, 0($s0)
    addiu $s0, $s0, 1

    li $t2, 100000            # divisor for the most significant fractional digit
    li $t3, 6                 # emit exactly 6 fractional digits (with leading zeros)

__rtoa_frac_loop:
    beqz $t3, __rtoa_done
    divu $s3, $t2             # LO = frac/divisor, HI = frac%divisor
    mflo $t1                  # this digit
    mfhi $s3                  # remainder for the next iteration
    addiu $t1, $t1, 48        # digit -> ASCII
    sb $t1, 0($s0)
    addiu $s0, $s0, 1
    li $t4, 10
    divu $t2, $t4             # divisor /= 10
    mflo $t2
    addiu $t3, $t3, -1
    j __rtoa_frac_loop

__rtoa_done:
    sb $zero, 0($s0)          # null terminator
    move $v0, $s1             # return the buffer base

    lw $ra, 0($sp)
    lw $s0, 4($sp)
    lw $s1, 8($sp)
    lw $s2, 12($sp)
    lw $s3, 16($sp)
    addiu $sp, $sp, 20
    jr $ra
""");
  }

  private void emitBooleanToStringConversionFunction() {
    mipsTargetCode.append("""
__btoa:                       # $a0 = boolean (0 or 1)
    beqz $a0, __btoa_false
    la $v0, __bool_true
    jr $ra

__btoa_false:
    la $v0, __bool_false
    jr $ra
""");
  }

  private void emitPushTemp(String reg) {
    mipsTargetCode.append("subu $sp, $sp, 4\n");
    mipsTargetCode.append("sw " + reg + ", 0($sp)\n");
  }

  private void emitPopTemp(String reg) {
    mipsTargetCode.append("lw " + reg + ", 0($sp)\n");
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
    int uniqueLabelId = labelCounter++;
    String elseLabel = "else_label_" + uniqueLabelId;
    String endIfLabel = "end_if_label_" + uniqueLabelId;

    visit(node.condition);
    emitPopTemp("$t0");

    // Se aqui for o else, então pula para o elseLabel se a condição for falsa
    if (node.elseStatement.isPresent()) {
      mipsTargetCode.append("beq $t0, $zero, " + elseLabel + "\n");
    } else {
      mipsTargetCode.append("beq $t0, $zero, " + endIfLabel + "\n");
    }

    // Then statement
    visit(node.thenStatement);

    if (node.elseStatement.isPresent()) {
      mipsTargetCode.append("j " + endIfLabel + "\n");
      mipsTargetCode.append(elseLabel + ":\n");
      visit(node.elseStatement.get());
    }

    mipsTargetCode.append(endIfLabel + ":\n\n");

  }

  private void visitForStatementNode(ForStatementNode node) {
    int uniqueLabelId = labelCounter++;
    String loopStartLabel = "for_start_" + uniqueLabelId;
    String loopEndLabel = "for_end_" + uniqueLabelId;
    
    visit(node.finalValue);
    visit(node.initialValue);

    emitPopTemp("$t0"); //t0 = initial value
    emitPopTemp("$t1"); //t1 = final value

    mipsTargetCode.append("sw $t0, " + node.controlVariable.identifier + "\n");
    emitPushTemp("$t1");
    mipsTargetCode.append(loopStartLabel + ":\n");

    mipsTargetCode.append("lw $t0, " + node.controlVariable.identifier + "\n");
    emitPopTemp("$t1"); //t1 = final value

    if(node.isDownto) {
      mipsTargetCode.append("blt $t0, $t1, " + loopEndLabel + "\n");
    } else {
      mipsTargetCode.append("bgt $t0, $t1, " + loopEndLabel + "\n");
    }

    visit(node.body);

    mipsTargetCode.append("lw $t0, " + node.controlVariable.identifier + "\n");

    if(node.isDownto) {
      mipsTargetCode.append("subi $t0, $t0, 1\n");
    } else {
      mipsTargetCode.append("addi $t0, $t0, 1\n");
    }

    mipsTargetCode.append("sw $t0, " + node.controlVariable.identifier + "\n");

    mipsTargetCode.append("j " + loopStartLabel + "\n");
    mipsTargetCode.append(loopEndLabel + ":\n");
    emitPopTemp("$t1"); 
    
  }

  private void visitAssignmentStatementNode(AssignmentStatementNode node) {
    // TODO
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
    visit(node.right);
    visit(node.left);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        mipsTargetCode.append("add $t0, $t0, $t1\n");
        emitPushTemp("$t0");
      }
      else {
        mipsTargetCode.append("mtc1 $t0, $f0\n");
        
        mipsTargetCode.append("mtc1 $t1, $f2\n");
        mipsTargetCode.append("cvt.s.w $f2, $f2\n");

        mipsTargetCode.append("add.s $f0, $f0, $f2\n");
        mipsTargetCode.append("mfc1 $t0, $f0\n");
        emitPushTemp("$t0");
      }
    }
    else {
      if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
        if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
          mipsTargetCode.append("mtc1 $t0, $f0\n");
          mipsTargetCode.append("cvt.s.w $f0, $f0\n");
          
          mipsTargetCode.append("mtc1 $t1, $f2\n");
          
          mipsTargetCode.append("add.s $f0, $f0, $f2\n");
          mipsTargetCode.append("mfc1 $t0, $f0\n");
          emitPushTemp("$t0");
        }
        else {
          mipsTargetCode.append("mtc1 $t0, $f0\n");
          mipsTargetCode.append("cvt.s.w $f0, $f0\n");
          
          mipsTargetCode.append("mtc1 $t1, $f2\n");
          mipsTargetCode.append("cvt.s.w $f2, $f2\n");
          
          mipsTargetCode.append("add.s $f0, $f0, $f2\n");
          mipsTargetCode.append("mfc1 $t0, $f0\n");
          emitPushTemp("$t0");
        }
      }
      else {
        if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
          if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
            // TODO
          }
          else {
            // TODO
          }
        }
        else {
          if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
              // TODO
            }
            else {
              // TODO
            }
          }
          else {
            throw new RuntimeException("Unsupported primitive type");
          }
        }      
      }
    }
  }

  private void handleMinus(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // no esq = int / no dir = int
    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        mipsTargetCode.append("sub $t0, $t0, $t1\n");
        emitPushTemp("$t0");
      }
      // no esq = int / no dir = real
      else {
        mipsTargetCode.append("mtc1 $t0, $f0\n");
        mipsTargetCode.append("cvt.s.w $f0, $f0\n");
        
        mipsTargetCode.append("mtc1 $t1, $f2\n");
        
        mipsTargetCode.append("sub.s $f0, $f0, $f2\n");
        mipsTargetCode.append("mfc1 $t0, $f0\n");
        emitPushTemp("$t0");
      }
    }
    else {
      // no esq = real / no dir = int
      if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
        if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
          mipsTargetCode.append("mtc1 $t0, $f0\n");
          mipsTargetCode.append("cvt.s.w $f0, $f0\n");
          
          mipsTargetCode.append("mtc1 $t1, $f2\n");
          
          mipsTargetCode.append("sub.s $f0, $f0, $f2\n");
          mipsTargetCode.append("mfc1 $t0, $f0\n");
          emitPushTemp("$t0");
        }
        // no esq = real / no dir = real
        else {
          mipsTargetCode.append("mtc1 $t0, $f0\n");
          mipsTargetCode.append("cvt.s.w $f0, $f0\n");
          
          mipsTargetCode.append("mtc1 $t1, $f2\n");
          mipsTargetCode.append("cvt.s.w $f2, $f2\n");
          
          mipsTargetCode.append("sub.s $f0, $f0, $f2\n");
          mipsTargetCode.append("mfc1 $t0, $f0\n");
          emitPushTemp("$t0");
        }
      }
      else {
        throw new RuntimeException("Unsupported primitive type");
      }
    }
    
  }

  private void handleMultiplication(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // no esq = int / no dir = int
    if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        mipsTargetCode.append("mul $t0, $t0, $t1\n");
        emitPushTemp("$t0");
      }
      // no esq = int / no dir = real
      else {
        mipsTargetCode.append("mtc1 $t0, $f0\n");
        mipsTargetCode.append("cvt.s.w $f0, $f0\n");
        
        mipsTargetCode.append("mtc1 $t1, $f2\n");
        
        mipsTargetCode.append("mul.s $f0, $f0, $f2\n");
        mipsTargetCode.append("mfc1 $t0, $f0\n");
        emitPushTemp("$t0");
      }
    }
    else {
      // no esq = real / no dir = int
      if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
        if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
          mipsTargetCode.append("mtc1 $t0, $f0\n");
          
          mipsTargetCode.append("mtc1 $t1, $f2\n");
          mipsTargetCode.append("cvt.s.w $f2, $f2\n");
          
          mipsTargetCode.append("mul.s $f0, $f0, $f2\n");
          mipsTargetCode.append("mfc1 $t0, $f0\n");
          emitPushTemp("$t0");
        }
        // no esq = real / no dir = real
        else {
          mipsTargetCode.append("mtc1 $t0, $f0\n");
          mipsTargetCode.append("cvt.s.w $f0, $f0\n");
          
          mipsTargetCode.append("mtc1 $t1, $f2\n");
          mipsTargetCode.append("cvt.s.w $f2, $f2\n");
          
          mipsTargetCode.append("mul.s $f0, $f0, $f2\n");
          mipsTargetCode.append("mfc1 $t0, $f0\n");
          emitPushTemp("$t0");
        }
      }
      else {
        throw new RuntimeException("Unsupported primitive type");      
      }
    }
  }

  private void handleRealDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    emitPopTemp("$t1");
    emitPopTemp("$t0");
    
    mipsTargetCode.append("mtc1 $t0, $f0\n");
    mipsTargetCode.append("cvt.s.w $f0, $f0\n");
    
    mipsTargetCode.append("mtc1 $t1, $f2\n");
    mipsTargetCode.append("cvt.s.w $f2, $f2\n");

    // TODO : check if divisor is 0

    mipsTargetCode.append("div.s $f0, $f2, $f0\n");
    mipsTargetCode.append("mfc1 $t0, $f0\n");
    emitPushTemp("$t0");
  }

  private void handleIntegerDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // TODO : check if divisor is 0

    mipsTargetCode.append("div $t1, $t0\n");
    mipsTargetCode.append("mflo $t0\n");
    emitPushTemp("$t0");
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
    // TODO

    visit(node.indexExpressionNode);
    emitPopTemp("$t1");

    if (node.type instanceof PrimitiveVariableType && node.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
      mipsTargetCode.append("la $t0, " + node.identifier.toLowerCase() + "\n");
      mipsTargetCode.append("add $t0, $t0, $t1\n");
      mipsTargetCode.append("lbu $t0, 0($t0)\n");
      emitPushTemp("$t0");

      return;
    }
  }

  private void visitVariableAccessExpressionNode(VariableAccessExpressionNode node) {
    switch (node.type) {
      case PrimitiveVariableType _ -> {
        switch (node.type.basePrimitiveType) {
          case PrimitiveTypeEnum.INTEGER, PrimitiveTypeEnum.REAL, PrimitiveTypeEnum.CHAR, PrimitiveTypeEnum.BOOLEAN -> {
            mipsTargetCode.append("lw $t0, " + node.identifier.toLowerCase() + "\n");
            emitPushTemp("$t0");
          }
          case PrimitiveTypeEnum.STRING -> {
            mipsTargetCode.append("la $t0, " + node.identifier.toLowerCase() + "\n");
            emitPushTemp("$t0");
          }
          default -> throw new RuntimeException("Unsupported primitive type");
        }
      }
      case ArrayVariableType _ -> {
        switch (node.type.basePrimitiveType) {
          // TODO
          default -> throw new RuntimeException("Unsupported primitive type of array type");
        }
      }
      default -> throw new RuntimeException("Unsupported type");
    }
  }

  private void visitLogicOperatorExpressionNode(LogicOperatorExpressionNode node) {
    visit(node.right);
    visit(node.left);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    switch (node.operator) {
      case "and" -> mipsTargetCode.append("and $t0, $t0, $t1\n");
      case "or" -> mipsTargetCode.append("or $t0, $t0, $t1\n");
      default -> throw new RuntimeException("Unsupported logic operator");
    }
    emitPushTemp("$t0");
  }

  private void visitNotOperatorExpressionNode(NotOperatorExpressionNode node) {
    visit(node.expression);
    emitPopTemp("$t0");
    mipsTargetCode.append("xori $t0, $t0, 1\n");
    emitPushTemp("$t0");
  }

  private void visitIntegerToRealExpressionNode(IntegerToRealExpressionNode node) {
    visit(node.expression);
    emitPopTemp("$t0");
    
    mipsTargetCode.append("mtc1 $t0, $f0\n");
    mipsTargetCode.append("cvt.s.w $f0, $f0\n");
    mipsTargetCode.append("mfc1 $t0, $f0\n");
    emitPushTemp("$t0");
  }

  private void visitCharToStringExpressionNode(CharToStringExpressionNode node) {
    visit(node.expression);
    emitPopTemp("$t0");
    
    mipsTargetCode.append("li $v0, 9\n");
    mipsTargetCode.append("li $a0, 4\n");
    mipsTargetCode.append("syscall\n");

    mipsTargetCode.append("sb $t0, 0($v0)\n");
    mipsTargetCode.append("sb $zero, 1($v0)\n");

    mipsTargetCode.append("move $t0, $v0\n");
    emitPushTemp("$t0");
  }

  private void visitComparisonOperatorExpressionNode(ComparisonOperatorExpressionNode node) {
    String operator = node.operator;

    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // TODO: handle string comparisons
    switch (operator) {
      case "=" -> {
        mipsTargetCode.append("subu $t0, $t0, $t1\n");
        mipsTargetCode.append("sltiu $t0, $t0, 1\n");
      }
      case "<>" -> {
        mipsTargetCode.append("subu $t0, $t0, $t1\n");
        mipsTargetCode.append("sltu $t0, $zero, $t0\n");
      }
      case "<" -> {
        mipsTargetCode.append("slt $t0, $t0, $t1\n");
      }
      case ">" -> {
        mipsTargetCode.append("slt $t0, $t1, $t0\n");
      }
      case "<=" -> {
        mipsTargetCode.append("slt $t0, $t1, $t0\n");
        mipsTargetCode.append("xori $t0, $t0, 1\n");
      }
      case ">=" -> {
        mipsTargetCode.append("slt $t0, $t0, $t1\n");
        mipsTargetCode.append("xori $t0, $t0, 1\n");
      }
      default -> throw new RuntimeException("Unsupported operation");
    }

    emitPushTemp("$t0");
  }

  private void visitProcedureCallStatementNode(ProcedureCallStatementNode node) {
    if (builtInProceduresAndFunctionsTable.lookProcedureOrFunction(node.procedureIdentifier)) {
      for (ExpressionNode argument : node.arguments) {
        visit(argument);
      }

      PrimitiveTypeEnum firstArgType = node.arguments.isEmpty() ? null : node.arguments.get(0).type.basePrimitiveType;

      executeBuiltInProcedureOrFunction(node.procedureIdentifier, firstArgType);
      return;
    }
  }

  private void visitFunctionCallStatementNode(FunctionCallExpressionNode node) {
    if (builtInProceduresAndFunctionsTable.lookProcedureOrFunction(node.functionIdentifier)) {
      for (ExpressionNode argument : node.arguments) {
        visit(argument);
      }

      PrimitiveTypeEnum firstArgType = node.arguments.isEmpty() ? null : node.arguments.get(0).type.basePrimitiveType;

      executeBuiltInProcedureOrFunction(node.functionIdentifier, firstArgType);
      return;
    }
  }

  private void visitExitStatementNode(ExitStatementNode node) {
    // TODO
  }

  private void executeBuiltInProcedureOrFunction(String identifier, PrimitiveTypeEnum argType) {
    switch (identifier.toLowerCase()) {
      case "write" -> {
        emitPopTemp("$a0");
        mipsTargetCode.append("li $v0, 4\n");
        mipsTargetCode.append("syscall\n");
      }
      case "writeln" -> {
        emitPopTemp("$a0");
        mipsTargetCode.append("li $v0, 4\n");
        mipsTargetCode.append("syscall\n");

        mipsTargetCode.append("li $a0, 10\n");
        mipsTargetCode.append("li $v0, 11\n");
        mipsTargetCode.append("syscall\n");
      }
      case "itos" -> {
        emitPopTemp("$a0");
        mipsTargetCode.append("jal __itoa\n");
        mipsTargetCode.append("move $t0, $v0\n");
        emitPushTemp("$t0");
      }
      case "rtos" -> {
        emitPopTemp("$a0");
        mipsTargetCode.append("jal __rtoa\n");
        mipsTargetCode.append("move $t0, $v0\n");
        emitPushTemp("$t0");
      }
      case "btos" -> {
        emitPopTemp("$a0");
        mipsTargetCode.append("jal __btoa\n");
        mipsTargetCode.append("move $t0, $v0\n");
        emitPushTemp("$t0");
      }
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

  private void visit(AstNode node) {
    switch (node) {
      case ProgramNode concreteTypeNode -> visitProgramNode(concreteTypeNode);
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
      case ExpressionNode expressionNode -> {
        switch(expressionNode) {
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
          default -> throw new RuntimeException("Unsupported expression node type");
        }
      }
      default -> throw new RuntimeException("Unsupported node type");
    }
  }
}
