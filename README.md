# Pascal to MIPS compiler

## Powered by hope

## How to run

### Prerequisites

- You must have a JDK installed.
- Preferably use JDK 25 or newer.
- ANTLR JAR is expected at `/usr/local/lib/antlr-4.13.2-complete.jar` (as configured in `src/Makefile`).

Tip: You can use jdkman (SDKMAN) to install and switch between JDK versions easily.

### Build

From the project source folder:

```bash
cd src
make
```

### Run lexer test

```bash
cd src
make run FILE=path/to/your_test_file.pas
```

This runs the lexer and prints recognized tokens.
