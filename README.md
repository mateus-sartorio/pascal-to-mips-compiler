# Pascal to MIPS Compiler

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
> `sdk install java 25-oracle`
>
> To select the installed version for the current session:
> `sdk use java 25-oracle`
>
> To set it as the default version:
> `sdk default java 25-oracle`

ANTLR is bundled in this repository at `libs/antlr-4.13.2-complete.jar` and is already configured in the root `Makefile`.

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

Executes the automated test suite.

### Clean test results

```bash
make test-clean
```

Removes generated test result files.
