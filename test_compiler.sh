#!/bin/bash

# Definição de cores
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
YELLOW='\033[0;33m'
NC='\033[0m'

TESTS_DIR="tests/compiler"
MARS_TIMEOUT=30
FAILED=0

# Localiza o JAR do MARS uma única vez
mars_jar="$(ls libs/Mars*.jar libs/mars*.jar Mars*.jar mars*.jar lib/Mars*.jar lib/mars*.jar 2>/dev/null | head -n 1)"
if [ -z "$mars_jar" ]; then
    echo -e "${RED}[ERRO] JAR do MARS não encontrado (procurei em libs/, lib/ ou na raiz).${NC}"
    exit 1
fi

if [ ! -d "$TESTS_DIR" ] || [ -z "$(ls -A "$TESTS_DIR" 2>/dev/null)" ]; then
    echo -e "${YELLOW}Diretório de testes '$TESTS_DIR' vazio ou inexistente.${NC}"
    exit 0
fi

echo -e "${BLUE}=== RODANDO TESTES DO COMPILADOR (MIPS + MARS) ===${NC}"

for infile in $(find "$TESTS_DIR" -name "source.pas" | sort); do
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"
    
    actual_asm="$case_dir/actual_code_result.asm"
    expected_txt="$case_dir/expected_result.txt"
    actual_txt="$case_dir/actual_result.txt"
    diff_file="$case_dir/diff.diff"

    if [ ! -f "$expected_txt" ]; then
        echo -e "${PURPLE}[COMPILER] $case_name - EXPECTED RESULT MISSING${NC}"
        continue
    fi

    # 1. Compila o arquivo Pascal gerando o código MIPS correspondente (.asm)
    compiler_err=$(mktemp)
    if ! make -s run-compiler FILE="$infile" > "$actual_asm" 2> "$compiler_err"; then
        echo -e "${RED}[FAIL] $case_name - Falha na compilação do compilador (Java Runtime Error)${NC}"
        sed 's/^/         /' "$compiler_err"
        FAILED=$((FAILED + 1))
        rm -f "$compiler_err"
        continue
    fi
    rm -f "$compiler_err"

    # 2. Configura a entrada padrão (input.txt) se o caso de teste exigir dados
    if [ -f "$case_dir/input.txt" ]; then
        stdin_file="$case_dir/input.txt"
    else
        stdin_file="/dev/null"
    fi

    # 3. Executa o assembly gerado dentro do simulador MARS
    # 'nc'  -> ignora telas gráficas e mensagens de copyright
    # 'ae1' -> define código de saída como incompatível caso falte alguma instrução estrutural
    timeout "$MARS_TIMEOUT" java -jar "$mars_jar" nc ae1 "$actual_asm" < "$stdin_file" > "$actual_txt" 2>&1
    mars_rc=$?

    # 3a. Valida estouro de tempo (Loops infinitos gerados pelo gerador de código)
    if [ "$mars_rc" -eq 124 ]; then
        echo -e "${RED}[FAIL] $case_name - Execução no MARS estourou o tempo limite de ${MARS_TIMEOUT}s (Loop Infinito?)${NC}"
        FAILED=$((FAILED + 1))
        continue
    fi

    # 4. Compara a saída obtida pelo simulador com o gabarito esperado
    diff -w -u "$expected_txt" "$actual_txt" > "$diff_file"

    if [ -s "$diff_file" ]; then
        echo -e "${RED}[FAIL] $case_name${NC}"
        sed 's/^/         /' "$diff_file"
        FAILED=$((FAILED + 1))
    else
        echo -e "${GREEN}[PASS] $case_name${NC}"
        rm -f "$diff_file" # Limpa o arquivo de diff se o teste passou com sucesso
    fi
done

exit $FAILED