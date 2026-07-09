package tables;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StringLiteralsTable {
  private Map<Integer, String> map = new HashMap<>();

  public boolean isEmpty() {
    return map.isEmpty();
  }

  public int size() {
    return map.size();
  }

  public List<String> toList() {
    return new ArrayList<>(map.values());
  }

  public List<Integer> keySet() {
    var list = new ArrayList<>(map.keySet());
    list.sort(null);
    return list;
  }

  public String get(Integer key) {
    return map.get(key);
  }

  public Integer indexOf(String value) {
    return toList().indexOf(value);
  }

  public void addStringLiteral(String literal) {
    map.put(map.size(), literal);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Formatter f = new Formatter(sb);
    
    int i = 0;
    for (String literal : map.values()) {
      f.format("%d. '%s'\n", i, literal);
      i++;
    }
    f.close();
    
    return sb.toString();
  }
}