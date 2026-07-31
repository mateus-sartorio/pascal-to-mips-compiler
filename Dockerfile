# Build stage: generates the ANTLR sources, then packages the Spring Boot jar.
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /build

# Dependencies first: this layer is rebuilt only when the pom changes, not on every
# edit to the source.
COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY libs ./libs
COPY src ./src

# src/parser is not in the repository (it is gitignored), and the pom carries only the
# ANTLR runtime, not the plugin that would generate it. So the lexer and parser are
# generated here, exactly as the Makefile's "antlr" target does, before javac runs.
RUN java -jar libs/antlr-4.13.2-complete.jar \
      -Xexact-output-dir -no-listener -visitor -o src/parser src/PascalLexer.g4 \
 && java -jar libs/antlr-4.13.2-complete.jar \
      -Xexact-output-dir -no-listener -visitor -lib src/parser -o src/parser src/PascalParser.g4

RUN mvn -B -q package -DskipTests


# Runtime stage.
FROM eclipse-temurin:25-jre

# Graphviz draws the parse tree and the AST: GraphvizService shells out to `dot`, and
# without it those two panels fall back to raw DOT text.
RUN apt-get update \
 && apt-get install -y --no-install-recommends graphviz \
 && rm -rf /var/lib/apt/lists/*

# Programs submitted through the page run in a child JVM. Keep that off root, and give
# it a home it may write to: the runner drops each program in a temp file.
RUN useradd --create-home --shell /usr/sbin/nologin compiler
USER compiler
WORKDIR /home/compiler

# The fat jar, not the plain one: InterpreterService relaunches this same jar through
# PropertiesLauncher to run each program, which needs the BOOT-INF layout.
COPY --from=build --chown=compiler:compiler \
     /build/target/pascal-to-mips-compiler-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

# Exec form, with no shell in between: java is PID 1, so it receives SIGTERM directly,
# and startup does not depend on /usr/bin/sh surviving the host's AppArmor or seccomp
# policy. MaxRAMPercentage sizes the heap of the server itself; every program the page
# runs is a separate JVM, capped at 256 MB by InterpreterService, so this leaves room
# for one or two of those alongside. Override by giving the container its own command.
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=60", "-jar", "app.jar"]
