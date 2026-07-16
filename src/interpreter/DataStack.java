package interpreter;

import java.util.Formatter;
import java.util.Stack;

/**
 * Classe que representa uma pilha de dados (Data Stack) utilizada no interpretador.
 * A pilha armazena valores do tipo Word, que podem representar inteiros ou floats.
 */
public final class DataStack {
  private final Stack<Word> stack = new Stack<>();

  /**
   * Empilha um valor inteiro na pilha de dados.
   * @param value
   */
  public void pushInteger(int value) {
    stack.push(Word.fromInt(value));
  }

  /**
   * Empilha um array de valores inteiros na pilha de dados.
   * @param values
   */
  public void pushIntegerArray(int[] values) {
    for(int value : values) {
      stack.push(Word.fromInt(value));
    }
  }

  /**
   * Desempilha um valor inteiro da pilha de dados.
   * @return O valor inteiro desempilhado.
   */
  public int popInteger() {
    return stack.pop().toInt();
  }

  /**
   * Desempilha um array de valores inteiros da pilha de dados.
   * @param size O tamanho do array a ser desempilhado.
   * @return O array de valores inteiros desempilhado.
   */
  public int[] popIntegerArray(int size) {
    int[] values = new int[size];

    for(int i = size - 1; i >= 0; i--) {
      values[i] = popInteger(); 
    }

    return values;
  }

  /**
   * Empilha um valor float na pilha de dados.
   * @param value O valor float a ser empilhado.
   */
  public void pushFloat(float value) {
    stack.push(Word.fromFloat(value));
  }

  /**
   * Empilha um array de valores float na pilha de dados.
   * @param values O array de valores float a ser empilhado.
   */
  public void pushFloatArray(float[] values) {
    for(float value : values) {
      stack.push(Word.fromFloat(value));
    }
  }

  /**
   * Desempilha um valor float da pilha de dados.
   * @return O valor float desempilhado.
   */
  public float popFloat() {
    return stack.pop().toFloat();
  }

  /**
   * Desempilha um array de valores float da pilha de dados.
   * @param size O tamanho do array a ser desempilhado.
   * @return O array de valores float desempilhado.
   */
  public float[] popFloatArray(int size) {
    float[] values = new float[size];

    for(int i = size - 1; i >= 0; i--) {
      values[i] = popFloat(); 
    }

    return values;
  }

  /**
   * Retorna uma representação em string da pilha de dados.
   * @return Uma string representando a pilha de dados.
   */
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
