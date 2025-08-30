package generated;

import com.github.tommyettinger.digital.TextTools;
import com.github.tommyettinger.ds.Junction;
import com.github.tommyettinger.ds.ObjectIntOrderedMap;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;

import java.util.List;
import java.util.Objects;

public class Effect {
  private static final String[] __headerLine;

  static {
    __headerLine = new String[]{"name:s", "operation:s", "valueMul:i", "valueAdd:j{, }i", "type:s", "listensFor:j", "succRemove:s[, ]", "succPut:s[, ]", "removedBy:j", "description:s"};
  }

  public String name;

  public String operation;

  public int valueMul;

  public ObjectIntOrderedMap<Junction<String>> valueAdd;

  public String type;

  public Junction<String> listensFor;

  public String[] succRemove;

  public String[] succPut;

  public Junction<String> removedBy;

  public String description;

  private long __code;

  public Effect() {
  }

  public Effect(String name, String operation, int valueMul,
                ObjectIntOrderedMap<Junction<String>> valueAdd, String type, Junction<String> listensFor,
                String[] succRemove, String[] succPut, Junction<String> removedBy, String description,
                long __code) {
    this.name = name;
    this.operation = operation;
    this.valueMul = valueMul;
    this.valueAdd = valueAdd;
    this.type = type;
    this.listensFor = listensFor;
    this.succRemove = succRemove;
    this.succPut = succPut;
    this.removedBy = removedBy;
    this.description = description;
    this.__code = __code;
  }

  public static ObjectObjectOrderedMap<String, Effect> parseAll(List<String> lines) {
    if(lines == null || lines.isEmpty()) return new ObjectObjectOrderedMap<>();

    String[] header = TextTools.split(lines.get(0), "\t");
    if(!stringArrayEquals(__headerLine, header)) throw new IllegalArgumentException("Header lines do not match: expected " +
            TextTools.join("\t", __headerLine) + ", got " + TextTools.join("\t", header));

    int numLines = lines.size();
    ObjectObjectOrderedMap<String, Effect> all = new ObjectObjectOrderedMap<>(numLines);
    for (int i = 1; i < numLines; i++) {
        String current = lines.get(i);
        String[] split = TextTools.split(current, "\t");
        //now we need generate the bigger code to parse each constructor parameter according to header fields...

    }
    return all;
  }

  public long hash64() {
    return __code;
  }

  public int hashCode() {
    return (int)__code;
  }

  private static boolean stringArrayEquals(String[] left, String[] right) {
    if (left == right) return true;
    if (left == null || right == null) return false;
    final int len = left.length;
    if(len != right.length) return false;
    for (int i = 0; i < len; i++) { if(!Objects.equals(left[i], right[i])) return false; }
    return true;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Effect other = (Effect) o;
    if (!Objects.equals(name, other.name)) return false;
    if (!Objects.equals(operation, other.operation)) return false;
    if (valueMul != other.valueMul) return false;
    if (!Objects.equals(valueAdd, other.valueAdd)) return false;
    if (!Objects.equals(type, other.type)) return false;
    if (!Objects.equals(listensFor, other.listensFor)) return false;
    if(!stringArrayEquals(succRemove, other.succRemove)) return false;
    if(!stringArrayEquals(succPut, other.succPut)) return false;
    if (!Objects.equals(removedBy, other.removedBy)) return false;
    if (!Objects.equals(description, other.description)) return false;
    return true;
  }
}
