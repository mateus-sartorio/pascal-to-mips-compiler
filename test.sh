#!/bin/bash

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
YELLOW='\033[0;33m'
NC='\033[0m'

run_module() {
    local module=$1
    local dir="tests/$module"
    
    if [ ! -d "$dir" ] || [ -z "$(ls -A "$dir" 2>/dev/null)" ]; then return; fi

    echo -e "${BLUE}=== RODANDO TESTES DO ${module^^} ===${NC}"
    
    local ext="txt"
    if [ "$module" = "ast" ]; then
        ext="dot"
    fi

    for infile in $(find "$dir" -name "source.pas" | sort); do
        case_dir="$(dirname "$infile")"
        case_name="$(basename "$case_dir")"
        expected="$case_dir/expected_result.$ext"
        actual="$case_dir/actual_result.$ext"
        diff_file="$case_dir/diff.diff"

        if [ ! -f "$expected" ]; then
            echo -e "${PURPLE}[${module^^}] $case_name - EXPECTED RESULT MISSING${NC}"
            continue
        fi

        if [ "$module" = "ast" ]; then
            make -s run-ast FILE="$infile" > /dev/null 2>&1
        else
            make -s "run-$module" FILE="$infile" > "$actual" 2>&1
        fi

        diff -w -u "$expected" "$actual" > "$diff_file"

        if [ -s "$diff_file" ]; then
            echo -e "${RED}[FAIL] $case_name${NC}"
        else
            echo -e "${GREEN}[PASS] $case_name${NC}"
            rm -f "$diff_file"
        fi
    done
}

case "$1" in
    "--lexer")
        run_module "lexer"
        ;;
    "--parser")
        run_module "parser"
        ;;
    "--semantic")
        run_module "semantic"
        ;;
    "--ast")
        run_module "ast"
        ;;
    "--interpreter")
        run_module "interpreter"
        ;;
    "--clean")
        find tests -name "actual_result.txt" -delete
        find tests -name "actual_result.dot" -delete
        find tests -name "actual_result.png" -delete
        find tests -name "diff.diff" -delete
        echo -e "${YELLOW}Logs e artefatos limpos.${NC}"
        ;;
    *)
        run_module "lexer"
        run_module "parser"
        run_module "semantic"
        run_module "ast"
        run_module "interpreter"
        ;;
esac