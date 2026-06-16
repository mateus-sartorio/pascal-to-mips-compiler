#!/bin/bash

# Definição de cores
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m'

TESTS_DIR="tests/ast"
FAILED=0

if [ ! -d "$TESTS_DIR" ] || [ -z "$(ls -A "$TESTS_DIR" 2>/dev/null)" ]; then
    exit 0
fi

echo -e "${BLUE}=== RODANDO TESTES DA AST ===${NC}"

for infile in $(find "$TESTS_DIR" -name "source.pas" | sort); do
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"
    
    expected="$case_dir/expected_result.dot"
    actual="$case_dir/actual_result.dot"
    png_file="$case_dir/actual_result.png"
    diff_file="$case_dir/diff.diff"

    if [ ! -f "$expected" ]; then
        echo -e "${PURPLE}[AST] $case_name - EXPECTED RESULT MISSING${NC}"
        continue
    fi

    # Executa a tarefa do Makefile (que já cria o actual_result.dot e .png no lugar certo)
    make -s run-ast FILE="$infile" > /dev/null 2>&1
    
    # Compara a árvore DOT gerada com a árvore esperada
    diff -u "$expected" "$actual" > "$diff_file"

    if [ -s "$diff_file" ]; then
        echo -e "${RED}[FAIL] $case_name${NC}"
        FAILED=$((FAILED + 1))
    else
        echo -e "${GREEN}[PASS] $case_name${NC}"
    fi
done

exit $FAILED