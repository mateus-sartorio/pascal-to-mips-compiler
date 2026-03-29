#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR"
SRC_DIR="$PROJECT_ROOT/src"
TESTS_DIR="$PROJECT_ROOT/tests"

make -C "$SRC_DIR"

for infile in "$TESTS_DIR"/*/source.pas; do
    [ -e "$infile" ] || continue
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"
    lexer_dir="$case_dir/lexer"
    expected_file="$lexer_dir/expected_result.txt"
    actual_file="$lexer_dir/actual_result.txt"
    diff_file="$lexer_dir/diff.txt"

    printf 'Running tests for %s\n' "$case_name"

    mkdir -p "$lexer_dir"
    make -s -C "$SRC_DIR" run FILE="$infile" > "$actual_file" 2>&1

    if [ -f "$expected_file" ]; then
        diff -u "$expected_file" "$actual_file" > "$diff_file" || true
    else
        printf 'Missing expected result file: %s\n' "$expected_file" > "$diff_file"
    fi

    if [ -s "$diff_file" ]; then
        printf '\033[31mFAIL ❌\033[0m %s\n' "$case_name"
    else
        printf '\033[32mPASS ✅\033[0m %s\n' "$case_name"
    fi
done