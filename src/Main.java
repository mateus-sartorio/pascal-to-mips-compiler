import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import ast.AstBuilder;
import ast.types.ProgramNode;
import parser.PascalLexer;
import parser.PascalParser;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import checker.*;
import codegenerator.CodeGenerator;
import interpreter.Interpreter;

public class Main {
  public static void main(String[] args) throws IOException {
    // Criando um char stream que le todos os caracteres do arquivo
    CharStream input = CharStreams.fromFileName(args[0], StandardCharsets.UTF_8);

    // Criando o lexer que vai consumir esses caracteres
    PascalLexer lexer = new PascalLexer(input);

    // Criando um buffer com os tokens vindos do lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Criando o parser utilizando o buffer de tokens
    PascalParser parser = new PascalParser(tokens);

    // Iniciando o processo de parsing (criando a parser tree)
    ParseTree tree = parser.program();

    // Verificando se houve algum erro de sintaxe durante o parsing
    if (parser.getNumberOfSyntaxErrors() != 0) {
      return;
    }

    // Criando o analisaador semântico para percorrer a parser tree e fazer as verificações semânticas
    SemanticChecker semanticChecker = new SemanticChecker();

    // Percorrendo a parser tree para fazer as verificações semânticas
    semanticChecker.visit(tree);

    // Imprimindo as tabelas de símbolos caso o argumento -print-tables seja passado
    if (args.length > 1 && args[1].equals("-print-tables")) {
      semanticChecker.printLiteralsTable();
      semanticChecker.printBuiltInProceduresAndFunctionsTable();
      semanticChecker.printGlobalVariablesTable();
      semanticChecker.printProceduresAndFunctionsTable();
    }

    // Obtendo as informações necessárias do analisaador semântico para construir a AST
    String programIdentifier = semanticChecker.getProgramIdentifier();

    // Obtendo as tabelas de símbolos do analisaador semântico
    VariablesTable globalVariablesTable = semanticChecker.getGlobalVariablesTable();

    // Obtendo as tabelas de procedimentos e funções do analisaador semântico
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = semanticChecker.getBuiltInProceduresAndFunctionsTable();

    // Obtendo as tabelas de procedimentos e funções do analisaador semântico
    ProceduresAndFunctionsTable proceduresAndFunctionsTable = semanticChecker.getProceduresAndFunctionsTable();

    // Obtendo a tabela de literais de string do analisaador semântico
    StringLiteralsTable stringLiteralsTable = semanticChecker.getStringLiteralsTable();

    // Criando o construtor de AST para percorrer a parser tree e construir a AST
    AstBuilder astBuilder = new AstBuilder(programIdentifier, globalVariablesTable, builtInProceduresAndFunctionsTable, proceduresAndFunctionsTable);

    // Percorrendo a parser tree para construir a AST
    astBuilder.visit(tree);
    
    // Imprimindo a AST em notação DOT caso o argumento -print-ast seja passado
    if (args.length > 1 && args[1].equals("-print-ast")) {
      String dotNotation = astBuilder.toDotNotation();
      Path inputPath = Path.of(args[0]).toAbsolutePath();
      Path outputPath = inputPath.getParent().resolve("actual_result.dot");
      Files.writeString(outputPath, dotNotation, StandardCharsets.UTF_8);
    }

    // Executando o interpretador caso o argumento -interpret seja passado
    if (args.length > 1 && args[1].equals("-interpret")) {
      ProgramNode programNode = astBuilder.getProgramNode();
      
      Interpreter interpreter = new Interpreter(
        globalVariablesTable,
        stringLiteralsTable,
        builtInProceduresAndFunctionsTable,
        proceduresAndFunctionsTable,
        programNode
      );

      interpreter.execute();
    }

    if (args.length > 1 && args[1].equals("-compiler")) {
      ProgramNode programNode = astBuilder.getProgramNode();

      CodeGenerator codeGenerator = new CodeGenerator(
        programNode,
        globalVariablesTable,
        stringLiteralsTable,
        builtInProceduresAndFunctionsTable,
        proceduresAndFunctionsTable
      );

      String mipsTargetCode = codeGenerator.generate();
      Path outputPath = Path.of(args[0]).toAbsolutePath().getParent().resolve("actual_code_result.asm");
      Files.writeString(outputPath, mipsTargetCode, StandardCharsets.UTF_8);
    }
  }
}