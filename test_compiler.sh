#!/bin/bash

# Definição de cores
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m'

TESTS_DIR="tests/compiler"
FAILED=0

if [ ! -d "$TESTS_DIR" ] || [ -z "$(ls -A "$TESTS_DIR" 2>/dev/null)" ]; then
    exit 0
fi

echo -e "${BLUE}=== RODANDO TESTES DO COMPILADOR===${NC}"

for infile in $(find "$TESTS_DIR" -name "source.pas" | sort); do
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"
    
    expected="$case_dir/expected_code_output.asm"
    actual="$case_dir/actual_code_output.asm"
    diff_file="$case_dir/diff.diff"

    if [ ! -f "$expected" ]; then
        echo -e "${PURPLE}[INTERPRETER] $case_name - EXPECTED OUTPUT MISSING${NC}"
        continue
    fi

    # Executa a tarefa do Makefile
    make -s run-compiler FILE="$infile" > "$actual"
    
    # Compara a saída gerada com a saída esperada
    diff -w -u "$expected" "$actual" > "$diff_file"

    if [ -s "$diff_file" ]; then
        echo -e "${RED}[FAIL] $case_name${NC}"
        FAILED=$((FAILED + 1))
    else
        echo -e "${GREEN}[PASS] $case_name${NC}"
    fi
done

exit $FAILED