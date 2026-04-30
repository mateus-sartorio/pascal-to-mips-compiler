# Pascal to MIPS Compiler

### Powered by hope and coffee ☕

## Prerequisites

- JDK installed (Java compiler and runtime).

> [!TIP]
> When setting up Java for this project, it is recommended to use JDK 25 (or newer) with SDKMAN.
>
> To install SDKMAN:
> `curl -s "https://get.sdkman.io" | bash`
>
> Then open a new terminal and check the active Java version:
> `java --version`
>
> To list available Java versions in SDKMAN:
> `sdk list java`
>
> To install a version (example):
> `sdk install java 25.0.2-tem`
>
> To select the installed version for the current session:
> `sdk use java 25.0.2-tem`
>
> To set it as the default version:
> `sdk default java 25.0.2-tem`

ANTLR is bundled in this repository at `libs/antlr-4.13.2-complete.jar` and is already configured in the root `Makefile`.

## Recommended Extensions

If you are using **Visual Studio Code**, it is highly recommended to install the following extension to assist with lexer and parser development:

* **[ANTLR4 support](https://marketplace.visualstudio.com/items?itemName=mike-lischke.vscode-antlr4)**: Provides syntax highlighting, code completion, and grammar diagrams for `.g` files.

## Commands

Run all commands from the repository root.

### Build

```bash
make
```

Generates lexer files and compiles Java sources.

### Clean generated files

```bash
make clean
```

Removes generated lexer files.

### Run tests

```bash
make test
```

Tests can be executed independently. You can do this by passing an argument as shown below:

```bash
make test ARGS="--lexer" 
```

or

```bash
make test ARGS="--parser" 
```

Executes the automated test suite.

### Clean test results

```bash
make test-clean
```

Removes generated test result files.
