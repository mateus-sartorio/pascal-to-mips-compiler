package interpreter;

import java.util.Vector;

import tables.VariablesTable;

public class Memory extends Vector<Word> {
  public Memory(VariablesTable variablesTable) {
    for (int i = 0; i < variablesTable.size(); i++) {
      this.add(Word.fromInt(0));
    }
  }

  public void storei(int addresss, int value) {
    this.set(addresss, Word.fromInt(value));
  }

  public int loadi(int addresss) {
    return this.get(addresss).toInt();
  }

  public void storef(int addresss, float value) {
    this.set(addresss, Word.fromFloat(value));
  }

  public float loadf(int addresss) {
    return this.get(addresss).toFloat();
  }
}
