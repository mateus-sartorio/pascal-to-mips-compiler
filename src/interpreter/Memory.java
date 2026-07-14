package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tables.VariablesTable;
import tables.VariablesTable.VariableTableEntry;
import types.ArrayVariableType;
import types.PrimitiveVariableType;

public class Memory {
  public record VariableEntry(int index, int size) {}

  private final ArrayList<Word> memory;
  private final Map<String, VariableEntry> identifierIndexMap;
  
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

  private int addressOf(String identifier) {
    return entryOf(identifier).index;
  }

  public VariableEntry entryOf(String identifier) {
    return identifierIndexMap.get(identifier.toLowerCase());
  }

  private void storeInteger(int address, int value) {
    memory.set(address, Word.fromInt(value));
  }

  public void storeInteger(String identifier, int value) {
    int address = addressOf(identifier);
    storeInteger(address, value);
  }

  public void storeIntegerAt(String identifier, int value, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    storeInteger(address, value);
  }

  private void storeIntegerArray(int address, int[] values) {
    int i = address;
    for(int value : values) {
      memory.set(i, Word.fromInt(value));
      i++;
    }
  }

  public void storeIntegerArray(String identifier, int[] values) {
    VariableEntry entry = entryOf(identifier);
    assert entry.size == values.length;
    storeIntegerArray(entry.index, values);
  }

  private int loadInteger(int address) {
    return memory.get(address).toInt();
  }

  public int loadInteger(String identifier) {
    int address = addressOf(identifier);
    return loadInteger(address);
  }

  public int loadIntegerAt(String identifier, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    return loadInteger(address);
  }

  private int[] loadIntegerArray(int baseAddress, int size) {
    int[] values = new int[size];
    
    for(int i = 0; i < size; i++) {
      values[i] = memory.get(baseAddress + i).toInt(); 
    }

    return values;
  }

  public int[] loadIntegerArray(String identifier) {
    VariableEntry entry = entryOf(identifier);
    return loadIntegerArray(entry.index, entry.size);
  }

  private void storeFloat(int address, float value) {
    memory.set(address, Word.fromFloat(value));
  }

  public void storeFloat(String identifier, float value) {
    int address = addressOf(identifier);
    storeFloat(address, value);
  }

  public void storeFloatAt(String identifier, float value, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    storeFloat(address, value);
  }

  private void storeFloatArray(int address, float[] values) {
    int i = address;
    for(float value : values) {
      memory.set(i, Word.fromFloat(value));
      i++;
    }
  }

  public void storeFloatArray(String identifier, float[] values) {
    VariableEntry entry = entryOf(identifier);
    assert entry.size == values.length;
    storeFloatArray(entry.index, values);
  }

  private float loadFloat(int address) {
    return memory.get(address).toFloat();
  }

  public float loadFloatAt(String identifier, int offset) {
    int baseAddress = addressOf(identifier);
    int address = baseAddress + offset;
    return loadFloat(address);
  }

  public float loadFloat(String identifier) {
    int address = addressOf(identifier);
    return loadFloat(address);
  }

  private float[] loadFloatArray(int baseAddress, int size) {
    float[] values = new float[size];
    
    for(int i = 0; i < size; i++) {
      values[i] = memory.get(baseAddress + i).toFloat(); 
    }

    return values;
  }

  public float[] loadFloatArray(String identifier) {
    VariableEntry entry = entryOf(identifier);
    int baseAddress = addressOf(identifier);
    return loadFloatArray(entry.index, entry.size);
  }
}