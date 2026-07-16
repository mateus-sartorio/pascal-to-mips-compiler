package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.ArrayVariableType;
import types.PrimitiveVariableType;

/**
 * Classe que representa a memória do interpretador, responsável por armazenar variáveis e seus valores.
 */
public class Memory {
  /**
   * Classe interna que representa uma entrada de variável na memória, contendo o índice e o tamanho da variável.
   */
  public record VariableEntry(int index, int size) {}

  /**
   * Lista que representa a memória, armazenando valores do tipo Word.
   */
  private final ArrayList<Word> memory;

  /**
   * Mapa que associa identificadores de variáveis aos seus respectivos índices e tamanhos na memória.
   */
  private final Map<String, VariableEntry> identifierIndexMap;
  
  /**
   * Construtor da classe Memory, que inicializa a memória com base na tabela de variáveis fornecida.
   * @param variablesTable A tabela de variáveis que contém informações sobre as variáveis a serem armazenadas na memória.
   */
  public Memory(VariablesTable variablesTable) {
    this.identifierIndexMap = new HashMap<>();
    this.memory = new ArrayList<>();

    int i = 0;
    for(VariableTableEntry variable : variablesTable.toList()) {
      switch(variable.type) {
        case PrimitiveVariableType _ -> {
          this.memory.add(Word.fromInt(0));
          this.identifierIndexMap.put(variable.identifier.toLowerCase(), new VariableEntry(i, 1));
          i++;
        }
        case ArrayVariableType type -> {
          this.identifierIndexMap.put(variable.identifier.toLowerCase(), new VariableEntry(i, type.size()));
          
          for(int j = type.lowerBound; j <= type.upperBound; j++) {
            this.memory.add(Word.fromInt(0));
            i++;
          }
        }
        default -> throw new RuntimeException("Unsupported variable type");
      }
    }
  }

  /**
   * Retorna o endereço (índice) de uma variável na memória com base no seu identificador.
   * @param identifier O identificador da variável.
   * @return O endereço (índice) da variável na memória.
   */
  private int addressOf(String identifier) {
    return entryOf(identifier).index;
  }

  /**
   * Retorna a entrada de variável correspondente a um identificador na memória.
   * @param identifier O identificador da variável.
   * @return A entrada de variável correspondente ao identificador.
   */
  public VariableEntry entryOf(String identifier) {
    return identifierIndexMap.get(identifier.toLowerCase());
  }

  /**
   * Armazena um valor inteiro na memória em um endereço específico.
   * @param address O endereço (índice) na memória onde o valor será armazenado.
   * @param value O valor inteiro a ser armazenado.
   */
  private void storeInteger(int address, int value) {
    memory.set(address, Word.fromInt(value));
  }

  /**
   * Armazena um valor inteiro na memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @param value O valor inteiro a ser armazenado.
   */
  public void storeInteger(String identifier, int value) {
    int address = addressOf(identifier);
    storeInteger(address, value);
  }

  /**
   * Armazena um valor inteiro na memória em um endereço específico, considerando um deslocamento (offset).
   * @param identifier O identificador da variável.
   * @param value O valor inteiro a ser armazenado.
   * @param offset O deslocamento (offset) em relação ao endereço base da variável.
   */
  public void storeIntegerAt(String identifier, int value, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    storeInteger(address, value);
  }

  /**
   * Armazena um array de valores inteiros na memória em um endereço específico.
   * @param address O endereço (índice) na memória onde o array será armazenado.
   * @param values O array de valores inteiros a ser armazenado.
   */
  private void storeIntegerArray(int address, int[] values) {
    int i = address;
    for(int value : values) {
      memory.set(i, Word.fromInt(value));
      i++;
    }
  }

  /**
   * Armazena um array de valores inteiros na memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @param values O array de valores inteiros a ser armazenado.
   */
  public void storeIntegerArray(String identifier, int[] values) {
    VariableEntry entry = entryOf(identifier);
    assert entry.size == values.length;
    storeIntegerArray(entry.index, values);
  }

  /**
   * Carrega um valor inteiro da memória em um endereço específico.
   * @param address O endereço (índice) na memória de onde o valor será carregado.
   * @return O valor inteiro carregado da memória.
   */
  private int loadInteger(int address) {
    return memory.get(address).toInt();
  }

  /**
   * Carrega um valor inteiro da memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @return O valor inteiro carregado da memória.
   */
  public int loadInteger(String identifier) {
    int address = addressOf(identifier);
    return loadInteger(address);
  }

  /**
   * Carrega um valor inteiro da memória em um endereço específico, considerando um deslocamento (offset).
   * @param identifier O identificador da variável.
   * @param offset O deslocamento (offset) em relação ao endereço base da variável.
   * @return O valor inteiro carregado da memória.
   */
  public int loadIntegerAt(String identifier, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    return loadInteger(address);
  }

  /**
   * Carrega um array de valores inteiros da memória em um endereço específico.
   * @param baseAddress O endereço (índice) na memória de onde o array será carregado.
   * @param size O tamanho do array a ser carregado.
   * @return O array de valores inteiros carregado da memória.
   */
  private int[] loadIntegerArray(int baseAddress, int size) {
    int[] values = new int[size];
    
    for(int i = 0; i < size; i++) {
      values[i] = memory.get(baseAddress + i).toInt(); 
    }

    return values;
  }

  /**
   * Carrega um array de valores inteiros da memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @return O array de valores inteiros carregado da memória.
   */
  public int[] loadIntegerArray(String identifier) {
    VariableEntry entry = entryOf(identifier);
    return loadIntegerArray(entry.index, entry.size);
  }

  /**
   * Armazena um valor float na memória em um endereço específico.
   * @param address O endereço (índice) na memória onde o valor será armazenado.
   * @param value O valor float a ser armazenado.
   */
  private void storeFloat(int address, float value) {
    memory.set(address, Word.fromFloat(value));
  }

  /**
   * Armazena um valor float na memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @param value O valor float a ser armazenado.
   */
  public void storeFloat(String identifier, float value) {
    int address = addressOf(identifier);
    storeFloat(address, value);
  }

  /**
   * Armazena um valor float na memória em um endereço específico, considerando um deslocamento (offset).
   * @param identifier O identificador da variável.
   * @param value O valor float a ser armazenado.
   * @param offset O deslocamento (offset) em relação ao endereço base da variável.
   */
  public void storeFloatAt(String identifier, float value, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    storeFloat(address, value);
  }

  /**
   * Armazena um array de valores float na memória em um endereço específico.
   * @param address O endereço (índice) na memória onde o array será armazenado.
   * @param values O array de valores float a ser armazenado.
   */
  private void storeFloatArray(int address, float[] values) {
    int i = address;
    for(float value : values) {
      memory.set(i, Word.fromFloat(value));
      i++;
    }
  }

  /**
   * Armazena um array de valores float na memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @param values O array de valores float a ser armazenado.
   */
  public void storeFloatArray(String identifier, float[] values) {
    VariableEntry entry = entryOf(identifier);
    assert entry.size == values.length;
    storeFloatArray(entry.index, values);
  }

  /**
   * Carrega um valor float da memória em um endereço específico.
   * @param address O endereço (índice) na memória de onde o valor será carregado.
   * @return O valor float carregado da memória.
   */
  private float loadFloat(int address) {
    return memory.get(address).toFloat();
  }

  /**
   * Carrega um valor float da memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @return O valor float carregado da memória.
   */
  public float loadFloatAt(String identifier, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    return loadFloat(address);
  }

  /**
   * Carrega um valor float da memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @return O valor float carregado da memória.
   */
  public float loadFloat(String identifier) {
    int address = addressOf(identifier);
    return loadFloat(address);
  }

  /**
   * Carrega um array de valores float da memória em um endereço específico.
   * @param baseAddress O endereço (índice) na memória de onde o array será carregado.
   * @param size O tamanho do array a ser carregado.
   * @return O array de valores float carregado da memória.
   */
  private float[] loadFloatArray(int baseAddress, int size) {
    float[] values = new float[size];
    
    for(int i = 0; i < size; i++) {
      values[i] = memory.get(baseAddress + i).toFloat(); 
    }

    return values;
  }

  /**
   * Carrega um array de valores float da memória com base no identificador da variável.
   * @param identifier O identificador da variável.
   * @return O array de valores float carregado da memória.
   */
  public float[] loadFloatArray(String identifier) {
    VariableEntry entry = entryOf(identifier);
    return loadFloatArray(entry.index, entry.size);
  }
}