package interpreter;

import java.util.Vector;

import tables.VariablesTable;

public class Memory extends Vector<Word> {
  public Memory(VariablesTable variablesTable) {
    for (int i = 0; i < variablesTable.size(); i++) {
      this.add(Word.fromInt(0));
    }
  }

  public void storei(int addr, int value) {
    this.set(addr, Word.fromInt(value));
  }

  public int loadi(int addr) {
    return this.get(addr).toInt();
  }

  public void storef(int addr, float value) {
    this.set(addr, Word.fromFloat(value));
  }

  public float loadf(int addr) {
    return this.get(addr).toFloat();
  }
}
