# Compilador de Pascal para MIPS

Trabalho acadêmico que implementa um compilador para um subconjunto da linguagem Pascal (baseado na norma **ISO 7185**, disponível em `docs/iso7185.pdf`), gerando código assembly **MIPS** executável no simulador **MARS**.

O projeto é escrito em **Java**, usa **ANTLR4** para gerar o analisador léxico e sintático a partir de gramáticas `.g4`, e implementa manualmente as demais etapas do compilador (análise semântica, construção de AST, interpretador e gerador de código).

## Sumário

- [Fluxo do compilador](#fluxo-do-compilador)
- [Estrutura do repositório](#estrutura-do-repositório)
- [Pré-requisitos](#pré-requisitos)
- [Compilando o projeto (build)](#compilando-o-projeto-build)
- [Executando o compilador manualmente](#executando-o-compilador-manualmente)
- [Rodando os testes automatizados](#rodando-os-testes-automatizados)
- [Estrutura de um caso de teste](#estrutura-de-um-caso-de-teste)
- [Integração contínua](#integração-contínua)
- [Licença](#licença)

## Fluxo do compilador

O ponto de entrada é `src/Main.java`, que recebe o caminho de um arquivo `.pas` e uma flag opcional indicando o que deve ser feito com ele. O pipeline é sempre o mesmo até a análise semântica; a partir daí, a flag escolhe qual "backend" será executado:

1. **Análise léxica** — `PascalLexer` (gerado pelo ANTLR a partir de `src/PascalLexer.g4`) lê o arquivo fonte e produz os tokens.
2. **Análise sintática** — `PascalParser` (gerado a partir de `src/PascalParser.g4`) consome os tokens e monta a *parse tree*. Se houver erro de sintaxe, a compilação para aqui.
3. **Análise semântica** — a classe `SemanticChecker` (`src/checker`) percorre a parse tree, valida tipos e regras semânticas, e monta as tabelas de símbolos: variáveis globais, procedimentos/funções (declarados no programa e embutidos/*built-in*) e literais de string.
4. **Construção da AST** — `AstBuilder` (`src/ast`) percorre a parse tree novamente, agora usando as tabelas geradas na etapa anterior, e monta a Árvore Sintática Abstrata (AST) do programa (`ProgramNode`).
5. A partir da AST, existem dois caminhos possíveis:
   - **Interpretador** (`src/interpreter`) — executa o programa diretamente, percorrendo a AST. Útil para validar a semântica do programa sem se preocupar com geração de código.
   - **Gerador de código** (`src/codegenerator`) — percorre a AST e emite código assembly **MIPS** equivalente, pronto para ser executado no simulador **MARS**.

As flags aceitas por `Main` (segundo argumento da linha de comando) são:

| Flag | Efeito |
|---|---|
| `-print-tables` | Imprime as tabelas de símbolos geradas na análise semântica |
| `-print-ast` | Gera um arquivo `actual_result.dot` (notação DOT) com a AST construída |
| `-interpret` | Executa o programa através do interpretador |
| `-compiler` | Gera o código MIPS em `actual_code_result.asm` |

Todas essas etapas são expostas como *targets* do `Makefile`, descritos mais abaixo.

## Estrutura do repositório

```
├── Makefile               # orquestra build, execução e testes
├── src/
│   ├── PascalLexer.g4      # gramática léxica (ANTLR)
│   ├── PascalParser.g4     # gramática sintática (ANTLR)
│   ├── Main.java           # ponto de entrada, orquestra o pipeline
│   ├── checker/            # analisador semântico
│   ├── ast/                # construção da AST
│   ├── interpreter/        # interpretador (executa a partir da AST)
│   ├── codegenerator/      # gerador de código MIPS (a partir da AST)
│   ├── tables/             # tabelas de símbolos (variáveis, funções, literais)
│   └── types/              # sistema de tipos da linguagem
├── libs/
│   ├── antlr-4.13.2-complete.jar   # ANTLR, já incluso no repositório
│   └── Mars4_5.jar                 # simulador MIPS usado para rodar o .asm gerado
├── tests/                  # casos de teste automatizados, um subdiretório por etapa
│   ├── lexer/  parser/  semantic/  ast/  interpreter/  compiler/
│   └── .../valid|invalid/<nome-do-caso>/source.pas + gabarito
├── test.sh                 # script principal chamado por "make test"
├── test_compiler.sh        # roda a suíte do gerador de código (MIPS + MARS)
├── test_lexer.sh, test_parser.sh, test_semantic.sh,
│   test_ast.sh, test_interpreter.sh   # versões standalone/legadas de cada suíte
│                                        # (não são chamadas pelo Makefile, mas podem
│                                        # ser rodadas isoladamente com "bash <script>")
└── docs/
    ├── project_specification.pdf       # especificação do trabalho
    ├── iso7185.pdf                     # norma Pascal usada como referência
    └── AST Class Diagram.jpg           # diagrama de classes da AST
```

## Pré-requisitos

- **JDK** instalado (compilador e runtime Java).
  > Recomenda-se o JDK 25+ via [SDKMAN](https://sdkman.io/):
  > ```bash
  > curl -s "https://get.sdkman.io" | bash
  > sdk install java 25.0.2-tem
  > sdk use java 25.0.2-tem
  > ```
- **Graphviz** (`dot`) instalado — necessário para gerar as imagens `.png` da AST (`make run-ast` e os testes de AST).
- **ANTLR** já vem embutido no repositório (`libs/antlr-4.13.2-complete.jar`) e configurado no `Makefile` — não é necessário instalar nada à parte.
- **MARS** (simulador MIPS) também já vem embutido (`libs/Mars4_5.jar`), usado para rodar o assembly gerado e para a suíte de testes do compilador.
- *(Opcional)* **Free Pascal Compiler** (`fpc`) — só é necessário se você quiser usar o target `run-pascal`, que compila e roda o `.pas` original com um compilador Pascal de verdade, útil para conferir manualmente se a saída esperada de um teste está correta.

## Compilando o projeto (build)

Rode todos os comandos a partir da raiz do repositório.

```bash
make
```

Esse é o target `all`, que faz duas coisas:

1. **`antlr`** — roda o ANTLR sobre `PascalLexer.g4` e `PascalParser.g4`, gerando as classes Java do lexer/parser dentro de `src/parser`.
2. **`javac`** — compila todos os `.java` do projeto (incluindo os gerados pelo ANTLR) para a pasta `java-bin/`.

Para limpar tudo o que foi gerado pelo build:

```bash
make clean
```

Isso remove `src/parser`, `java-bin` e `bin`.

## Executando o compilador manualmente

Todos os targets abaixo recebem o arquivo Pascal de entrada através da variável `FILE`.

```bash
# Roda só o lexer e imprime os tokens gerados
make run-lexer FILE=caminho/para/arquivo.pas

# Roda o parser e imprime a árvore sintática em texto
make run-parser FILE=caminho/para/arquivo.pas

# Mesmo que acima, mas abre uma janela gráfica com a árvore (útil para debug local)
make run-parser-gui FILE=caminho/para/arquivo.pas

# Roda o analisador semântico e imprime as tabelas de símbolos
make run-semantic FILE=caminho/para/arquivo.pas

# Constrói a AST e gera actual_result.dot / actual_result.png (via Graphviz)
# na mesma pasta do arquivo de entrada
make run-ast FILE=caminho/para/arquivo.pas

# Interpreta o programa diretamente a partir da AST
make run-interpreter FILE=caminho/para/arquivo.pas

# Gera o código MIPS correspondente em actual_code_result.asm
# na mesma pasta do arquivo de entrada
make run-compiler FILE=caminho/para/arquivo.pas

# (requer fpc instalado) compila e roda o Pascal original com o Free Pascal Compiler
make run-pascal FILE=caminho/para/arquivo.pas
```

> Nos targets `run-interpreter` e `run-compiler`, se existir um arquivo `input.txt` na mesma pasta do `.pas`, ele é usado automaticamente como entrada padrão (stdin) do programa.

Exemplo prático — gerar e rodar o MIPS de um dos programas de teste:

```bash
make run-compiler FILE=tests/compiler/valid/14-fibonacci/source.pas
java -jar libs/Mars4_5.jar nc ae1 tests/compiler/valid/14-fibonacci/actual_code_result.asm
```

## Rodando os testes automatizados

```bash
make test
```

Roda a suíte completa: lexer, parser, semântico, AST, interpretador e o gerador de código (MIPS + MARS), comparando a saída obtida ("actual") com o gabarito ("expected") de cada caso em `tests/`.

Para rodar só uma etapa específica, passe o argumento via `ARGS`:

```bash
make test ARGS="--lexer"
make test ARGS="--parser"
make test ARGS="--semantic"
make test ARGS="--ast"
make test ARGS="--interpreter"
make test ARGS="--compiler"
```

Também é possível rodar só a suíte do gerador de código (compila cada `.pas` para `.asm` e executa o resultado dentro do MARS, comparando a saída com o gabarito) diretamente:

```bash
make test-compiler
```

Para limpar os arquivos gerados pelos testes (`actual_result.*`, `diff.diff` etc.):

```bash
make test-clean
```

## Estrutura de um caso de teste

Cada caso de teste fica em uma subpasta `tests/<etapa>/(valid|invalid)/<nome-do-caso>/`, contendo:

- **`source.pas`** — o programa Pascal de entrada.
- **`expected_result.txt`** (ou `expected_result.dot` para os testes de AST) — o gabarito esperado.
- **`input.txt`** *(opcional)* — dados de entrada padrão (stdin), usado nos testes de interpretador/compilador quando o programa faz leitura de dados.

Ao rodar os testes, cada caso gera um `actual_result.*` (ou `actual_code_result.asm`, no caso do gerador de código) e um `diff.diff` comparando linha a linha com o gabarito:

- Se não houver diferenças → **`[PASS]`** (e os arquivos temporários são removidos).
- Se houver diferenças → **`[FAIL]`** (o `diff.diff` é mantido para inspeção).
- Se o `expected_result` não existir para o caso → é reportado como gabarito ausente.

## Integração contínua

O workflow `.github/workflows/tests.yml` roda `make test` automaticamente em *pull requests* para a branch `main`, usando JDK 25 e instalando o Graphviz previamente.

## Licença

Distribuído sob a licença **MIT** — veja o arquivo [`LICENSE`](./LICENSE) para mais detalhes.
