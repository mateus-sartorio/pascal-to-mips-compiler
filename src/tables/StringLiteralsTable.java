package tables;

import java.util.Formatter;
import java.util.LinkedHashSet;
import java.util.Set;

public final class StringLiteralsTable {
  private Set<String> table = new LinkedHashSet<>();

  public boolean isEmpty() {
    return table.isEmpty();
  }

  public void addStringLiteral(String literal) {
    table.add(literal);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    int i = 0;
    for (String literal : table) {
      f.format("%d - '%s'\n", i, literal);
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}
