#!/bin/bash
#
# test_compiler.sh — Gerador de gabaritos do backend MIPS (ASM + MARS).
#
# Para cada tests/compiler/**/source.pas este script:
#   1. Compila o programa           -> <caso>/actual_code_result.asm
#   2. Grava o ASM como gabarito     -> <caso>/expected_code_result.asm
#   3. Roda o ASM no MARS e grava a  -> <caso>/expected_result.txt
#      saída de execução como gabarito
#
# Casos cuja compilação ou execução no MARS falhar são PULADOS: os gabaritos
# existentes NÃO são sobrescritos, evitando gravar lixo por cima de um gabarito
# válido. Se houver input.txt no diretório do caso, ele é enviado ao stdin do
# programa em execução no MARS.

set -u

# Roda sempre a partir da raiz do projeto (onde este script está)
cd "$(dirname "$0")" || exit 1

# Cores
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m'

TESTS_DIR="tests/compiler"
MARS_TIMEOUT=30
MARS_ERROR_MARKER="Processing terminated due to errors."

RECORDED=0
SKIPPED=0

# Limpa arquivos temporários ao sair
TMP_FILES=()
cleanup() { [ ${#TMP_FILES[@]} -gt 0 ] && rm -f "${TMP_FILES[@]}"; }
trap cleanup EXIT

# Localiza o JAR do MARS uma única vez
mars_jar="$(ls libs/Mars*.jar libs/mars*.jar Mars*.jar mars*.jar lib/Mars*.jar lib/mars*.jar 2>/dev/null | head -n 1)"
if [ -z "$mars_jar" ]; then
    echo -e "${RED}[ERRO] JAR do MARS não encontrado (procurei em libs/Mars*.jar, lib/Mars*.jar, ./Mars*.jar).${NC}"
    exit 1
fi

if [ ! -d "$TESTS_DIR" ] || [ -z "$(ls -A "$TESTS_DIR" 2>/dev/null)" ]; then
    echo -e "${YELLOW}Diretório de testes '$TESTS_DIR' vazio ou inexistente. Nada a fazer.${NC}"
    exit 0
fi

echo -e "${BLUE}=== GERANDO GABARITOS DO COMPILADOR (ASM + MARS) ===${NC}"
echo -e "${BLUE}MARS: $mars_jar${NC}"

# Mostra as primeiras linhas de um arquivo, indentadas (para diagnóstico)
show_snippet() { head -n 8 "$1" | sed 's/^/         /'; }

while IFS= read -r infile; do
    case_dir="$(dirname "$infile")"
    case_name="$(basename "$case_dir")"

    actual_asm="$case_dir/actual_code_result.asm"
    expected_asm="$case_dir/expected_code_result.asm"
    actual_txt="$case_dir/actual_result.txt"
    expected_txt="$case_dir/expected_result.txt"

    # 1. Compila -> actual_code_result.asm
    #    stderr vai para arquivo separado para não corromper o .asm gerado.
    compiler_err="$(mktemp)"; TMP_FILES+=("$compiler_err")
    if ! make -s run-compiler FILE="$infile" > "$actual_asm" 2> "$compiler_err"; then
        echo -e "${RED}[SKIP] $case_name — falha na compilação${NC}"
        show_snippet "$compiler_err"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi

    # 2. Executa o ASM gerado no MARS
    #    'nc'  -> sem aviso de copyright/GUI (modo linha de comando)
    #    'ae1' -> sai com código != 0 se houver erro de montagem
    if [ -f "$case_dir/input.txt" ]; then
        stdin_file="$case_dir/input.txt"
    else
        stdin_file="/dev/null"
    fi

    mars_out="$(mktemp)"; TMP_FILES+=("$mars_out")
    mars_err="$(mktemp)"; TMP_FILES+=("$mars_err")
    timeout "$MARS_TIMEOUT" java -jar "$mars_jar" nc ae1 "$actual_asm" \
        < "$stdin_file" > "$mars_out" 2> "$mars_err"
    mars_rc=$?

    # Saída real desta execução (sucesso OU erro), para inspeção
    cp "$mars_out" "$actual_txt"

    # 2a. Timeout (provável loop infinito no código gerado)
    if [ "$mars_rc" -eq 124 ]; then
        echo -e "${RED}[SKIP] $case_name — MARS excedeu ${MARS_TIMEOUT}s (possível loop infinito)${NC}"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi

    # 2b. Erro de montagem/execução: código != 0 OU marcador de erro na saída.
    #     (divisão inteira por zero no MARS não gera erro — é gravada normalmente.)
    if [ "$mars_rc" -ne 0 ] || grep -qF "$MARS_ERROR_MARKER" "$mars_out" "$mars_err"; then
        echo -e "${RED}[SKIP] $case_name — erro de montagem/execução no MARS${NC}"
        show_snippet "$mars_out"
        show_snippet "$mars_err"
        SKIPPED=$((SKIPPED + 1))
        continue
    fi

    # 3. Grava os gabaritos (ASM e saída de execução)
    cp "$actual_asm" "$expected_asm"
    cp "$mars_out"   "$expected_txt"

    # Remove diffs antigos, sem sentido no modo de gravação
    rm -f "$case_dir/diff.diff" "$case_dir/diff_asm.diff" "$case_dir/diff_txt.diff"

    echo -e "${GREEN}[PASS] $case_name${NC}"
    RECORDED=$((RECORDED + 1))
done < <(find "$TESTS_DIR" -name "source.pas" | sort)

# Sai com código != 0 se algum caso não pôde ser gravado
[ "$SKIPPED" -eq 0 ]
