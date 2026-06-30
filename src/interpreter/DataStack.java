package interpreter;

import java.util.Formatter;
import java.util.Stack;

public final class DataStack {
  private final Stack<Word> stack = new Stack<>();

  public void pushInteger(int value) {
    stack.push(Word.fromInt(value));
  }

  public void pushIntegerArray(int[] values) {
    for(int value : values) {
      stack.push(Word.fromInt(value));
    }
  }

  public int popInteger() {
    return stack.pop().toInt();
  }

  public int[] popIntegerArray(int size) {
    int[] values = new int[size];

    for(int i = 0; i < size; i++) {
      values[i] = popInteger(); 
    }

    return values;
  }

  public void pushFloat(float value) {
    stack.push(Word.fromFloat(value));
  }

  public void pushFloatArray(float[] values) {
    for(float value : values) {
      stack.push(Word.fromFloat(value));
    }
  }

  public float popFloat() {
    return stack.pop().toFloat();
  }

  public float[] popFloatArray(int size) {
    float[] values = new float[size];

    for(int i = 0; i < size; i++) {
      values[i] = popFloat(); 
    }

    return values;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    f.format("*** STACK: ");
    
    for (int i = 0; i < stack.size(); i++) {
      f.format("%d ", stack.get(i).toInt());
    }

    f.format("\n");
    f.close();
    
    return sb.toString();
  }
}
