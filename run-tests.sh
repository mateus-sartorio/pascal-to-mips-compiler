#!/bin/bash

SRC_DIR=src
TESTS_DIR=tests

clean_only=false
if [ "$1" = "--clean" ]; then
    clean_only=true
fi

if [ "$clean_only" = false ]; then
    make
fi

for infile in "$TESTS_DIR"/*/source.pas; do
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"
    
    lexer_dir="$case_dir/lexer"
    
    expected_file="$lexer_dir/expected_result.txt"
    actual_file="$lexer_dir/actual_result.txt"
    diff_file="$lexer_dir/diff.diff"

    # Ensure lexer directory exists and clean previous results
    mkdir -p "$lexer_dir"
    rm -f "$actual_file" "$diff_file"

    # If we're only cleaning, skip the test execution
    if [ "$clean_only" = true ]; then
        printf 'Cleaned %s\n' "$case_name"
        continue
    fi

    # Run the test and capture output

    printf 'Running tests for %s\n' "$case_name"

    make -s run FILE="$infile" > "$actual_file"

    if [ -f "$expected_file" ]; then
        diff -u "$expected_file" "$actual_file" > "$diff_file"
    fi

    if [ -s "$diff_file" ]; then
        printf '\033[31mFAIL ❌\033[0m %s\n' "$case_name"
    else
        printf '\033[32mPASS ✅\033[0m %s\n' "$case_name"
    fi
done