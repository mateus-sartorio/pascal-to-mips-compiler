package interpreter;

import java.util.Stack;

import interpreter.Memory.VariableEntry;
import tables.ProceduresAndFunctionsTable;
import tables.ProceduresAndFunctionsTable.ProceduresAndFunctionsEntry;
import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.PrimitiveVariableType;
import types.ProcedureOrFunctionEnum;

public class MemoryStack {
  private final int MAX_SIZE = 100;

  private final ProceduresAndFunctionsTable proceduresAndFunctionsTable;
  private final Stack<Memory> stack;
  private final Stack<VariablesTable> currentTable;
  private final Stack<ProceduresAndFunctionsEntry> currentEntry;

  public MemoryStack(ProceduresAndFunctionsTable proceduresAndFunctionsTable) {
    this.proceduresAndFunctionsTable = proceduresAndFunctionsTable;
    this.stack = new Stack<>();
    this.currentTable = new Stack<>();
    this.currentEntry = new Stack<>();
  }

  public Memory peek() {
    return stack.peek();
  }

  public VariablesTable peekTable() {
    return currentTable.peek();
  }

  public ProceduresAndFunctionsEntry peekEntry() {
    return currentEntry.peek();
  }

  public void pushFrame(String identifier) {
    if(stack.size() == MAX_SIZE) {
      System.out.printf(
        "RUNTIME ERROR: stack overflow - max stack (%d) size exceeded!\n",
        MAX_SIZE
      );
      
      System.exit(1);
    }

    var entry = proceduresAndFunctionsTable.get(identifier);
    
    var l1 = entry.parameters.toList();
    l1.addAll(entry.localVariables.toList());

    if(entry.type == ProcedureOrFunctionEnum.FUNCTION) {
      l1.add(new VariableTableEntry(identifier, new PrimitiveVariableType(entry.returnType)));
    }
    
    var nT = new VariablesTable(l1);
    
    currentTable.push(nT);
    stack.push(new Memory(nT));
    currentEntry.push(entry);
  }

  public void popFrame() {
    stack.pop();
    currentTable.pop();
    currentEntry.pop();
  }

  public VariableEntry entryOf(String identifier) {
    return stack.peek().entryOf(identifier);
  }

  public void storeInteger(String identifier, int value) {
    stack.peek().storeInteger(identifier, value);
  }

  public void storeIntegerAt(String identifier, int value, int offset) {
    stack.peek().storeIntegerAt(identifier, value, offset);
  }

  public void storeIntegerArray(String identifier, int[] values) {
    stack.peek().storeIntegerArray(identifier, values);

  }

  public int loadInteger(String identifier) {
    return stack.peek().loadInteger(identifier);
  }

  public int loadIntegerAt(String identifier, int offset) {
    return stack.peek().loadIntegerAt(identifier, offset);
  }

  public int[] loadIntegerArray(String identifier) {
    return stack.peek().loadIntegerArray(identifier);
  }

  public void storeFloat(String identifier, float value) {
    stack.peek().storeFloat(identifier, value);
  }

  public void storeFloatAt(String identifier, float value, int offset) {
    stack.peek().storeFloatAt(identifier, value, offset);
  }

  public void storeFloatArray(String identifier, float[] values) {
    stack.peek().storeFloatArray(identifier, values);
  }

  public float loadFloatAt(String identifier, int offset) {
    return stack.peek().loadFloatAt(identifier, offset);
  }

  public float loadFloat(String identifier) {
    return stack.peek().loadFloat(identifier);
  }

  public float[] loadFloatArray(String identifier) {
    return stack.peek().loadFloatArray(identifier);
  }
}
