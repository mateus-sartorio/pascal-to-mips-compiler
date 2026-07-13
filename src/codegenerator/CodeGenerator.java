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
  private static final int WORD_SIZE = 4;

  private final StringBuilder mipsTargetCode ;
  private final ProgramNode programNode;

  private final StringLiteralsTable stringLiteralsTable;
  private final VariablesTable globalVariablesTable;
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable;

  private int labelCounter;
  private int indentLevel;

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
    this.indentLevel = 0;
  }

  public String generate() {
    emitHeader(); 
    visit(this.programNode);
    emitFooter();

    // Auxiliary functions
    emit("");
    emitIntegerToStringConversionFunction();
    emitRealToStringConversionFunction();
    emitBooleanToStringConversionFunction();
    emitStringComparisonFunction();
    emitConcatStringWithString();
    emitConcatCharWithString();
    emitConcatStringWithChar();

    return mipsTargetCode.toString();
  }

  private void emit(String line) {
    mipsTargetCode.append("\t".repeat(indentLevel)).append(line).append("\n");
  }

  private void emitHeader() {
    emit(".data");

    indentLevel++;

    emit("__bool_true: .asciiz \"true\"");
    emit("__bool_false: .asciiz \"false\"");
    emit("");

    // Runtime error messages
    emit("__div_zero_msg: .asciiz \"RUNTIME ERROR: division by zero!\"");
    emit("");

    for(Integer key : stringLiteralsTable.keySet()) {
      emit("__string%d: .asciiz \"%s\"".formatted(key, stringLiteralsTable.get(key)));
    }
    emit("");

    for(VariableTableEntry variable : globalVariablesTable.toList()) {
      if(variable.type instanceof ArrayVariableType arrayVariableType) {
        emit(".align 2");
        emit("%s: .space %d".formatted(variable.identifier.toLowerCase(), WORD_SIZE * arrayVariableType.size()));
        continue;
      }
      
      emit("%s: .word 0".formatted(variable.identifier.toLowerCase()));
    }

    indentLevel--;

    emit("\n.text\n.globl %s\n\n%s:".formatted(programNode.programIdentifier, programNode.programIdentifier));
    indentLevel++;
  }

  private void emitFooter() {
    emit("");
    indentLevel--;
    emit("exit_program:");
    indentLevel++;
    emit("li $v0, 10");
    emit("syscall");
  }

  private void emitConcatStringWithString(){
    emit("""
  __concat_string_with_string: #a0 = address of first string, $a1 = address of second string
    subu $sp, $sp, 16 
    sw $ra, 12($sp)
    sw $a0, 8($sp)
    sw $a1, 4($sp)

# Calculate the length of the first string
    move $t0, $a0
  __ssc_len1:
    lb $t1, 0($t0)
    beqz $t1, __ssc_len1_end
    addi $t0, $t0, 1
    j __ssc_len1
  __ssc_len1_end:
    sub $t2, $t0, $a0 # t2 = length of first string

# Calculate the length of the second string
    move $t0, $a1
  __ssc_len2:
    lb $t1, 0($t0)
    beqz $t1, __ssc_len2_end
    addi $t0, $t0, 1
    j __ssc_len2
  __ssc_len2_end:
    sub $t3, $t0, $a1 # t3 = length of second string

# Allocate memory for the concatenated string
    add $a0, $t2, $t3
    addi $a0, $a0, 1 # +1 for null terminator
    li $v0, 9
    syscall # $v0 = address of new string

# Load the original string addresses from the stack
    lw $a0, 8($sp) 
    lw $a1, 4($sp)
    move $t0, $v0 # t0 = address of new string

# Copy the first string to the new string
  __ssc_copy1:
    lb $t1, 0($a0)
    beqz $t1, __ssc_copy1_end
    sb $t1, 0($t0)
    addi $a0, $a0, 1
    addi $t0, $t0, 1
    j __ssc_copy1
  __ssc_copy1_end:

# Copy the second string to the new string
  __ssc_copy2:
    lb $t1, 0($a1)
    beqz $t1, __ssc_copy2_end
    sb $t1, 0($t0)
    addi $a1, $a1, 1
    addi $t0, $t0, 1
    j __ssc_copy2
  __ssc_copy2_end:

    sb $zero, 0($t0) # Null terminator
    lw $ra, 12($sp) # Restore return address
    addi $sp, $sp, 16 # Restore stack pointer
    jr $ra
    """);
  }

  private void emitConcatCharWithString() {
    emit("""
  __concat_char_with_string: #a0 = char, $a1 = address of string
    subu $sp, $sp, 16
    sw $ra, 12($sp)
    sw $a0, 8($sp)
    sw $a1, 4($sp)

  # Calculate the length of the string
    move $t0, $a1

  __ccs_len:
    lb $t1, 0($t0)
    beqz $t1, __ccs_len_end
    addi $t0, $t0, 1
    j __ccs_len
  __ccs_len_end:
    sub $t2, $t0, $a1 # t2 = length of string

  # Allocate memory for the concatenated string
    addi $a0, $t2, 2 # +1 for char, +1 for null terminator
    li $v0, 9
    syscall # $v0 = address of new string

    lw $a0, 8($sp) # Load char
    lw $a1, 4($sp) # Load original string address
    move $t0, $v0 # t0 = address of new string

  # Add the char to the new string

    sb $a0, 0($t0)
    addi $t0, $t0, 1
    
  # Copy the original string to the new string
  __ccs_copy:
    lb $t1, 0($a1)
    beqz $t1, __ccs_copy_end
    sb $t1, 0($t0)
    addi $a1, $a1, 1
    addi $t0, $t0, 1
    j __ccs_copy
  __ccs_copy_end:

    sb $zero, 0($t0) # Null terminator
    lw $ra, 12($sp) # Restore return address
    addi $sp, $sp, 16 # Restore stack pointer
    jr $ra
    """);
  }

  private void emitConcatStringWithChar() {
    emit("""
  __concat_string_with_char: #a0 = address of string, $a1 = char
    subu $sp, $sp, 16
    sw $ra, 12($sp)
    sw $a0, 8($sp)
    sw $a1, 4($sp)
    
  # Calculate the length of the string
    move $t0, $a0
    
__csc_len:
    lb $t1, 0($t0)
    beqz $t1, __csc_len_end
    addi $t0, $t0, 1
    j __csc_len
__csc_len_end:
    sub $t2, $t0, $a0 # t2 = length of string

  # Allocate memory for the concatenated string
    addi $a0, $t2, 2 # +1 for char, +1 for null terminator
    li $v0, 9
    syscall # $v0 = address of new string
    
    lw $a0, 8($sp) # Load original string address
    lw $a1, 4($sp) # Load char
    move $t0, $v0 # t0 = address of new string

  # Copy the original string to the new string
  __csc_copy:
    lb $t1, 0($a0)
    beqz $t1, __csc_copy_end
    sb $t1, 0($t0)
    addi $a0, $a0, 1
    addi $t0, $t0, 1
    j __csc_copy
  __csc_copy_end:
  
    sb $a1, 0($t0) # Add the char
    sb $zero, 1($t0) # Null terminator
  
    lw $ra, 12($sp) # Restore return address
    addi $sp, $sp, 16 # Restore stack pointer
    jr $ra
    """);
  }

  private void emitIntegerToStringConversionFunction() {
    emit("""
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
    emit("""
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
    emit("""
__btoa:                       # $a0 = boolean (0 or 1)
    beqz $a0, __btoa_false
    la $v0, __bool_true
    jr $ra

__btoa_false:
    la $v0, __bool_false
    jr $ra
""");
  }

  private void emitStringComparisonFunction() {
    emit("""
# ------------------------------------------------------------
# __strcmp : lexical comparison of two null-terminated strings
#   $a0 = address of left string
#   $a1 = address of right string
#   returns in $v0:  -1 if left < right
#                     0 if left = right
#                     1 if left > right
# clobbers: $t0, $t1  (leaves $a0/$a1 advanced)
# ------------------------------------------------------------
__strcmp:
__strcmp_loop:
    lbu $t0, 0($a0)              # current byte of left  (unsigned!)
    lbu $t1, 0($a1)              # current byte of right
    bne $t0, $t1, __strcmp_diff  # bytes differ -> decide ordering
    beq $t0, $zero, __strcmp_eq  # both zero -> reached end together, equal
    addiu $a0, $a0, 1            # advance both pointers
    addiu $a1, $a1, 1
    j __strcmp_loop

__strcmp_diff:
    bltu $t0, $t1, __strcmp_less # left byte < right byte -> left is smaller
    li $v0, 1                    # otherwise left > right -> left is bigger
    jr $ra

__strcmp_less:
    li $v0, -1
    jr $ra

__strcmp_eq:
    li $v0, 0
    jr $ra
""");
  }

  private void emitPushTemp(String register) {
    emit("# push temp from %s into the stack".formatted(register));

    emit("subu $sp, $sp, 4");
    emit("sw %s, 0($sp)".formatted(register));
    
    emit("# ----------------");
  }

  private void emitPopTemp(String register) {
    emit("# pop stack temp into %s".formatted(register));

    emit("lw %s, 0($sp)".formatted(register));
    emit("addu $sp, $sp, 4");

    emit("# ----------------");
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
    String elseLabel = "__else_label_%d".formatted(uniqueLabelId);
    String endIfLabel = "__end_if_label_%d".formatted(uniqueLabelId);

    visit(node.condition);
    emitPopTemp("$t0");

    // Se aqui for o else, então pula para o elseLabel se a condição for falsa
    if (node.elseStatement.isPresent()) {
      emit("beq $t0, $zero, %s".formatted(elseLabel));
    }
    else {
      emit("beq $t0, $zero, %s".formatted(endIfLabel));
    }

    // Then statement
    visit(node.thenStatement);

    if (node.elseStatement.isPresent()) {
      emit("j %s".formatted(endIfLabel));
      emit("%s:".formatted(elseLabel));
      visit(node.elseStatement.get());
    }

    emit("%s:".formatted(endIfLabel));
    emit("");
  }

  private void visitForStatementNode(ForStatementNode node) {
    int uniqueLabelId = labelCounter++;
    String loopStartLabel = "__for_start_%s".formatted(uniqueLabelId);
    String loopEndLabel = "__for_end_%s".formatted(uniqueLabelId);
    
    visit(node.finalValue);
    visit(node.initialValue);

    emitPopTemp("$t0"); //t0 = initial value
    emitPopTemp("$t1"); //t1 = final value

    emit("sw $t0, %s".formatted(node.controlVariable.identifier.toLowerCase()));
    
    // TODO
    emitPushTemp("$t1");
    
    emit("%s:".formatted(loopStartLabel));

    emit("lw $t0, %s".formatted(node.controlVariable.identifier.toLowerCase()));
    
    emit("lw $t1, 0($sp)"); //t1 = final value

    if(node.isDownto) {
      emit("blt $t0, $t1, %s".formatted(loopEndLabel));
    }
    else {
      emit("bgt $t0, $t1, %s".formatted(loopEndLabel));
    }

    visit(node.body);

    emit("lw $t0, %s".formatted(node.controlVariable.identifier.toLowerCase()));

    if(node.isDownto) {
      emit("addi $t0, $t0, -1");
    }
    else {
      emit("addi $t0, $t0, 1");
    }

    emit("sw $t0, %s".formatted(node.controlVariable.identifier.toLowerCase()));

    emit("j %s".formatted(loopStartLabel));
    emit("%s:".formatted(loopEndLabel));
    emitPopTemp("$t1");
  }

  // TODO: consider local variables
  private void visitAssignmentStatementNode(AssignmentStatementNode node) {
    visit(node.expression);

    String variableIdentifier = node.variableAccessExpressionNode.identifier;

    if (node.variableAccessExpressionNode instanceof IndexedVariableAccessExpressionNode indexedVariableAccessExpressionNode) {
      // TODO: handle index out of bounds exceptions

      visit(indexedVariableAccessExpressionNode.indexExpressionNode);
      emitPopTemp("$t1");
      emitPopTemp("$t2");

      VariableTableEntry variableValue = globalVariablesTable.get(variableIdentifier);
      ArrayVariableType arrayVariableType = (ArrayVariableType) variableValue.type;
      
      emit("addi $t1, $t1, -%d".formatted(arrayVariableType.lowerBound));
      emit("sll $t1, $t1, 2");

      emit("la $t0, %s".formatted(indexedVariableAccessExpressionNode.identifier.toLowerCase()));
      emit("add $t0, $t0, $t1");

      emit("sw $t2, 0($t0)");
    }
    else {
      emitPopTemp("$t0");
      emit("sw $t0, %s".formatted(node.variableAccessExpressionNode.identifier.toLowerCase()));
    }
  }

  private void visitPrimitiveTypeExpressionNode(PrimitiveTypeExpressionNode<?> node) {
    switch (node.value) {
      case Integer value -> {
        emit("li $t0, %d".formatted(value));
        emitPushTemp("$t0");
      }
      case Double value -> {
        int bits = Float.floatToIntBits(value.floatValue());
        emit("li $t0, %d".formatted(bits));
        emitPushTemp("$t0");
      }
      case String value -> {
        int index = stringLiteralsTable.indexOf(value);
        emit("la $t0, __string%d".formatted(index));
        emitPushTemp("$t0");
      }
      case Boolean value -> {
        emit("li $t0, %d".formatted(value ? 1 : 0));
        emitPushTemp("$t0");
      }
      case Character value -> {
        emit("li $t0, %d".formatted((int) value));
        emitPushTemp("$t0");
      }
      default -> throw new RuntimeException("Unsupported primitive type");
    }
  }

  private void handlePlus(ArithmeticOperatorExpressionNode node) {
    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        emit("add $t0, $t0, $t1");
        emitPushTemp("$t0");
      }
      else {
        emit("mtc1 $t0, $f0");
        
        emit("mtc1 $t1, $f2");
        emit("cvt.s.w $f2, $f2");

        emit("add.s $f0, $f0, $f2");
        emit("mfc1 $t0, $f0");
        emitPushTemp("$t0");
      }
    }
    else {
      if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
        if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
          emit("mtc1 $t0, $f0");
          emit("cvt.s.w $f0, $f0");
          
          emit("mtc1 $t1, $f2");
          
          emit("add.s $f0, $f0, $f2");
          emit("mfc1 $t0, $f0");
          emitPushTemp("$t0");
        }
        else {
          emit("mtc1 $t0, $f0");
          emit("cvt.s.w $f0, $f0");
          
          emit("mtc1 $t1, $f2");
          emit("cvt.s.w $f2, $f2");
          
          emit("add.s $f0, $f0, $f2");
          emit("mfc1 $t0, $f0");
          emitPushTemp("$t0");
        }
      }
      else {
        if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
          if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
            // Aloca espaço para a string resultante
            emit("li $a0, 3");
            emit("li $v0, 9");
            emit("syscall"); // o endereço da string alocada estará em $v0

            // Concatena os caracteres
            emit("sb $t0, 0($v0)"); // primeiro caractere no primeiro byte
            emit("sb $t1, 1($v0)"); // segundo caractere no segundo byte
            emit("sb $zero, 2($v0)"); // terminador nulo

            emitPushTemp("$v0"); // empurra o endereço da string resultante para a pilha
          }
          else if
          (node.right.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            emit("move $a0, $t0"); // caractere da esquerda
            emit("move $a1, $t1"); // endereço da string da direita
            emit("jal __concat_char_with_string");
            emitPushTemp("$v0"); // empurra o endereço da string resultante para a pilha
          }
          else {
            throw new RuntimeException("Unsupported primitive type");
          }
        }
        else {
          if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
              emit("move $a0, $t0"); // endereço da primeira string da esquerda
              emit("move $a1, $t1"); // endereço da segunda string da direita
              emit("jal __concat_string_with_string");
              emitPushTemp("$v0"); // empurra o endereço da string resultante para a pilha
            }
            else {
              if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.CHAR) {
                emit("move $a0, $t0"); // endereço da string da esquerda
                emit("move $a1, $t1"); // caractere da direita
                emit("jal __concat_string_with_char");
                emitPushTemp("$v0"); // empurra o endereço da string resultante para a pilha
              }
              else {
                throw new RuntimeException("Unsupported primitive type");
              }
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
    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // no esq = int / no dir = int
    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        emit("sub $t0, $t0, $t1");
        emitPushTemp("$t0");
      }
      // no esq = int / no dir = real
      else {
        emit("mtc1 $t0, $f0");
        emit("cvt.s.w $f0, $f0");
        
        emit("mtc1 $t1, $f2");
        
        emit("sub.s $f0, $f0, $f2");
        emit("mfc1 $t0, $f0");
        emitPushTemp("$t0");
      }
    }
    else {
      // no esq = real / no dir = int
      if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
        if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
          emit("mtc1 $t0, $f0");
          emit("cvt.s.w $f0, $f0");
          
          emit("mtc1 $t1, $f2");
          
          emit("sub.s $f0, $f0, $f2");
          emit("mfc1 $t0, $f0");
          emitPushTemp("$t0");
        }
        // no esq = real / no dir = real
        else {
          emit("mtc1 $t0, $f0");
          emit("cvt.s.w $f0, $f0");
          
          emit("mtc1 $t1, $f2");
          emit("cvt.s.w $f2, $f2");
          
          emit("sub.s $f0, $f0, $f2");
          emit("mfc1 $t0, $f0");
          emitPushTemp("$t0");
        }
      }
      else {
        throw new RuntimeException("Unsupported primitive type");
      }
    }
  }

  private void handleMultiplication(ArithmeticOperatorExpressionNode node) {
    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // no esq = int / no dir = int
    if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
        emit("mul $t0, $t0, $t1");
        emitPushTemp("$t0");
      }
      // no esq = int / no dir = real
      else {
        emit("mtc1 $t0, $f0");
        emit("cvt.s.w $f0, $f0");
        
        emit("mtc1 $t1, $f2");
        
        emit("mul.s $f0, $f0, $f2");
        emit("mfc1 $t0, $f0");
        emitPushTemp("$t0");
      }
    }
    else {
      // no esq = real / no dir = int
      if (node.left.type.basePrimitiveType == PrimitiveTypeEnum.REAL) {
        if (node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
          emit("mtc1 $t0, $f0");
          
          emit("mtc1 $t1, $f2");
          emit("cvt.s.w $f2, $f2");
          
          emit("mul.s $f0, $f0, $f2");
          emit("mfc1 $t0, $f0");
          emitPushTemp("$t0");
        }
        // no esq = real / no dir = real
        else {
          emit("mtc1 $t0, $f0");
          emit("cvt.s.w $f0, $f0");
          
          emit("mtc1 $t1, $f2");
          emit("cvt.s.w $f2, $f2");
          
          emit("mul.s $f0, $f0, $f2");
          emit("mfc1 $t0, $f0");
          emitPushTemp("$t0");
        }
      }
      else {
        throw new RuntimeException("Unsupported primitive type");      
      }
    }
  }

  private void handleRealDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // --- Check if divisor is not equal to zero

    int id = labelCounter++;
    String okLabel = "real_division_ok_%d".formatted(id);
    
    emit("bnez $t1, %s".formatted(okLabel));
    
    // Print error message
    emit("la $a0, __div_zero_msg");
    emit("li $v0, 4");
    emit("syscall");
    
    // Exit program
    emit("li $v0, 10");
    emit("syscall");

    // Safe to continue
    emit("%s:".formatted(okLabel));

    // ------------------------------------------

    emit("mtc1 $t0, $f0");
    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      emit("cvt.s.w $f0, $f0");
    }

    emit("mtc1 $t1, $f2");
    if(node.right.type.basePrimitiveType == PrimitiveTypeEnum.INTEGER) {
      emit("cvt.s.w $f2, $f2");
    }

    emit("div.s $f0, $f0, $f2");
    emit("mfc1 $t0, $f0");
    emitPushTemp("$t0");
  }

  private void handleIntegerDivision(ArithmeticOperatorExpressionNode node) {
    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    // --- Check if divisor is not equal to zero

    int id = labelCounter++;
    String okLabel = "integer_division_ok_%d".formatted(id);
    
    emit("bnez $t1, %s".formatted(okLabel));
    
    // Print error message
    emit("la $a0, __div_zero_msg");
    emit("li $v0, 4");
    emit("syscall");
    
    // Exit program
    emit("li $v0, 10");
    emit("syscall");

    // Safe to continue
    emit("%s:".formatted(okLabel));

    // ------------------------------------------

    emit("div $t0, $t1");
    emit("mflo $t0");
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

  // TODO: handle local variables of functions
  private void visitIndexedVariableAccessExpressionNode(IndexedVariableAccessExpressionNode node) {
    visit(node.indexExpressionNode);
    emitPopTemp("$t1");
    
    if (node.type instanceof PrimitiveVariableType && node.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
      // TODO: handle string out of bounds exception
      emit("lw $t0, %s".formatted(node.identifier.toLowerCase()));
      emit("add $t0, $t0, $t1");
      emit("lbu $t0, 0($t0)");
      emitPushTemp("$t0");
      return;
    }

    // TODO: handle array out of bounds exception
    
    VariableTableEntry symbol = globalVariablesTable.get(node.identifier);
    ArrayVariableType arrayType = (ArrayVariableType) symbol.type;
    
    emit("la $t0, %s".formatted(node.identifier.toLowerCase()));
    emit("addi $t1, $t1, -%d".formatted(arrayType.lowerBound));
    emit("sll $t1, $t1, 2");
    emit("add $t0, $t0, $t1");
    emit("lw $t0, 0($t0)");
    emitPushTemp("$t0");
  }

  private void visitVariableAccessExpressionNode(VariableAccessExpressionNode node) {
    switch (node.type) {
      case PrimitiveVariableType _ -> {
        emit("lw $t0, %s".formatted(node.identifier.toLowerCase()));
        emitPushTemp("$t0");
      }
      case ArrayVariableType _ -> {
        emit("la $t0, %s".formatted(node.identifier.toLowerCase()));
        emitPushTemp("$t0");
      }
      default -> throw new RuntimeException("Unsupported type");
    }
  }

  private void visitLogicOperatorExpressionNode(LogicOperatorExpressionNode node) {
    visit(node.left);
    visit(node.right);

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    switch (node.operator) {
      case "and" -> emit("and $t0, $t0, $t1");
      case "or" -> emit("or $t0, $t0, $t1");
      default -> throw new RuntimeException("Unsupported logic operator");
    }

    emitPushTemp("$t0");
  }

  private void visitNotOperatorExpressionNode(NotOperatorExpressionNode node) {
    visit(node.expression);
    emitPopTemp("$t0");
    emit("xori $t0, $t0, 1");
    emitPushTemp("$t0");
  }

  private void visitIntegerToRealExpressionNode(IntegerToRealExpressionNode node) {
    visit(node.expression);
    emitPopTemp("$t0");
    
    emit("mtc1 $t0, $f0");
    emit("cvt.s.w $f0, $f0");
    emit("mfc1 $t0, $f0");
    emitPushTemp("$t0");
  }

  private void visitCharToStringExpressionNode(CharToStringExpressionNode node) {
    visit(node.expression);
    emitPopTemp("$t0");
    
    emit("li $v0, 9");
    emit("li $a0, 4");
    emit("syscall");

    emit("sb $t0, 0($v0)");
    emit("sb $zero, 1($v0)");

    emit("move $t0, $v0");
    emitPushTemp("$t0");
  }

  private void visitComparisonOperatorExpressionNode(ComparisonOperatorExpressionNode node) {
    String operator = node.operator;

    visit(node.left);
    visit(node.right);
    
    if(node.left.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
      emitPopTemp("$a1");
      emitPopTemp("$a0");      

      emit("jal __strcmp");

      switch (operator) {
        case "=" -> emit("sltiu $t0, $v0, 1");
        case "<>" -> emit("sltu $t0, $zero, $v0");
        case "<" -> emit("slt $t0, $v0, $zero");
        case ">" -> emit("slt $t0, $zero, $v0");
        case "<=" -> {
          emit("slt $t0, $zero, $v0");
          emit("xori $t0, $t0, 1");
        }
        case ">=" -> {
          emit("slt $t0, $v0, $zero");
          emit("xori $t0, $t0, 1");
        }
        default -> throw new RuntimeException("Unsupported operation");
      }

      emitPushTemp("$t0");
      return;
    }

    emitPopTemp("$t1");
    emitPopTemp("$t0");

    switch (operator) {
      case "=" -> {
        emit("subu $t0, $t0, $t1");
        emit("sltiu $t0, $t0, 1");
      }
      case "<>" -> {
        emit("subu $t0, $t0, $t1");
        emit("sltu $t0, $zero, $t0");
      }
      case "<" -> {
        emit("slt $t0, $t0, $t1");
      }
      case ">" -> {
        emit("slt $t0, $t1, $t0");
      }
      case "<=" -> {
        emit("slt $t0, $t1, $t0");
        emit("xori $t0, $t0, 1");
      }
      case ">=" -> {
        emit("slt $t0, $t0, $t1");
        emit("xori $t0, $t0, 1");
      }
      default -> throw new RuntimeException("Unsupported operation");
    }

    emitPushTemp("$t0");
  }

  // TODO: handle non built-in procedures
  private void visitProcedureCallStatementNode(ProcedureCallStatementNode node) {
    if (builtInProceduresAndFunctionsTable.lookProcedureOrFunction(node.procedureIdentifier)) {
      for (ExpressionNode argument : node.arguments) {
        visit(argument);
      }

      PrimitiveTypeEnum firstArgumentType = node.arguments.isEmpty() ? null : node.arguments.get(0).type.basePrimitiveType;

      executeBuiltInProcedureOrFunction(node.procedureIdentifier.toLowerCase(), firstArgumentType);
      return;
    }
  }

  // TODO: handle non built-in functions
  private void visitFunctionCallStatementNode(FunctionCallExpressionNode node) {
    if (builtInProceduresAndFunctionsTable.lookProcedureOrFunction(node.functionIdentifier)) {
      for (ExpressionNode argument : node.arguments) {
        visit(argument);
      }

      PrimitiveTypeEnum firstArgumentType = node.arguments.isEmpty() ? null : node.arguments.get(0).type.basePrimitiveType;

      executeBuiltInProcedureOrFunction(node.functionIdentifier.toLowerCase(), firstArgumentType);
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
        emit("li $v0, 4");
        emit("syscall");
      }
      case "writeln" -> {
        emitPopTemp("$a0");
        emit("li $v0, 4");
        emit("syscall");

        emit("li $a0, 10");
        emit("li $v0, 11");
        emit("syscall");
      }
      case "itos" -> {
        emitPopTemp("$a0");
        emit("jal __itoa");
        emit("move $t0, $v0");
        emitPushTemp("$t0");
      }
      case "rtos" -> {
        emitPopTemp("$a0");
        emit("jal __rtoa");
        emit("move $t0, $v0");
        emitPushTemp("$t0");
      }
      case "btos" -> {
        emitPopTemp("$a0");
        emit("jal __btoa");
        emit("move $t0, $v0");
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
