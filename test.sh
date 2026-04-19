#!/bin/bash

SRC_DIR=src

TESTS_ROOT_DIR=tests
VALID_TESTS_DIR=$TESTS_ROOT_DIR/valid
INVALID_TESTS_DIR=$TESTS_ROOT_DIR/invalid

clean_only=false
if [ "$1" = "--clean" ]; then
    clean_only=true
fi

if [ "$clean_only" = false ]; then
    make
fi

compiler_modules=("lexer" "parser")
failed_tests=0

for infile in "$VALID_TESTS_DIR"/*/source.pas "$INVALID_TESTS_DIR"/*/source.pas; do
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"
    case_group="$(basename "$(dirname "$case_dir")")"

    # If we're only cleaning, skip the test execution
    if [ "$clean_only" = true ]; then
        printf '\033[1mCLEANED: %s\033[0m\n' "$case_name"
        continue
    fi

    # Run the test and capture output
    printf '\033[1mTEST CASE: [%s] %s\033[0m\n' "${case_group^^}" "$case_name"

    for module in "${compiler_modules[@]}"; do
        module_dir="$case_dir/${module}"
        
        expected_module_file="$module_dir/expected_result.txt"
        actual_module_file="$module_dir/actual_result.txt"
        diff_module_file="$module_dir/diff.diff"

        # Ensure module's directory exists and clean previous results
        mkdir -p "$module_dir"
        rm -f "$actual_module_file" "$diff_module_file"

        if [ ! -f "$expected_module_file" ]; then
            printf '\033[35m[%s] - EXPECTED RESULT FILE NOT FOUND\033[0m\n' "${module^^}"
            continue
        fi

        make -s "run-${module}" FILE="$infile" > "$actual_module_file" 2>&1

        diff -u "$expected_module_file" "$actual_module_file" > "$diff_module_file"

        if [ -s "$diff_module_file" ]; then
            printf '\033[31m[%s] - FAIL ❌\033[0m\n' "${module^^}"
            failed_tests=$((failed_tests + 1))
        else
            printf '\033[32m[%s] - PASS ✅\033[0m\n' "${module^^}"
        fi
    done

    printf '\n'
done

if [ "$clean_only" = false ] && [ "$failed_tests" -gt 0 ]; then
    printf '\n\033[1;31m%d test(s) failed.\033[0m\n' "$failed_tests"
    exit 1
fi

exit 0