package webapp.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.stereotype.Service;

import ast.AstBuilder;
import ast.types.ProgramNode;
import checker.SemanticChecker;
import codegenerator.CodeGenerator;
import parser.PascalLexer;
import parser.PascalParser;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.BuiltInProceduresAndFunctionsTable.BuiltInProceduresAndFunctionsEntry;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.ProcedureOrFunctionEnum;
import webapp.dto.CompilationIssue;
import webapp.dto.CompilerRequest;
import webapp.dto.CompilerResponse;
import webapp.dto.ExecutionDto;
import webapp.dto.RoutineDto;
import webapp.dto.StringLiteralDto;
import webapp.dto.SymbolDto;
import webapp.dto.SymbolTablesDto;
import webapp.dto.TokenDto;

@Service
public class CompilerService {
  private final Object semanticOutputLock = new Object();
  private final GraphvizService graphvizService;
  private final ParseTreeDotService parseTreeDotService;
  private final InterpreterService interpreterService;

  public CompilerService(
    GraphvizService graphvizService,
    ParseTreeDotService parseTreeDotService,
    InterpreterService interpreterService
  ) {
    this.graphvizService = graphvizService;
    this.parseTreeDotService = parseTreeDotService;
    this.interpreterService = interpreterService;
  }

  public CompilerResponse compile(CompilerRequest request) {
    String sourceCode = request.sourceCode() == null ? "" : request.sourceCode();
    List<CompilationIssue> issues = new ArrayList<>();

    CharStream input = CharStreams.fromString(sourceCode);
    PascalLexer lexer = new PascalLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new CollectingErrorListener("LEXER", issues));

    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tokens.fill();

    List<TokenDto> tokenDtos = toTokenDtos(tokens.getTokens());
    if (!issues.isEmpty()) {
      return new CompilerResponse(false, tokenDtos, null, null, null, null, null, null, null, issues);
    }

    PascalParser parser = new PascalParser(tokens);
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectingErrorListener("PARSER", issues));

    ParseTree tree = parser.program();
    String parseTree = tree.toStringTree(parser);
    if (!issues.isEmpty() || parser.getNumberOfSyntaxErrors() > 0) {
      return new CompilerResponse(false, tokenDtos, parseTree, null, null, null, null, null, null, issues);
    }

    // Parsing succeeded, so the tree is worth drawing even if a later stage fails.
    String parseTreeSvg = graphvizService.renderSvg(parseTreeDotService.toDotNotation(tree, parser));

    SemanticChecker semanticChecker = new SemanticChecker();
    ByteArrayOutputStream semanticOutput = new ByteArrayOutputStream();
    RuntimeException semanticFailure = null;

    synchronized (semanticOutputLock) {
      PrintStream originalOut = System.out;
      PrintStream captureStream = new PrintStream(semanticOutput, true, StandardCharsets.UTF_8);

      System.setOut(captureStream);
      try {
        semanticChecker.visit(tree);
      } catch (RuntimeException exception) {
        semanticFailure = exception;
      } finally {
        captureStream.flush();
        System.setOut(originalOut);
      }
    }

    if (semanticFailure != null) {
      String semanticMessage = lastNonEmptyLine(semanticOutput.toString(StandardCharsets.UTF_8));
      if (semanticMessage == null || semanticMessage.isBlank()) {
        semanticMessage = semanticFailure.getMessage() == null ? "Semantic error" : semanticFailure.getMessage();
      }

      issues.add(new CompilationIssue("SEMANTIC", 0, 0, semanticMessage));
      return new CompilerResponse(false, tokenDtos, parseTree, parseTreeSvg, null, null, null, null, null, issues);
    }

    String programIdentifier = semanticChecker.getProgramIdentifier();
    VariablesTable globalVariablesTable = semanticChecker.getGlobalVariablesTable();
    BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = semanticChecker.getBuiltInProceduresAndFunctionsTable();
    ProceduresAndFunctionsTable proceduresAndFunctionsTable = semanticChecker.getProceduresAndFunctionsTable();
    StringLiteralsTable stringLiteralsTable = semanticChecker.getStringLiteralsTable();

    AstBuilder astBuilder = new AstBuilder(
      programIdentifier,
      globalVariablesTable,
      builtInProceduresAndFunctionsTable,
      proceduresAndFunctionsTable
    );

    ProgramNode programNode = (ProgramNode) astBuilder.visit(tree);
    String astDot = astBuilder.toDotNotation();
    String astPng = graphvizService.renderPngAsBase64(astDot);

    CodeGenerator codeGenerator = new CodeGenerator(
      programNode,
      globalVariablesTable,
      stringLiteralsTable,
      builtInProceduresAndFunctionsTable,
      proceduresAndFunctionsTable
    );

    String mipsCode = codeGenerator.generate();

    ExecutionDto execution = interpreterService.run(sourceCode, request.standardInput());
    SymbolTablesDto symbolTables = buildSymbolTables(semanticChecker);

    return new CompilerResponse(true, tokenDtos, parseTree, parseTreeSvg, astDot, astPng, mipsCode, symbolTables, execution, issues);
  }

  private List<TokenDto> toTokenDtos(List<Token> tokens) {
    List<TokenDto> result = new ArrayList<>();
    int index = 0;

    for (Token token : tokens) {
      if (token.getType() == Token.EOF) {
        continue;
      }

      String symbolicName = PascalLexer.VOCABULARY.getSymbolicName(token.getType());
      result.add(new TokenDto(index++, symbolicName != null ? symbolicName : token.getText(), token.getText(), token.getLine(), token.getCharPositionInLine(), token.getStartIndex(), token.getStopIndex()));
    }

    return result;
  }

  /**
   * Converte as tabelas da análise semântica em DTOs. As implementações de
   * {@code toString()} das tabelas continuam intactas para a saída de linha de comando;
   * aqui os dados seguem estruturados para que o front-end escolha como apresentá-los.
   */
  private SymbolTablesDto buildSymbolTables(SemanticChecker semanticChecker) {
    StringLiteralsTable stringLiteralsTable = semanticChecker.getStringLiteralsTable();
    List<StringLiteralDto> stringLiterals = new ArrayList<>();

    for (Integer key : stringLiteralsTable.keySet()) {
      // O mesmo rótulo emitido na seção .data do MIPS, para ligar as duas visões.
      stringLiterals.add(new StringLiteralDto("__string" + key, stringLiteralsTable.get(key)));
    }

    List<RoutineDto> builtInRoutines = new ArrayList<>();
    for (BuiltInProceduresAndFunctionsEntry entry : semanticChecker.getBuiltInProceduresAndFunctionsTable().getAll()) {
      builtInRoutines.add(new RoutineDto(
        entry.identifier,
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? "function" : "procedure",
        // Procedimentos guardam NO_TYPE em vez de null; só funções têm tipo de retorno.
        entry.type == ProcedureOrFunctionEnum.FUNCTION && entry.returnType != null ? entry.returnType.toString() : null,
        null,
        toSymbolDtos(entry.parameters),
        List.of()
      ));
    }

    List<RoutineDto> routines = new ArrayList<>();
    for (ProceduresAndFunctionsEntry entry : semanticChecker.getProceduresAndFunctionsTable().getAll()) {
      routines.add(new RoutineDto(
        entry.identifier,
        entry.type == ProcedureOrFunctionEnum.FUNCTION ? "function" : "procedure",
        // Procedimentos guardam NO_TYPE em vez de null; só funções têm tipo de retorno.
        entry.type == ProcedureOrFunctionEnum.FUNCTION && entry.returnType != null ? entry.returnType.toString() : null,
        entry.line,
        toSymbolDtos(entry.parameters),
        toSymbolDtos(entry.localVariables)
      ));
    }

    return new SymbolTablesDto(
      toSymbolDtos(semanticChecker.getGlobalVariablesTable()),
      routines,
      builtInRoutines,
      stringLiterals
    );
  }

  private List<SymbolDto> toSymbolDtos(VariablesTable table) {
    List<SymbolDto> symbols = new ArrayList<>();

    for (VariableTableEntry entry : table.toList()) {
      symbols.add(new SymbolDto(entry.identifier, entry.type.toString(), entry.line));
    }

    return symbols;
  }

  private String lastNonEmptyLine(String content) {
    if (content == null || content.isBlank()) {
      return null;
    }

    String[] lines = content.split("\\R");
    for (int index = lines.length - 1; index >= 0; index--) {
      if (!lines[index].isBlank()) {
        return lines[index].trim();
      }
    }

    return null;
  }

  private static final class CollectingErrorListener extends BaseErrorListener {
    private final String stage;
    private final List<CompilationIssue> issues;

    private CollectingErrorListener(String stage, List<CompilationIssue> issues) {
      this.stage = stage;
      this.issues = issues;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException exception) {
      issues.add(new CompilationIssue(stage, line, charPositionInLine, msg));
    }
  }
}