#!/bin/bash

# Configurações
GRAMMAR_NAME="pascal_grammar"
GEN_PATH="src/lexer"
INVALID_TESTS_DIR="tests/invalid"
ANTLR_JAR="../../libs/antlr-4.13.2-complete.jar"
failed_tests=0

# Procura por todas as pastas dentro de INVALID_TESTS_DIR
for test_case in $INVALID_TESTS_DIR/*; do
    if [ -d "$test_case" ]; then
        TEST_NAME=$(basename "$test_case")
        echo -n "Testando [$TEST_NAME]... "

        # Caminhos dos arquivos
        SOURCE_FILE="../../$test_case/source.pas"
        LEXER_DIR="$test_case/lexer"
        ACTUAL="$LEXER_DIR/actual_result.txt"
        EXPECTED="$LEXER_DIR/expected_result.txt"
        DIFF_FILE="$LEXER_DIR/diff.diff"

        # Garante que a pasta lexer existe
        mkdir -p "$LEXER_DIR"

        # Roda o grun.
        cd $GEN_PATH
        java -cp .:$ANTLR_JAR org.antlr.v4.gui.TestRig $GRAMMAR_NAME tokens -tokens "$SOURCE_FILE" > "../../$ACTUAL" 2>&1
        cd ../..

        if [ ! -f "$EXPECTED" ]; then
            echo "⚠️  Ignorado (expected_result.txt não encontrado)"
            continue
        fi

        diff -u "$EXPECTED" "$ACTUAL" > "$DIFF_FILE"

        if [ -s "$DIFF_FILE" ]; then
            echo "❌ FALHOU (Saída diferente do esperado. Veja $DIFF_FILE)"
            failed_tests=$((failed_tests + 1))
        else
            echo "✅ PASSOU"
            rm -f "$DIFF_FILE" # Limpa o diff se passou
        fi
    fi
done

if [ "$failed_tests" -gt 0 ]; then
    exit 1
fi

exit 0
