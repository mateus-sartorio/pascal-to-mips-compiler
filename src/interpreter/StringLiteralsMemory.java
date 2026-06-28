package interpreter;

import java.util.List;

import tables.StringLiteralsTable;

public class StringLiteralsMemory {
  private final List<String> stringLiteralsList;

  public StringLiteralsMemory(StringLiteralsTable stringLiteralsTable) {
    this.stringLiteralsList = stringLiteralsTable.toList();
  }

  public int addEntry(String entry) {
    if(!stringLiteralsList.contains(entry)) {
      stringLiteralsList.add(entry);
    }

    return getIndex(entry);
  }

  public int getIndex(String entry) {
    return stringLiteralsList.indexOf(entry);
  }

  public String getEntry(int index) {
    return stringLiteralsList.get(index);
  }
}
