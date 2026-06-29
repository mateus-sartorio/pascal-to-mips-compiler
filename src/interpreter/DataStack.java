package interpreter;

import java.util.Formatter;
import java.util.Stack;

public final class DataStack extends Stack<Word> {
  public void pushInteger(int value) {
    super.push(Word.fromInt(value));
  }

  public void pushIntegerArray(int[] values) {
    for(int value : values) {
      super.push(Word.fromInt(value));
    }
  }

  public int popInteger() {
    return super.pop().toInt();
  }

  public int[] popIntegerArray(int size) {
    int[] values = {};

    for(int i = 0; i < size; i++) {
      values[i] = popInteger(); 
    }

    return values;
  }

  public void pushFloat(float value) {
    super.push(Word.fromFloat(value));
  }

  public void pushFloatArray(float[] values) {
    for(float value : values) {
      super.push(Word.fromFloat(value));
    }
  }

  public float popFloat() {
    return super.pop().toFloat();
  }

  public float[] popFloatArray(int size) {
    float[] values = {};

    for(int i = 0; i < size; i++) {
      values[i] = popFloat(); 
    }

    return values;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    f.format("*** STACK: ");
    
    for (int i = 0; i < this.size(); i++) {
      f.format("%d ", this.get(i).toInt());
    }

    f.format("\n");
    f.close();
    
    return sb.toString();
  }
}
