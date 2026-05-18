package checker;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.PascalParser;
import parser.PascalParserBaseVisitor;

public class SemanticChecker extends PascalParserBaseVisitor<Void> {

  private static class Entry {
    public String varID;
    public String varType;
    public int line;
  }

  // Tabela de símbolos para armazenar as variáveis declaradas no código
  private Map<String, Entry> symbolsTable = new LinkedHashMap<>();

  public String printSymbolsTable() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(String.format("%-15s %-15s %s\n", "ID", "TYPE", "LINE"));
    
    for (Entry entry : symbolsTable.values()) {
        sb.append(String.format("%-15s %-15s %d\n", entry.varID, entry.varType, entry.line));
    }
    
    return sb.toString();
}

  // Tabela de strings para armazenar as strings literais encontradas no código
  private Set<String> stringsTable = new LinkedHashSet<>();

  public String printStringsTable() {
    StringBuilder sb = new StringBuilder();
    sb.append("STRINGS\n");
    for (String str : stringsTable) {
      sb.append(str).append("\n");
    }
    return sb.toString();
  }

  // Método auxiliar para verificar se uma variável foi declarada antes de ser
  // usada
  private void checkVar(Token token) {
    String varID = token.getText();
    if (!symbolsTable.containsKey(varID)) {
      System.out.printf("SEMANTIC ERROR (%d): Variable '%s' was not declared.", token.getLine(), varID);
      System.exit(1);
    }
  }

  // Auxiliar reutilizável para cadastrar variáveis na tabela e checar duplicados
  private void registerVariables(PascalParser.Identifier_listContext idListCtx, String varType) {
    if (idListCtx != null) {
      for (TerminalNode idNode : idListCtx.IDENTIFIER()) {
        Token token = idNode.getSymbol();
        String varID = token.getText();

        // Validação de duplicidade de declaração de variável
        if (symbolsTable.containsKey(varID)) {
          System.err.printf("SEMANTIC ERROR (%d): Variable '%s' already declared at line %d.\n",
              token.getLine(), varID, symbolsTable.get(varID).line);
          System.exit(1);
        }

        Entry entry = new Entry();
        entry.varID = varID;
        entry.varType = varType;
        entry.line = token.getLine();

        symbolsTable.put(varID, entry);
      }
    }
  }

  // ------------------ VISITORS DE DECLARAÇÃO -----------------------

  @Override
  public Void visitVariable_declaration(PascalParser.Variable_declarationContext ctx) {
    if (ctx.type_denoter() != null) {
      registerVariables(ctx.identifier_list(), ctx.type_denoter().getText());
    }
    return visitChildren(ctx);
  }

  @Override
  public Void visitValue_parameter_speficiation(PascalParser.Value_parameter_speficiationContext ctx) {
    if (ctx.type_denoter() != null) {
      registerVariables(ctx.identifier_list(), ctx.type_denoter().getText());
    }
    return visitChildren(ctx);
  }

  @Override
  public Void visitVariable_parameter_specification(PascalParser.Variable_parameter_specificationContext ctx) {
    if (ctx.type_denoter() != null) {
      registerVariables(ctx.identifier_list(), ctx.type_denoter().getText());
    }
    return visitChildren(ctx);
  }

  // ------------------- CHECAGEM DE USO DE VARIÁVEIS ------------------

  @Override
  public Void visitVariable_access(PascalParser.Variable_accessContext ctx) {
    if (ctx.IDENTIFIER() != null) {
      checkVar(ctx.IDENTIFIER().getSymbol());
    }
    return visitChildren(ctx);
  }

  @Override
  public Void visitIndexed_variable(PascalParser.Indexed_variableContext ctx) {
    if (ctx.IDENTIFIER() != null) {
      checkVar(ctx.IDENTIFIER().getSymbol());
    }
    return visitChildren(ctx);
  }

  @Override
  public Void visitFor_statement(PascalParser.For_statementContext ctx) {
    if (ctx.IDENTIFIER() != null) {
      checkVar(ctx.IDENTIFIER().getSymbol());
    }
    return visitChildren(ctx);
  }

  // ------------------- CHECAGEM DE USO DE STRINGS ------------------

  @Override
  public Void visitUnsigned_constant(PascalParser.Unsigned_constantContext ctx) {
    // Como unsigned_constant aceita números, precisamos isolar apenas a String
    if (ctx.CHARACTER_STRING() != null) {
      String strVal = ctx.CHARACTER_STRING().getText();
      // Remove as aspas simples de início e fim ('texto' -> texto)
      stringsTable.add(strVal.substring(1, strVal.length() - 1));
    }
    return visitChildren(ctx);
  }

}
