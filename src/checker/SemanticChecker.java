package checker;

import java.util.List;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser.Actual_parameterContext;
import parser.PascalParser.Actual_parameter_listContext;
import parser.PascalParser.Adding_operatorContext;
import parser.PascalParser.Array_typeContext;
import parser.PascalParser.Assignment_statementContext;
import parser.PascalParser.BooleanConstantContext;
import parser.PascalParser.Boolean_constantContext;
import parser.PascalParser.ExpressionContext;
import parser.PascalParser.FactorContext;
import parser.PascalParser.For_statementContext;
import parser.PascalParser.FunctionCallContext;
import parser.PascalParser.Function_declarationContext;
import parser.PascalParser.Function_designatorContext;
import parser.PascalParser.Function_headingContext;
import parser.PascalParser.Identifier_listContext;
import parser.PascalParser.If_statementContext;
import parser.PascalParser.Indexed_variableContext;
import parser.PascalParser.Multiplying_operatorContext;
import parser.PascalParser.NotFactorContext;
import parser.PascalParser.NumericConstantContext;
import parser.PascalParser.Numeric_constantContext;
import parser.PascalParser.ParenthesisExpressionContext;
import parser.PascalParser.Primitive_typeContext;
import parser.PascalParser.Procedure_declarationContext;
import parser.PascalParser.Procedure_headingContext;
import parser.PascalParser.Procedure_statementContext;
import parser.PascalParser.ProgramContext;
import parser.PascalParser.Program_headingContext;
import parser.PascalParser.Simple_expressionContext;
import parser.PascalParser.StringConstantContext;
import parser.PascalParser.Subrange_typeContext;
import parser.PascalParser.TermContext;
import parser.PascalParser.Type_denoterContext;
import parser.PascalParser.Value_parameter_speficiationContext;
import parser.PascalParser.VariableAccessContext;
import parser.PascalParser.Variable_accessContext;
import parser.PascalParser.Variable_declarationContext;
import parser.PascalParserBaseVisitor;
import tables.StringLiteralsTable;
import tables.VariablesTable;
import tables.BuiltInProceduresAndFunctionsTable.BuiltInProceduresAndFunctionsEntry;
import tables.VariablesTable.VariableTableEntry;
import tables.BuiltInProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import types.ArrayVariableType;
import types.ConstantPrimitiveVariableType;
import types.PrimitiveTypeEnum;
import types.PrimitiveVariableType;
import types.ProcedureOrFunctionEnum;
import types.TypeRules;
import types.VariableType;

/**
 * Classe responsável por realizar a verificação semântica do código Pascal.
 * Ela percorre a árvore de análise sintática gerada pelo ANTLR e verifica se o código está semanticamente correto.
 * Durante a verificação, são construídas tabelas de símbolos para variáveis globais, procedimentos e funções, bem como uma tabela de literais de string.
 */
public class SemanticChecker extends PascalParserBaseVisitor<VariableType> {
  /**
   * Identificador do programa principal.
   */
  String programIdentifier;

  /**
   * Tabela de literais de string, que armazena todas as strings literais encontradas no código.
   */
  private final StringLiteralsTable stringLiteralsTable = new StringLiteralsTable();

  /**
   * Tabela de variáveis globais, que armazena todas as variáveis declaradas no escopo global do programa.
   */
  private final VariablesTable globalVariablesTable = new VariablesTable();

  /**
   * Tabela de procedimentos e funções, que armazena todas as declarações de procedimentos e funções, juntamente com suas variáveis locais e parâmetros.
   */
  private final BuiltInProceduresAndFunctionsTable builtInProceduresAndFunctionsTable = new BuiltInProceduresAndFunctionsTable();

  /**
   * Tabela de procedimentos e funções definidos pelo usuário, que armazena todas as declarações de procedimentos e funções, juntamente com suas variáveis locais e parâmetros.
   */
  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable = new ProceduresAndFunctionsTable();

  /**
   * Retorna o identificador do programa principal.
   *
   * @return O identificador do programa principal.
   */
  public String getProgramIdentifier() {
    return programIdentifier;
  }

  /**
   * Retorna a tabela de literais de string.
   *
   * @return A tabela de literais de string.
   */
  public StringLiteralsTable getStringLiteralsTable() {
    return stringLiteralsTable;
  }

  /**
   * Retorna a tabela de variáveis globais.
   *
   * @return A tabela de variáveis globais.
   */
  public VariablesTable getGlobalVariablesTable() {
    return globalVariablesTable;
  }

  /**
   * Retorna a tabela de procedimentos e funções pré-definidos.
   *
   * @return A tabela de procedimentos e funções pré-definidos.
   */
  public BuiltInProceduresAndFunctionsTable getBuiltInProceduresAndFunctionsTable() {
    return builtInProceduresAndFunctionsTable;
  }

  /**
   * Retorna a tabela de procedimentos e funções definidos pelo usuário.
   *
   * @return A tabela de procedimentos e funções definidos pelo usuário.
   */
  public ProceduresAndFunctionsTable getProceduresAndFunctionsTable() {
    return proceduresAndFunctionsTable;
  }

  /**
   * Construtor da classe SemanticChecker.
   * Inicializa a tabela de procedimentos e funções pré-definidos.
   */
  public SemanticChecker() {
    registerPreDeclaredProceduresAndFunctions();
  }

  /**
   * Imprime a tabela de literais de string, caso não esteja vazia.
   */
  public void printLiteralsTable() {
    if (stringLiteralsTable.isEmpty()) {
      return;
    }

    System.out.println("STRING LITERALS TABLE:\n");
    System.out.println(stringLiteralsTable);
  }

  /**
   * Imprime a tabela de variáveis globais, caso não esteja vazia.
   */
  public void printGlobalVariablesTable() {
    if (globalVariablesTable.isEmpty()) {
      return;
    }

    System.out.println("\nGLOBAL VARIABLES TABLE:\n");
    System.out.println(globalVariablesTable);
  }

  /**
   * Imprime a tabela de procedimentos e funções definidos pelo usuário, caso não esteja vazia.
   */
  public void printProceduresAndFunctionsTable() {
    if (proceduresAndFunctionsTable.isEmpty()) {
      return;
    }

    System.out.println("\nPROCEDURES AND FUNCTIONS TABLE:\n");
    System.out.println(proceduresAndFunctionsTable);
  }

  /**
   * Imprime a tabela de procedimentos e funções pré-definidos, caso não esteja vazia.
   */
  public void printBuiltInProceduresAndFunctionsTable() {
    if (builtInProceduresAndFunctionsTable.isEmpty()) {
      return;
    }

    System.out.println("\nBUILT-IN PROCEDURES AND FUNCTIONS TABLE:\n");
    System.out.println(builtInProceduresAndFunctionsTable);
  }

  /**
   * Registra procedimentos e funções pré-definidos na tabela de procedimentos e funções.
   * Esses procedimentos e funções são fornecidos pelo compilador e podem ser utilizados no código Pascal.
   */
  private void registerPreDeclaredProceduresAndFunctions() {
    // Reusable primitive types
    VariableType typeString = new PrimitiveVariableType(PrimitiveTypeEnum.STRING);
    VariableType typeInteger = new PrimitiveVariableType(PrimitiveTypeEnum.INTEGER);
    VariableType typeReal = new PrimitiveVariableType(PrimitiveTypeEnum.REAL);
    VariableType typeChar = new PrimitiveVariableType(PrimitiveTypeEnum.CHAR);
    VariableType typeBoolean = new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN);
    VariableType typeNoType = new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);

    // Standard parameter entries
    VariableTableEntry stringParam = new VariableTableEntry("str", typeString);
    VariableTableEntry integerParam = new VariableTableEntry("n", typeInteger);
    VariableTableEntry realParam = new VariableTableEntry("n", typeReal);
    VariableTableEntry charParam = new VariableTableEntry("c", typeChar);
    VariableTableEntry booleanParam = new VariableTableEntry("b", typeBoolean);
    VariableTableEntry noTypeParam = new VariableTableEntry("dummy", typeNoType);

    // --- PROCEDURES ---
    // I/O
    builtInProceduresAndFunctionsTable.addProcedure("write", List.of(stringParam));
    builtInProceduresAndFunctionsTable.addProcedure("writeln", List.of(stringParam));

    List<VariableTableEntry> dummyParamsForRead = new java.util.ArrayList<>();
    for (int i = 0; i < 100; i++) {
      dummyParamsForRead.add(noTypeParam);
    }
    builtInProceduresAndFunctionsTable.addProcedure("read", dummyParamsForRead);
    builtInProceduresAndFunctionsTable.addProcedure("readln", dummyParamsForRead);

    // Math
    builtInProceduresAndFunctionsTable.addFunction("abs", List.of(realParam), PrimitiveTypeEnum.REAL);
    builtInProceduresAndFunctionsTable.addFunction("sqr", List.of(realParam), PrimitiveTypeEnum.REAL);
    builtInProceduresAndFunctionsTable.addFunction("sqrt", List.of(realParam), PrimitiveTypeEnum.REAL);
    builtInProceduresAndFunctionsTable.addFunction("trunc", List.of(realParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("round", List.of(realParam), PrimitiveTypeEnum.INTEGER);

    // Ordinal & Character
    builtInProceduresAndFunctionsTable.addFunction("ord", List.of(charParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("chr", List.of(integerParam), PrimitiveTypeEnum.CHAR);
    builtInProceduresAndFunctionsTable.addFunction("succ", List.of(integerParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("pred", List.of(integerParam), PrimitiveTypeEnum.INTEGER);

    // String
    builtInProceduresAndFunctionsTable.addFunction("length", List.of(stringParam), PrimitiveTypeEnum.INTEGER);
    builtInProceduresAndFunctionsTable.addFunction("upcase", List.of(charParam), PrimitiveTypeEnum.CHAR);
    builtInProceduresAndFunctionsTable.addFunction("itos", List.of(integerParam), PrimitiveTypeEnum.STRING);
    builtInProceduresAndFunctionsTable.addFunction("rtos", List.of(realParam), PrimitiveTypeEnum.STRING);
    builtInProceduresAndFunctionsTable.addFunction("btos", List.of(booleanParam), PrimitiveTypeEnum.STRING);
  }

  /**
   * Retorna o contexto de acesso a variável correspondente à árvore de análise sintática fornecida.
   *
   * @param tree A árvore de análise sintática a ser verificada.
   * @return O contexto de acesso a variável correspondente, ou null se não for encontrado.
   */
  private Variable_accessContext getVariableAccess(ParseTree tree) {
    if (tree == null) {
      return null;
    }
    if (tree instanceof Variable_accessContext) {
      return (Variable_accessContext) tree;
    }
    if (tree instanceof VariableAccessContext) {
      return ((VariableAccessContext) tree).variable_access();
    }
    if (tree.getChildCount() == 1 && tree.getChild(0) instanceof ParseTree) {
      return getVariableAccess(tree.getChild(0));
    }
    return null;
  }

  /**
   * Verifica se um identificador global já foi definido.
   * Se o identificador for igual ao identificador do programa principal, ou se for um procedimento ou função pré-definido, ou se já estiver presente na tabela de variáveis globais ou na tabela de procedimentos e funções, um erro semântico é gerado e o programa é encerrado.
   *
   * @param identifierToken O token do identificador a ser verificado.
   */
  private void checkGlobalIdentifierIsNotDefined(Token identifierToken) {
    String identifier = identifierToken.getText();

    if (identifier.equalsIgnoreCase(programIdentifier)) {
      System.out.printf("SEMANTIC ERROR (%d): Program heading '%s' cannot be used.\n", identifierToken.getLine(), programIdentifier);

      throw new SemanticErrorException();
    }

    BuiltInProceduresAndFunctionsEntry builtInProceduresAndFunctionsEntry = builtInProceduresAndFunctionsTable.get(identifier);

    if (builtInProceduresAndFunctionsEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): '%s' is a built-in %s.\n", identifierToken.getLine(), builtInProceduresAndFunctionsEntry.identifier, builtInProceduresAndFunctionsEntry.type.toString());

      throw new SemanticErrorException();
    }

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier);

    if (globalVariableEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Global variable '%s' was already declared at line %d.\n", identifierToken.getLine(), globalVariableEntry.identifier, globalVariableEntry.line);

      throw new SemanticErrorException();
    }

    ProceduresAndFunctionsEntry proceduresAndFunctionsEntry = proceduresAndFunctionsTable.get(identifier);

    if (proceduresAndFunctionsEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): %s '%s' was already declared at line %d.\n", identifierToken.getLine(), proceduresAndFunctionsEntry.type.toString(), proceduresAndFunctionsEntry.identifier, proceduresAndFunctionsEntry.line);

      throw new SemanticErrorException();
    }
  }

  /**
   * Verifica se um identificador de parâmetro ou variável local de um procedimento ou função já foi definido.
   * Se o identificador já estiver presente na tabela de parâmetros ou na tabela de variáveis locais do procedimento ou função, um erro semântico é gerado e o programa é encerrado.
   *
   * @param identifierToken O token do identificador a ser verificado.
   * @param procedureOrFunctionIdentifier O identificador do procedimento ou função ao qual o parâmetro ou variável local pertence.
   */
  private void checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(Token identifierToken, String procedureOrFunctionIdentifier) {
    String identifier = identifierToken.getText();

    ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable.get(procedureOrFunctionIdentifier);

    if (procedureOrFunctionEntry == null) {
      return;
    }

    VariableTableEntry variableEntry = procedureOrFunctionEntry.localVariables.get(identifier);
    VariableTableEntry parameterEntry = procedureOrFunctionEntry.parameters.get(identifier);

    if (parameterEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Parameter '%s' of %s '%s' was already declared at line %d.\n", identifierToken.getLine(), identifier, procedureOrFunctionEntry.type.toString(), procedureOrFunctionIdentifier, parameterEntry.line);

      throw new SemanticErrorException();
    }

    if (variableEntry != null) {
      System.out.printf("SEMANTIC ERROR (%d): Local variable '%s' of %s '%s' was already declared at line %d.\n", identifierToken.getLine(), identifier, procedureOrFunctionEntry.type.toString(), procedureOrFunctionIdentifier, variableEntry.line);

      throw new SemanticErrorException();
    }
  }

  /**
   * Gera um erro semântico para operações unárias com tipos incompatíveis.
   * Imprime uma mensagem de erro indicando a linha, o operador e o tipo incompatível, e encerra o programa.
   *
   * @param line O número da linha onde ocorreu o erro.
   * @param operation O operador unário que causou o erro.
   * @param type O tipo incompatível encontrado.
   */
  private void unaryOperationTypeError(int line, String operation, VariableType type) {
    System.out.printf("SEMANTIC ERROR (%d): incompatible type for operator '%s', type is '%s'.\n", line, operation, type.toString());

    throw new SemanticErrorException();
  }

  /**
   * Gera um erro semântico para operações binárias com tipos incompatíveis.
   * Imprime uma mensagem de erro indicando a linha, o operador e os tipos incompatíveis, e encerra o programa.
   *
   * @param line O número da linha onde ocorreu o erro.
   * @param operation O operador binário que causou o erro.
   * @param leftType O tipo do operando à esquerda.
   * @param rightType O tipo do operando à direita.
   */
  private void binaryOperationTypeError(int line, String operation, VariableType leftType, VariableType rightType) {
    System.out.printf("SEMANTIC ERROR (%d): incompatible types for operator '%s', LHS is '%s' and RHS is '%s'.\n", line, operation, leftType.toString(), rightType.toString());

    throw new SemanticErrorException();
  }

  /**
   * Registra variáveis globais na tabela de variáveis globais.
   * Para cada identificador na lista de identificadores, verifica se o identificador já foi definido globalmente e, em seguida, adiciona a variável à tabela de variáveis globais com o tipo especificado.
   *
   * @param context O contexto da lista de identificadores contendo os identificadores das variáveis a serem registradas.
   * @param variableType O tipo das variáveis a serem registradas.
   */
  private void registerGlobalVariables(Identifier_listContext context, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token identifierToken = identifierNode.getSymbol();
      String variableIdentifier = identifierToken.getText();

      checkGlobalIdentifierIsNotDefined(identifierToken);

      int varLine = identifierToken.getLine();
      globalVariablesTable.addVariable(variableIdentifier, varLine, variableType);
    }
  }

  /**
   * Registra variáveis locais de um procedimento ou função na tabela de procedimentos e funções.
   * Para cada identificador na lista de identificadores, verifica se o identificador já foi definido globalmente ou como parâmetro/variável local do procedimento/função, e em seguida adiciona a variável à tabela de variáveis locais do procedimento/função com o tipo especificado.
   *
   * @param context O contexto da lista de identificadores contendo os identificadores das variáveis a serem registradas.
   * @param procedureOrFunctionIdentifier O identificador do procedimento ou função ao qual as variáveis pertencem.
   * @param variableType O tipo das variáveis a serem registradas.
   */
  private void registerProcedureOrFunctionLocalVariables(Identifier_listContext context, String procedureOrFunctionIdentifier, VariableType variableType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();

      checkGlobalIdentifierIsNotDefined(token);
      checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(token, procedureOrFunctionIdentifier);

      String variableIdentifier = token.getText();
      int variableLine = token.getLine();

      proceduresAndFunctionsTable.addProcedlureOrFunctionVariable(procedureOrFunctionIdentifier, variableIdentifier, variableLine, variableType);
    }
  }

  /**
   * Registra parâmetros de um procedimento ou função na tabela de procedimentos e funções.
   * Para cada identificador na lista de identificadores, verifica se o identificador já foi definido globalmente ou como parâmetro/variável local do procedimento/função, e em seguida adiciona o parâmetro à tabela de parâmetros do procedimento/função com o tipo especificado.
   *
   * @param context O contexto da lista de identificadores contendo os identificadores dos parâmetros a serem registrados.
   * @param procedureOrFunctionIdentifier O identificador do procedimento ou função ao qual os parâmetros pertencem.
   * @param variableType O tipo dos parâmetros a serem registrados.
   * @param procedureOrFunctionType O tipo do procedimento ou função (PROCEDURE ou FUNCTION).
   */
  private void registerProcedureOrFunctionParameters(Identifier_listContext context, String procedureOrFunctionIdentifier, VariableType variableType, ProcedureOrFunctionEnum procedureOrFunctionType) {
    for (TerminalNode identifierNode : context.IDENTIFIER()) {
      Token token = identifierNode.getSymbol();

      checkGlobalIdentifierIsNotDefined(token);
      checkProcedureOrFunctionParameterOrLocalVariableIdentifierIsNotDefined(token, procedureOrFunctionIdentifier);

      String variableIdentifier = token.getText();
      int variableLine = token.getLine();

      proceduresAndFunctionsTable.addProcedlureOrFunctionParameter(procedureOrFunctionIdentifier, variableIdentifier, variableLine, variableType);
    }
  }

  /**
   * Extrai o tipo de variável a partir do contexto de tipo denotador fornecido.
   * Se o tipo denotador for um tipo primitivo, cria um objeto PrimitiveVariableType correspondente.
   * Se o tipo denotador for um tipo de array, cria um objeto ArrayVariableType com os limites especificados.
   *
   * @param context O contexto de tipo denotador a ser analisado.
   * @return O tipo de variável correspondente ao tipo denotador.
   */
  private VariableType extractVariableTypeFromTypeDenoter(Type_denoterContext context) {
    VariableType type;

    if (context.primitive_type() != null) {
      Primitive_typeContext primitiveType = context.primitive_type();
      type = new PrimitiveVariableType(PrimitiveTypeEnum.getType(primitiveType.getText()));
    }
    else {
      Array_typeContext arrayType = context.array_type();
      PrimitiveTypeEnum primitiveType = PrimitiveTypeEnum.getType(arrayType.primitive_type().getText());
      Subrange_typeContext subrangeType = arrayType.subrange_type();
      int lowerBound = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(0).getText());
      int upperBound = Integer.parseInt(subrangeType.UNSIGNED_INTEGER(1).getText());

      if (upperBound <= lowerBound) {
        System.out.printf("SEMANTIC ERROR (%d): End index (%d) must be bigger than start index (%d) in declaration of array variables.\n", arrayType.ARRAY().getSymbol().getLine(), upperBound, lowerBound);

        throw new SemanticErrorException();
      }

      type = new ArrayVariableType(primitiveType, lowerBound, upperBound);
    }

    return type;
  }

  /**
   * Visita o contexto do cabeçalho do programa e registra o identificador do programa principal.
   *
   * @param context O contexto do cabeçalho do programa.
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado ao cabeçalho do programa.
   */
  @Override
  public VariableType visitProgram_heading(Program_headingContext context) {
    programIdentifier = context.IDENTIFIER().getText();
    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  /**
   * Visita o contexto de declaração de variável e registra as variáveis globais ou locais, dependendo do escopo.
   *
   * @param context O contexto da declaração de variável.
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado à declaração de variável.
   */
  @Override
  public VariableType visitVariable_declaration(Variable_declarationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    RuleContext parent = context.parent.parent;

    if (parent instanceof Procedure_headingContext) {
      Procedure_headingContext procedureHeadingContext = (Procedure_headingContext) parent;
      String identifier = procedureHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
      registerProcedureOrFunctionLocalVariables(context.identifier_list(), identifier, type);
    }
    else
      if (parent instanceof Function_headingContext) {
        Function_headingContext functionHeadingContext = (Function_headingContext) parent;
        String identifier = functionHeadingContext.IDENTIFIER().getText();
        assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);
        registerProcedureOrFunctionLocalVariables(context.identifier_list(), identifier, type);

      }
      else {
        registerGlobalVariables(context.identifier_list(), type);
      }

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  /**
   * Visita o contexto do cabeçalho de procedimento e registra o procedimento na tabela de procedimentos e funções.
   *
   * @param context O contexto do cabeçalho de procedimento.
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado ao cabeçalho de procedimento.
   */
  @Override
  public VariableType visitProcedure_heading(Procedure_headingContext context) {
    Token identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    String procedureIdentifier = identifierToken.getText();
    int line = identifierToken.getLine();

    proceduresAndFunctionsTable.addProcedure(procedureIdentifier, line);

    return visitChildren(context);
  }

  /**
   * Visita o contexto do cabeçalho de função e registra a função na tabela de procedimentos e funções.
   * Verifica se o tipo de retorno da função é um tipo primitivo, caso contrário, gera um erro semântico.
   *
   * @param context O contexto do cabeçalho de função.
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado ao cabeçalho de função.
   */
  @Override
  public VariableType visitFunction_heading(Function_headingContext context) {
    Token identifierToken = context.IDENTIFIER().getSymbol();

    checkGlobalIdentifierIsNotDefined(identifierToken);

    String functionIdentifier = identifierToken.getText();
    int line = identifierToken.getLine();
    VariableType returnType = extractVariableTypeFromTypeDenoter(context.type_denoter());

    if (!(returnType instanceof PrimitiveVariableType)) {
      System.out.printf("SEMANTIC ERROR (%d): Function '%s' return type should be a primitive type.\n", line, functionIdentifier);

      throw new SemanticErrorException();
    }

    proceduresAndFunctionsTable.addFunction(functionIdentifier, line, returnType.basePrimitiveType);

    return visitChildren(context);
  }

  /**
   * Visita o contexto de especificação de parâmetro de valor e registra os parâmetros na tabela de procedimentos e funções.
   *
   * @param context O contexto da especificação de parâmetro de valor.
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado à especificação de parâmetro de valor.
   */
  @Override
  public VariableType visitValue_parameter_speficiation(Value_parameter_speficiationContext context) {
    Type_denoterContext typeDenoter = context.type_denoter();
    VariableType type = extractVariableTypeFromTypeDenoter(typeDenoter);

    RuleContext declaration = context.parent.parent;

    if (declaration instanceof Function_headingContext functionHeadingContext) {
      String identifier = functionHeadingContext.IDENTIFIER().getText();
      assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);

      registerProcedureOrFunctionParameters(context.identifier_list(), identifier, type, ProcedureOrFunctionEnum.FUNCTION);
    }
    else {
      if (declaration instanceof Procedure_headingContext procedureHeadingContext) {
        String identifier = procedureHeadingContext.IDENTIFIER().getText();
        assert proceduresAndFunctionsTable.lookProcedureOrFunction(identifier);

        registerProcedureOrFunctionParameters(context.identifier_list(), identifier, type, ProcedureOrFunctionEnum.PROCEDURE);
      }
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  /**
   * Visita o contexto de acesso a variável e retorna o tipo da variável acessada.
   * Verifica se a variável é global, local ou parâmetro de um procedimento/função, e se é indexável caso seja um array ou string.
   * Gera erros semânticos caso a variável não seja encontrada ou não seja indexável quando necessário.
   *
   * @param context O contexto de acesso a variável.
   * @return O tipo da variável acessada.
   */
  @Override
  public VariableType visitVariable_access(Variable_accessContext context) {
    TerminalNode identifier;
    boolean isIndexedVariable = false;

    if (context.IDENTIFIER() != null) {
      identifier = context.IDENTIFIER();
    }
    else {
      Indexed_variableContext indexedVariable = context.indexed_variable();

      var expression = indexedVariable.expression();
      var expressionReturnType = visit(expression);

      if (!(expressionReturnType instanceof PrimitiveVariableType) || !(expressionReturnType.basePrimitiveType == PrimitiveTypeEnum.INTEGER)) {
        System.out.printf("SEMANTIC ERROR (%d): indexing expression must be an integer.\n", expression.start.getLine());

        throw new SemanticErrorException();
      }

      identifier = indexedVariable.IDENTIFIER();
      isIndexedVariable = true;
    }

    String variableIdentifier = identifier.getSymbol().getText();

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier.getSymbol().getText());
    if (globalVariableEntry != null) {
      if (!isIndexedVariable) {
        return globalVariableEntry.type;
      }

      if (!(globalVariableEntry.type instanceof ArrayVariableType || (globalVariableEntry.type instanceof PrimitiveVariableType && globalVariableEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
        System.out.printf("SEMANTIC ERROR (%d): expression must be indexable.\n", context.start.getLine());

        throw new SemanticErrorException();
      }

      if (globalVariableEntry.type instanceof PrimitiveVariableType && globalVariableEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
        return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
      }

      return new PrimitiveVariableType(((ArrayVariableType) globalVariableEntry.type).basePrimitiveType);
    }

    RuleContext parent = context.parent;

    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext functionDeclarationContext) {
        Function_headingContext functionHeading = functionDeclarationContext.function_heading();
        String functionIdentifier = functionHeading.IDENTIFIER().getText();

        ProceduresAndFunctionsEntry procedureOrFunctionEntry = proceduresAndFunctionsTable.get(functionIdentifier);

        if (variableIdentifier.equalsIgnoreCase(functionIdentifier)) {
          return new PrimitiveVariableType(procedureOrFunctionEntry.returnType);
        }

        VariableTableEntry parameterEntry = procedureOrFunctionEntry.parameters.get(variableIdentifier);

        if (parameterEntry != null) {
          if (!isIndexedVariable) {
            return parameterEntry.type;
          }

          if (!(parameterEntry.type instanceof ArrayVariableType || (parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
            System.out.printf("SEMANTIC ERROR (%d): expression must be indexable.\n", context.start.getLine());

            throw new SemanticErrorException();
          }

          if (parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
          }

          return new PrimitiveVariableType(((ArrayVariableType) parameterEntry.type).basePrimitiveType);
        }

        VariableTableEntry localEntry = procedureOrFunctionEntry.localVariables.get(variableIdentifier);

        if (localEntry != null) {
          if (!isIndexedVariable) {
            return localEntry.type;
          }

          if (!(localEntry.type instanceof ArrayVariableType || (localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
            System.out.printf("SEMANTIC ERROR (%d): expression must be indexable.\n", context.start.getLine());

            throw new SemanticErrorException();
          }

          if (localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
            return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
          }

          return new PrimitiveVariableType(((ArrayVariableType) localEntry.type).basePrimitiveType);
        }

        break;
      }
      else
        if (parent instanceof Procedure_declarationContext procedureDeclarationContext) {
          Procedure_headingContext procedureHeading = procedureDeclarationContext.procedure_heading();
          String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

          ProceduresAndFunctionsEntry paramterEntry = proceduresAndFunctionsTable.get(procedureIdentifier);

          VariableTableEntry parameterEntry = paramterEntry.parameters.get(variableIdentifier);

          if (parameterEntry != null) {
            if (!isIndexedVariable) {
              return parameterEntry.type;
            }

            if (!(parameterEntry.type instanceof ArrayVariableType || (parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
              System.out.printf("SEMANTIC ERROR (%d): expression must be indexable.\n", context.start.getLine());

              throw new SemanticErrorException();
            }

            if (parameterEntry.type instanceof PrimitiveVariableType && parameterEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
              return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
            }

            return new PrimitiveVariableType(((ArrayVariableType) parameterEntry.type).basePrimitiveType);
          }

          VariableTableEntry localEntry = paramterEntry.localVariables.get(variableIdentifier);

          if (localEntry != null) {
            if (!isIndexedVariable) {
              return localEntry.type;
            }

            if (!(localEntry.type instanceof ArrayVariableType || (localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING))) {
              System.out.printf("SEMANTIC ERROR (%d): expression must be indexable.\n", context.start.getLine());

              throw new SemanticErrorException();
            }

            if (localEntry.type instanceof PrimitiveVariableType && localEntry.type.basePrimitiveType == PrimitiveTypeEnum.STRING) {
              return new PrimitiveVariableType(PrimitiveTypeEnum.CHAR, isIndexedVariable);
            }

            return new PrimitiveVariableType(((ArrayVariableType) localEntry.type).basePrimitiveType);
          }

          break;
        }

      parent = parent.parent;
    }

    System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.\n", identifier.getSymbol().getLine(), variableIdentifier);

    throw new SemanticErrorException();
  }

  /**
   * Visita o contexto de variável indexada e retorna o tipo da variável acessada.
   * Verifica se a expressão de índice é do tipo inteiro e se a variável é global.
   * Gera erros semânticos caso a expressão de índice não seja do tipo inteiro ou se a variável não for encontrada.
   *
   * @param context O contexto de variável indexada.
   * @return O tipo da variável acessada.
   */
  @Override
  public VariableType visitIndexed_variable(Indexed_variableContext context) {
    ExpressionContext expression = context.expression();
    VariableType expressionType = visit(expression);

    if (!(expressionType instanceof PrimitiveVariableType) || expressionType.basePrimitiveType != PrimitiveTypeEnum.INTEGER) {
      TerminalNode bracket = context.OPEN_BRACKET();
      unaryOperationTypeError(bracket.getSymbol().getLine(), "[]", expressionType);
    }

    TerminalNode identifier = context.IDENTIFIER();

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier.getSymbol().getText());

    if (globalVariableEntry != null) {
      return new PrimitiveVariableType(globalVariableEntry.type.basePrimitiveType);
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }

  /**
   * Verifica se uma variável é do tipo ordinal.
   * Se a variável não for do tipo ordinal, um erro semântico é gerado e o programa é encerrado.
   *
   * @param identifierToken O token do identificador da variável a ser verificada.
   * @param variableType O tipo da variável a ser verificada.
   */
  private static void checkVariableIsOrdinal(Token identifierToken, VariableType variableType) {
    if (!variableType.isOrdinal()) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' should be an ordinal type.\n", identifierToken.getLine(), identifierToken.getText());

      throw new SemanticErrorException();
    }
  }

  /**
   * Verifica se dois tipos primitivos são iguais.
   * Se os tipos primitivos forem diferentes, um erro semântico é gerado e o programa é encerrado.
   * @param line O número da linha onde ocorreu a verificação.
   * @param leftType O tipo primitivo do lado esquerdo da comparação.
   * @param rightType O tipo primitivo do lado direito da comparação.
   */
  private static void checkPrimitiveTypesAreEqual(int line, PrimitiveVariableType leftType, PrimitiveVariableType rightType) {
    if (leftType.basePrimitiveType != rightType.basePrimitiveType) {
      System.out.printf("SEMANTIC ERROR (%d): control variable type is '%s' and loop bounds types are '%s'.\n", line, leftType.toString(), rightType.toString());

      throw new SemanticErrorException();
    }
  }

  /**
   * Visita o contexto da declaração de um laço "for" e realiza verificações semânticas relacionadas ao tipo da variável de controle e aos limites do laço.
   * Verifica se a variável de controle é do tipo ordinal, se os tipos dos limites são compatíveis e se os valores dos limites são coerentes com a direção do laço (up to ou down to).
   *
   * @param context O contexto da declaração do laço "for".
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado à declaração do laço "for".
   */
  @Override
  public VariableType visitFor_statement(For_statementContext context) {
    visit(context.statement());

    TerminalNode identifier = context.IDENTIFIER();

    String variableIdentifier = identifier.getSymbol().getText();

    VariableType beginExpression = visit(context.expression(0));
    VariableType endExpression = visit(context.expression(1));

    if (!(beginExpression instanceof PrimitiveVariableType) || (beginExpression.basePrimitiveType != endExpression.basePrimitiveType)) {
      System.out.printf("SEMANTIC ERROR (%d): incompatible begin and end variable types: %s and %s.\n", context.FOR().getSymbol().getLine(), beginExpression.toString(), endExpression.toString());

      throw new SemanticErrorException();
    }

    boolean isDownTo = context.DOWNTO() != null;

    if (beginExpression instanceof ConstantPrimitiveVariableType beginExpressionWithValue && endExpression instanceof ConstantPrimitiveVariableType endExpressionWithValue) {
      boolean isBeginSmallerThanEnd = switch (beginExpressionWithValue.value) {
      case Integer _ -> (int) beginExpressionWithValue.value < (int) endExpressionWithValue.value;
      case Double _ -> (double) beginExpressionWithValue.value < (double) endExpressionWithValue.value;
      case Character _ -> (char) beginExpressionWithValue.value < (char) endExpressionWithValue.value;
      case Boolean _ -> !((boolean) beginExpressionWithValue.value) && (boolean) endExpressionWithValue.value;
      default -> throw new RuntimeException("Control variable of for statement must be an ordinal type");
      };

      boolean isBeginBiggerThanEnd = switch (beginExpressionWithValue.value) {
      case Integer _ -> (int) beginExpressionWithValue.value > (int) endExpressionWithValue.value;
      case Double _ -> (double) beginExpressionWithValue.value > (double) endExpressionWithValue.value;
      case Character _ -> (char) beginExpressionWithValue.value > (char) endExpressionWithValue.value;
      case Boolean _ -> (boolean) beginExpressionWithValue.value && !((boolean) endExpressionWithValue.value);
      default -> throw new RuntimeException("Control variable of for statement must be an ordinal type");
      };

      if (isDownTo && !isBeginBiggerThanEnd) {
        System.out.printf("SEMANTIC ERROR (%d): incompatible begin and end variable values: %s downto %s.\n", context.FOR().getSymbol().getLine(), beginExpressionWithValue.value.toString(), endExpressionWithValue.value.toString());

        throw new SemanticErrorException();
      }
      else
        if (!isDownTo && !isBeginSmallerThanEnd) {
          System.out.printf("SEMANTIC ERROR (%d): incompatible begin and end variable values: %s to %s.\n", context.FOR().getSymbol().getLine(), beginExpressionWithValue.value.toString(), endExpressionWithValue.value.toString());

          throw new SemanticErrorException();
        }
    }

    PrimitiveVariableType forLoopType = (PrimitiveVariableType) beginExpression;

    VariableTableEntry globalVariableEntry = globalVariablesTable.get(identifier.getSymbol().getText());
    if (globalVariableEntry != null) {
      checkVariableIsOrdinal(identifier.getSymbol(), globalVariableEntry.type);
      checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) globalVariableEntry.type, forLoopType);

      return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
    }

    RuleContext parent = context.parent;
    while (!(parent instanceof ProgramContext)) {
      if (parent instanceof Function_declarationContext functionDeclarationContext) {
        Function_headingContext functionHeading = functionDeclarationContext.function_heading();
        String functionIdentifier = functionHeading.IDENTIFIER().getText();

        ProceduresAndFunctionsEntry paramterEntry = proceduresAndFunctionsTable.get(functionIdentifier);

        VariableTableEntry parameterEntry = paramterEntry.parameters.get(variableIdentifier);
        checkVariableIsOrdinal(identifier.getSymbol(), parameterEntry.type);
        checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) parameterEntry.type, forLoopType);

        VariableTableEntry localEntry = paramterEntry.localVariables.get(variableIdentifier);
        checkVariableIsOrdinal(identifier.getSymbol(), localEntry.type);
        checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) localEntry.type, forLoopType);

        return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
      }
      else
        if (parent instanceof Procedure_declarationContext procedureDeclarationContext) {
          Procedure_headingContext procedureHeading = procedureDeclarationContext.procedure_heading();
          String procedureIdentifier = procedureHeading.IDENTIFIER().getText();

          ProceduresAndFunctionsEntry paramterEntry = proceduresAndFunctionsTable.get(procedureIdentifier);

          VariableTableEntry parameterEntry = paramterEntry.parameters.get(variableIdentifier);
          checkVariableIsOrdinal(identifier.getSymbol(), parameterEntry.type);
          checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) parameterEntry.type, forLoopType);

          VariableTableEntry localEntry = paramterEntry.localVariables.get(variableIdentifier);
          checkVariableIsOrdinal(identifier.getSymbol(), localEntry.type);
          checkPrimitiveTypesAreEqual(context.FOR().getSymbol().getLine(), (PrimitiveVariableType) localEntry.type, forLoopType);

          return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
        }

      parent = parent.parent;
    }

    System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.\n", identifier.getSymbol().getLine(), variableIdentifier);

    throw new SemanticErrorException();
  }

  /**
   * Visita o contexto de declaração de atribuição e realiza verificações semânticas relacionadas aos tipos das variáveis envolvidas na atribuição.
   * Verifica se os tipos das variáveis à esquerda e à direita da atribuição são compatíveis, considerando arrays, strings e tipos primitivos.
   *
   * @param context O contexto da declaração de atribuição.
   * @return O tipo da variável à esquerda da atribuição, caso a atribuição seja válida.
   */
  @Override
  public VariableType visitAssignment_statement(Assignment_statementContext context) {
    VariableType leftType = visit(context.variable_access());
    VariableType rightType = visit(context.expression());

    if (leftType instanceof PrimitiveVariableType && leftType.basePrimitiveType == PrimitiveTypeEnum.CHAR && leftType.isIndexed) {
      System.out.printf("SEMANTIC ERROR (%d): string cannot be indexed in lhs expression!\n", context.ASSIGNMENT().getSymbol().getLine());

      throw new SemanticErrorException();
    }

    if (leftType instanceof ArrayVariableType || rightType instanceof ArrayVariableType) {
      if (!rightType.isEquivalent(leftType)) {
        System.out.printf("SEMANTIC ERROR (%d): incompatible type: type expected is %s, and the type is %s!\n", context.ASSIGNMENT().getSymbol().getLine(), leftType.toString(), rightType.toString());

        throw new SemanticErrorException();
      }

      return leftType;
    }

    PrimitiveTypeEnum returnType = TypeRules.getResultType(TypeRules.ASSIGNMENT_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);

    if (returnType == PrimitiveTypeEnum.NO_TYPE) {
      System.out.printf("SEMANTIC ERROR (%d): Assignment statement has incompatible types.\n", context.ASSIGNMENT().getSymbol().getLine());

      throw new SemanticErrorException();
    }

    return new PrimitiveVariableType(returnType);
  }

  /**
   * Verifica a lista de parâmetros de uma chamada de procedimento ou função em relação à lista de parâmetros esperada.
   * Gera erros semânticos caso o número de argumentos seja diferente do esperado ou se os tipos dos argumentos não forem compatíveis com os tipos dos parâmetros.
   *
   * @param actualParameterList O contexto da lista de parâmetros reais fornecida na chamada.
   * @param parametersList A lista de entradas de tabela de variáveis representando os parâmetros esperados.
   * @param returnType O tipo de retorno esperado da função ou procedimento.
   * @param entryIdentifier O identificador do procedimento ou função sendo chamado.
   * @param line O número da linha onde ocorreu a chamada.
   * @param type O tipo do procedimento ou função (PROCEDURE ou FUNCTION).
   * @return Um objeto PrimitiveVariableType representando o tipo de retorno da função ou procedimento.
   */
  private PrimitiveVariableType checkParameterList(Actual_parameter_listContext actualParameterList, List<VariableTableEntry> parametersList, PrimitiveTypeEnum returnType, String entryIdentifier, int line, ProcedureOrFunctionEnum type) {
    if (parametersList.isEmpty()) {
      return new PrimitiveVariableType(returnType);
    }

    List<Actual_parameterContext> actualParameters = actualParameterList.actual_parameter();

    if (actualParameters.size() != parametersList.size()) {
      System.out.printf("SEMANTIC ERROR (%d): Invalid number of arguments to procedure '%s'.\n", line, entryIdentifier);

      throw new SemanticErrorException();
    }

    int i = 0;
    for (VariableTableEntry parameter : parametersList) {
      Actual_parameterContext actualParameter = actualParameters.get(i);
      VariableType actualParameterType = visit(actualParameter);

      if (!actualParameterType.isEquivalent(parameter.type)) {
        System.out.printf("SEMANTIC ERROR (%d): Invalid type '%s' for parameter '%s' of %s '%s'.\n", line, actualParameterType.toString(), parameter.identifier, type.toString(), entryIdentifier);

        throw new SemanticErrorException();
      }

      i++;
    }

    return new PrimitiveVariableType(returnType);
  }

  /**
   * Visita o contexto de declaração de procedimento e realiza verificações semânticas relacionadas à chamada do procedimento.
   * Verifica se o procedimento é uma função ou um procedimento embutido, e se os tipos dos argumentos fornecidos são compatíveis com os tipos dos parâmetros esperados.
   *
   * @param context O contexto da declaração de procedimento.
   * @return Um objeto PrimitiveVariableType representando o tipo de retorno do procedimento (NO_TYPE para procedimentos).
   */
  @Override
  public PrimitiveVariableType visitProcedure_statement(Procedure_statementContext context) {
    TerminalNode identifier = context.IDENTIFIER();
    Actual_parameter_listContext actualParameterList = context.actual_parameter_list();
    String procedureName = identifier.getText();

    if (procedureName.equalsIgnoreCase("read") || procedureName.equalsIgnoreCase("readln")) {
      if (actualParameterList != null) {
        for (Actual_parameterContext actualParameter : actualParameterList.actual_parameter()) {
          VariableType actualParameterType = visit(actualParameter);

          Variable_accessContext varAccess = getVariableAccess(actualParameter);
          if (varAccess == null) {
            System.out.printf("SEMANTIC ERROR (%d): Arguments of '%s' must be variables (L-Value).\n", actualParameter.start.getLine(), procedureName);
            throw new SemanticErrorException();
          }

          if (!(actualParameterType instanceof PrimitiveVariableType)) {
            System.out.printf("SEMANTIC ERROR (%d): Invalid type '%s' for argument of procedure '%s'. Only primitive types are allowed.\n", actualParameter.start.getLine(), actualParameterType.toString(), procedureName);
            throw new SemanticErrorException();
          }

          PrimitiveTypeEnum type = actualParameterType.basePrimitiveType;
          if (type != PrimitiveTypeEnum.INTEGER && type != PrimitiveTypeEnum.REAL && type != PrimitiveTypeEnum.CHAR && type != PrimitiveTypeEnum.STRING && type != PrimitiveTypeEnum.BOOLEAN) {

            System.out.printf("SEMANTIC ERROR (%d): Invalid type '%s' for '%s' argument.\n", actualParameter.start.getLine(), actualParameterType.toString(), procedureName);
            throw new SemanticErrorException();
          }
        }
      }
      return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
    }

    BuiltInProceduresAndFunctionsEntry builtInProcedureEntry = builtInProceduresAndFunctionsTable.get(identifier.getText());

    if (builtInProcedureEntry != null) {
      return checkParameterList(actualParameterList, builtInProcedureEntry.parameters.toList(), builtInProcedureEntry.returnType, builtInProcedureEntry.identifier, identifier.getSymbol().getLine(), ProcedureOrFunctionEnum.PROCEDURE);
    }

    ProceduresAndFunctionsEntry procedureEntry = proceduresAndFunctionsTable.get(identifier.getText());

    if (procedureEntry == null) {
      System.out.printf("SEMANTIC ERROR (%d): Procedure '%s' is not defined.\n", identifier.getSymbol().getLine(), identifier.getText());

      throw new SemanticErrorException();
    }

    return checkParameterList(actualParameterList, procedureEntry.parameters.toList(), procedureEntry.returnType, procedureEntry.identifier, identifier.getSymbol().getLine(), ProcedureOrFunctionEnum.PROCEDURE);
  }

  /**
   * Visita o contexto de designador de função e realiza verificações semânticas relacionadas à chamada da função.
   * Verifica se a função é uma função embutida ou uma função definida pelo usuário, e se os tipos dos argumentos fornecidos são compatíveis com os tipos dos parâmetros esperados.
   *
   * @param context O contexto do designador de função.
   * @return O tipo de retorno da função chamada.
   */
  @Override
  public VariableType visitFunction_designator(Function_designatorContext context) {
    TerminalNode identifier = context.IDENTIFIER();
    Actual_parameter_listContext actualParameterList = context.actual_parameter_list();

    BuiltInProceduresAndFunctionsEntry builtInFunctionEntry = builtInProceduresAndFunctionsTable.get(identifier.getText());

    if (builtInFunctionEntry != null) {
      if (builtInFunctionEntry.type == ProcedureOrFunctionEnum.PROCEDURE) {
        System.out.printf("SEMANTIC ERROR (%d): Built-in procedure '%s' is not an expression.\n", identifier.getSymbol().getLine(), identifier.getText());

        throw new SemanticErrorException();
      }

      return checkParameterList(actualParameterList, builtInFunctionEntry.parameters.toList(), builtInFunctionEntry.returnType, builtInFunctionEntry.identifier, identifier.getSymbol().getLine(), ProcedureOrFunctionEnum.FUNCTION);
    }

    ProceduresAndFunctionsEntry functionEntry = proceduresAndFunctionsTable.get(identifier.getText());

    if (functionEntry == null) {
      System.out.printf("SEMANTIC ERROR (%d): Function '%s' is not defined.\n", identifier.getSymbol().getLine(), identifier.getText());

      throw new SemanticErrorException();
    }
    else
      if (functionEntry.type == ProcedureOrFunctionEnum.PROCEDURE) {
        System.out.printf("SEMANTIC ERROR (%d): Procedure '%s' is not an expression.\n", identifier.getSymbol().getLine(), identifier.getText());

        throw new SemanticErrorException();
      }

    return checkParameterList(actualParameterList, functionEntry.parameters.toList(), functionEntry.returnType, functionEntry.identifier, identifier.getSymbol().getLine(), ProcedureOrFunctionEnum.FUNCTION);
  }

  /**
   * Visita o contexto de expressão e realiza verificações semânticas relacionadas aos tipos das expressões envolvidas.
   * Verifica se os tipos das expressões à esquerda e à direita da operação relacional são compatíveis, considerando arrays e tipos primitivos.
   *
   * @param context O contexto da expressão.
   * @return O tipo resultante da expressão, caso a operação seja válida.
   */
  @Override
  public VariableType visitExpression(ExpressionContext context) {
    VariableType leftType = visit(context.simple_expression(0));

    if (context.relational_operator() == null) {
      return leftType;
    }

    VariableType rightType = visit(context.simple_expression(1));

    if (leftType instanceof ArrayVariableType || rightType instanceof ArrayVariableType) {
      System.out.printf("SEMANTIC ERROR (%d): Array types are not compatible with comparison operations.\n", context.relational_operator().start.getLine());

      throw new SemanticErrorException();
    }

    if (leftType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE || rightType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
      return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
    }

    PrimitiveTypeEnum returnType = TypeRules.getResultType(TypeRules.RELATIONAL_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);

    return new PrimitiveVariableType(returnType);
  }

  /**
   * Visita o contexto de expressão simples e realiza verificações semânticas relacionadas aos tipos das expressões envolvidas.
   * Verifica se os tipos das expressões à esquerda e à direita da operação de adição ou subtração são compatíveis, considerando arrays e tipos primitivos.
   *
   * @param context O contexto da expressão simples.
   * @return O tipo resultante da expressão simples, caso a operação seja válida.
   */
  @Override
  public VariableType visitSimple_expression(Simple_expressionContext context) {
    VariableType firstType = visit(context.term(0));

    if (context.adding_operator() == null) {
      return firstType;
    }

    int i = 1;
    VariableType leftType = firstType;

    for (Adding_operatorContext operator : context.adding_operator()) {
      VariableType rightType = visit(context.term(i));

      TerminalNode concreteOperator;
      VariableType returnType;

      if (operator.PLUS() != null) {
        concreteOperator = operator.PLUS();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.PLUS_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else
        if (operator.MINUS() != null) {
          concreteOperator = operator.MINUS();
          PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.MATH_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
          returnType = new PrimitiveVariableType(result);
        }
        else {
          concreteOperator = operator.OR();
          PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.LOGICAL_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
          returnType = new PrimitiveVariableType(result);
        }

      if (!(leftType instanceof PrimitiveVariableType) || !(rightType instanceof PrimitiveVariableType)) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      if (leftType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE || rightType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
      }

      if (returnType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      leftType = returnType;
      i++;
    }

    return leftType;
  }

  /**
   * Visita o contexto de termo e realiza verificações semânticas relacionadas aos tipos das expressões envolvidas.
   * Verifica se os tipos das expressões à esquerda e à direita da operação de multiplicação, divisão ou módulo são compatíveis, considerando arrays e tipos primitivos.
   *
   * @param context O contexto do termo.
   * @return O tipo resultante do termo, caso a operação seja válida.
   */
  @Override
  public VariableType visitTerm(TermContext context) {
    VariableType firstType = visit(context.factor(0));

    if (context.multiplying_operator() == null) {
      return firstType;
    }

    int i = 1;
    VariableType leftType = firstType;

    for (Multiplying_operatorContext operator : context.multiplying_operator()) {
      VariableType rightType = visit(context.factor(i));

      TerminalNode concreteOperator;
      VariableType returnType;

      if (operator.MULTIPLICATION() != null) {
        concreteOperator = operator.MULTIPLICATION();
        PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.MATH_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
        returnType = new PrimitiveVariableType(result);
      }
      else
        if (operator.DIVISION() != null) {
          concreteOperator = operator.DIVISION();
          PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.REAL_DIVISION_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
          returnType = new PrimitiveVariableType(result);
        }
        else
          if (operator.DIV() != null) {
            concreteOperator = operator.DIV();
            PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.INTEGER_DIVISION_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
            returnType = new PrimitiveVariableType(result);
          }
          else {
            concreteOperator = operator.AND();
            PrimitiveTypeEnum result = TypeRules.getResultType(TypeRules.LOGICAL_TABLE, leftType.basePrimitiveType, rightType.basePrimitiveType);
            returnType = new PrimitiveVariableType(result);
          }

      if (!(leftType instanceof PrimitiveVariableType) || !(rightType instanceof PrimitiveVariableType)) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      if (leftType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE || rightType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
      }

      if (returnType.basePrimitiveType == PrimitiveTypeEnum.NO_TYPE) {
        binaryOperationTypeError(concreteOperator.getSymbol().getLine(), concreteOperator.getText(), leftType, rightType);
      }

      leftType = returnType;
      i++;
    }

    return leftType;
  }

  /**
   * Visita o contexto de acesso a variável e retorna o tipo da variável acessada.
   * @param context O contexto de acesso a variável.
   * @return O tipo da variável acessada.
   */
  @Override
  public VariableType visitVariableAccess(VariableAccessContext context) {
    return visit(context.variable_access());
  }

  /**
   * Visita o contexto de constante string e retorna o tipo da constante.
   * Adiciona a constante à tabela de literais de string.
   * @param context O contexto de constante string.
   * @return O tipo da constante string.
   */
  @Override
  public ConstantPrimitiveVariableType<?> visitStringConstant(StringConstantContext context) {
    String stringLiteral = context.CHARACTER_STRING().getText();

    String croppedStringLiteral = stringLiteral.substring(1, stringLiteral.length() - 1);

    stringLiteralsTable.addStringLiteral(croppedStringLiteral);

    if (croppedStringLiteral.length() == 1) {
      return new ConstantPrimitiveVariableType<Character>(PrimitiveTypeEnum.CHAR, croppedStringLiteral.charAt(0));
    }

    return new ConstantPrimitiveVariableType<String>(PrimitiveTypeEnum.STRING, croppedStringLiteral);
  }

  /**
   * Visita o contexto de constante numérica e retorna o tipo da constante.
   * @param context O contexto de constante numérica.
   * @return O tipo da constante numérica.
   */
  @Override
  public ConstantPrimitiveVariableType<?> visitNumeric_constant(Numeric_constantContext context) {
    boolean signal = context.MINUS() != null;

    if (context.UNSIGNED_INTEGER() != null) {
      int unsignedInteger = Integer.parseInt(context.UNSIGNED_INTEGER().getText());
      return new ConstantPrimitiveVariableType<Integer>(PrimitiveTypeEnum.INTEGER, signal ? -unsignedInteger : unsignedInteger);
    }

    double unsignedReal = Double.parseDouble(context.UNSIGNED_REAL().getText());
    return new ConstantPrimitiveVariableType<Double>(PrimitiveTypeEnum.REAL, signal ? -unsignedReal : unsignedReal);
  }

  /**
   * Visita o contexto de constante booleana e retorna o tipo da constante.
   * @param context O contexto de constante booleana.
   * @return O tipo da constante booleana.
   */
  @Override
  public ConstantPrimitiveVariableType<Boolean> visitBoolean_constant(Boolean_constantContext context) {
    if (context.TRUE() != null) {
      return new ConstantPrimitiveVariableType<Boolean>(PrimitiveTypeEnum.BOOLEAN, true);
    }

    return new ConstantPrimitiveVariableType<Boolean>(PrimitiveTypeEnum.BOOLEAN, false);
  }

  /**
   * Visita o contexto de constante numérica e retorna o tipo da constante.
   * @param context O contexto de constante numérica.
   * @return O tipo da constante numérica.
   */
  @Override
  public ConstantPrimitiveVariableType<?> visitNumericConstant(NumericConstantContext context) {
    Numeric_constantContext numericConstant = context.numeric_constant();
    return (ConstantPrimitiveVariableType<?>) visit(numericConstant);
  }

  /**
   * Visita o contexto de constante booleana e retorna o tipo da constante.
   * @param context O contexto de constante booleana.
   * @return O tipo da constante booleana.
   */
  @Override
  public ConstantPrimitiveVariableType<Boolean> visitBooleanConstant(BooleanConstantContext context) {
    Boolean_constantContext booleanConstant = context.boolean_constant();
    return visitBoolean_constant(booleanConstant);
  }

  /**
   * Visita o contexto de chamada de função e realiza verificações semânticas relacionadas à chamada da função.
   * Verifica se a função é uma função embutida ou uma função definida pelo usuário, e se os tipos dos argumentos fornecidos são compatíveis com os tipos dos parâmetros esperados.
   *
   * @param context O contexto da chamada de função.
   * @return O tipo de retorno da função chamada.
   */
  @Override
  public VariableType visitFunctionCall(FunctionCallContext context) {
    Function_designatorContext functionDesignator = context.function_designator();
    return visit(functionDesignator);
  }

  /**
   * Visita o contexto de expressão entre parênteses e retorna o tipo da expressão contida.
   * @param context O contexto de expressão entre parênteses.
   * @return O tipo da expressão contida.
   */
  @Override
  public VariableType visitParenthesisExpression(ParenthesisExpressionContext context) {
    ExpressionContext expression = context.expression();
    return visit(expression);
  }

  /**
   * Visita o contexto de fator negado e realiza verificações semânticas relacionadas ao tipo do fator.
   * Verifica se o tipo do fator é booleano, caso contrário, gera um erro semântico.
   *
   * @param context O contexto de fator negado.
   * @return Um objeto PrimitiveVariableType com tipo BOOLEAN, indicando que o resultado da operação NOT é booleano.
   */
  @Override
  public VariableType visitNotFactor(NotFactorContext context) {
    FactorContext factor = context.factor();

    VariableType returnType = visit(factor);

    if (!(returnType instanceof PrimitiveVariableType && returnType.basePrimitiveType == PrimitiveTypeEnum.BOOLEAN)) {
      TerminalNode not = context.NOT();
      unaryOperationTypeError(not.getSymbol().getLine(), not.getText(), returnType);
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.BOOLEAN);
  }

  /**
   * Visita o contexto de declaração de laço "if" e realiza verificações semânticas relacionadas ao tipo da expressão condicional.
   * Verifica se a expressão condicional é do tipo booleano, caso contrário, gera um erro semântico.
   *
   * @param context O contexto da declaração do laço "if".
   * @return Um objeto PrimitiveVariableType com tipo NO_TYPE, indicando que não há tipo associado à declaração do laço "if".
   */
  @Override
  public VariableType visitIf_statement(If_statementContext context) {
    visit(context.statement());

    if (context.else_part() != null) {
      visit(context.else_part());
    }

    VariableType expressionType = visit(context.expression());

    if (!(expressionType instanceof PrimitiveVariableType && expressionType.basePrimitiveType == PrimitiveTypeEnum.BOOLEAN)) {
      System.out.printf("SEMANTIC ERROR (%d): if expression must be boolean.\n", context.IF().getSymbol().getLine());

      throw new SemanticErrorException();
    }

    return new PrimitiveVariableType(PrimitiveTypeEnum.NO_TYPE);
  }
}