package interpreter;

import java.util.List;
import java.util.Stack;

import interpreter.Memory.VariableEntry;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveVariableType;
import types.ProcedureOrFunctionEnum;

/**
 * Representação da pilha de memória do interpretador. Cada frame da pilha representa uma chamada de função ou procedimento, contendo suas variáveis locais e parâmetros.
 */
public class MemoryStack {
  /**
   * Tamanho máximo da pilha de memória. Se a pilha exceder esse tamanho, ocorrerá um erro de stack overflow.
   */
  private final int MAX_SIZE = 100;

  /**
   * Tabela de procedimentos e funções, usada para obter informações sobre os procedimentos e funções definidos no programa.
   */
  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable;

  /**
   * Pilha de memória, onde cada elemento representa um frame de memória para uma chamada de função ou procedimento.
   */
  private final Stack<Memory> stack;

  /**
   * Pilha de tabelas de variáveis, onde cada elemento representa a tabela de variáveis para o frame de memória correspondente.
   */
  private final Stack<VariablesTable> currentTable;
  
  /**
   * Pilha de entradas de procedimentos e funções, onde cada elemento representa a entrada correspondente ao frame de memória atual.
   */
  private final Stack<ProceduresAndFunctionsEntry> currentEntry;

  /**
   * Construtor da classe MemoryStack.
   * 
   * @param proceduresAndFunctionsTable Tabela de procedimentos e funções usada para obter informações sobre os procedimentos e funções definidos no programa.
   */
  public MemoryStack(ProceduresAndFunctionsTable proceduresAndFunctionsTable) {
    this.proceduresAndFunctionsTable = proceduresAndFunctionsTable;
    this.stack = new Stack<>();
    this.currentTable = new Stack<>();
    this.currentEntry = new Stack<>();
  }

  /**
   * Retorna o frame de memória atual (topo da pilha).
   * 
   * @return O frame de memória atual.
   */
  public Memory peek() {
    return stack.peek();
  }

  /**
   * Retorna a tabela de variáveis atual (topo da pilha).
   * 
   * @return A tabela de variáveis atual.
   */
  public VariablesTable peekTable() {
    return currentTable.peek();
  }

  /**
   * Retorna a entrada de procedimento ou função atual (topo da pilha).
   * 
   * @return A entrada de procedimento ou função atual.
   */
  public ProceduresAndFunctionsEntry peekEntry() {
    return currentEntry.peek();
  }

  /**
   * Adiciona um novo frame de memória à pilha, representando uma chamada de função ou procedimento.
   * 
   * @param identifier Identificador do procedimento ou função a ser chamado.
   */
  public void pushFrame(String identifier) {
    if(stack.size() == MAX_SIZE) {
      System.out.printf(
        "RUNTIME ERROR: stack overflow - max stack (%d) size exceeded!\n",
        MAX_SIZE
      );
      
      System.exit(1);
    }

    ProceduresAndFunctionsEntry entry = proceduresAndFunctionsTable.get(identifier);
    
    List<VariableTableEntry> expandedList = entry.parameters.toList();
    expandedList.addAll(entry.localVariables.toList());

    if(entry.type == ProcedureOrFunctionEnum.FUNCTION) {
      expandedList.add(new VariableTableEntry(identifier, new PrimitiveVariableType(entry.returnType)));
    }
    
    VariablesTable expandedTable = new VariablesTable(expandedList);
    
    currentTable.push(expandedTable);
    stack.push(new Memory(expandedTable));
    currentEntry.push(entry);
  }

  /**
   * Remove o frame de memória atual da pilha, retornando ao contexto da chamada anterior.
   */
  public void popFrame() {
    stack.pop();
    currentTable.pop();
    currentEntry.pop();
  }

  /**
   * Retorna a entrada de variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @return A entrada de variável correspondente ao identificador fornecido.
   */
  public VariableEntry entryOf(String identifier) {
    return stack.peek().entryOf(identifier);
  }

  /**
   * Armazena um valor inteiro na variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @param value Valor inteiro a ser armazenado.
   */
  public void storeInteger(String identifier, int value) {
    stack.peek().storeInteger(identifier, value);
  }

  /**
   * Armazena um valor inteiro em uma posição específica de um array de inteiros correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador do array de inteiros.
   * @param value Valor inteiro a ser armazenado.
   * @param offset Posição específica do array onde o valor será armazenado.
   */
  public void storeIntegerAt(String identifier, int value, int offset) {
    stack.peek().storeIntegerAt(identifier, value, offset);
  }

  /**
   * Armazena um array de inteiros na variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @param values Array de inteiros a ser armazenado.
   */
  public void storeIntegerArray(String identifier, int[] values) {
    stack.peek().storeIntegerArray(identifier, values);

  }

  /**
   * Carrega o valor inteiro da variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @return Valor inteiro armazenado na variável.
   */
  public int loadInteger(String identifier) {
    return stack.peek().loadInteger(identifier);
  }

  /**
   * Carrega o valor inteiro de uma posição específica de um array de inteiros correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador do array de inteiros.
   * @param offset Posição específica do array de onde o valor será carregado.
   * @return Valor inteiro armazenado na posição específica do array.
   */
  public int loadIntegerAt(String identifier, int offset) {
    return stack.peek().loadIntegerAt(identifier, offset);
  }

  /**
   * Carrega o array de inteiros da variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @return Array de inteiros armazenado na variável.
   */
  public int[] loadIntegerArray(String identifier) {
    return stack.peek().loadIntegerArray(identifier);
  }

  /**
   * Armazena um valor float na variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @param value Valor float a ser armazenado.
   */
  public void storeFloat(String identifier, float value) {
    stack.peek().storeFloat(identifier, value);
  }

  /**
   * Armazena um valor float em uma posição específica de um array de floats correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador do array de floats.
   * @param value Valor float a ser armazenado.
   * @param offset Posição específica do array onde o valor será armazenado.
   */
  public void storeFloatAt(String identifier, float value, int offset) {
    stack.peek().storeFloatAt(identifier, value, offset);
  }

  /**
   * Armazena um array de floats na variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @param values Array de floats a ser armazenado.
   */
  public void storeFloatArray(String identifier, float[] values) {
    stack.peek().storeFloatArray(identifier, values);
  }

  /**
   * Carrega o valor float da variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @return Valor float armazenado na variável.
   */
  public float loadFloatAt(String identifier, int offset) {
    return stack.peek().loadFloatAt(identifier, offset);
  }

  /**
   * Carrega o valor float de uma posição específica de um array de floats correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador do array de floats.
   * @param offset Posição específica do array de onde o valor será carregado.
   * @return Valor float armazenado na posição específica do array.
   */
  public float loadFloat(String identifier) {
    return stack.peek().loadFloat(identifier);
  }

  /**
   * Carrega o array de floats da variável correspondente ao identificador fornecido no frame de memória atual.
   * 
   * @param identifier Identificador da variável.
   * @return Array de floats armazenado na variável.
   */
  public float[] loadFloatArray(String identifier) {
    return stack.peek().loadFloatArray(identifier);
  }
}
