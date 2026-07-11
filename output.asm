.data
string0: .asciiz "test"
__bool_true: .asciiz "true"
__bool_false: .asciiz "false"
c: .word 0

.text
.globl p1
p1:
la $t0, c
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
li $a0, 10
li $v0, 11
syscall
li $v0, 10
syscall
__itoa:                       # $a0 = integer
    move $t0, $a0             # $t0 = n  (save it; syscall 9 needs $a0)
    li $v0, 9                 # allocate a fixed 12-byte buffer
    li $a0, 12                #   max is "-2147483648\0" = 12 bytes exactly
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
__btoa:                       # $a0 = boolean (0 or 1)
    beqz $a0, __btoa_false
    la $v0, __bool_true
    jr $ra

__btoa_false:
    la $v0, __bool_false
    jr $ra
