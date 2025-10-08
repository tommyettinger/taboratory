// Generated code produced by taboratory.
// This is here to generate an import statement for PartialParser .
// This is here to generate an import statement for Junction .
// This is here to generate an import statement for Hasher .
// This is here to generate an import statement for IntList .
// This is here to generate an import statement for LongList .
// This is here to generate an import statement for FloatList .
// This is here to generate an import statement for DoubleList .
// This is here to generate an import statement for CharList .
// This is here to generate an import statement for BooleanList .
// This is here to generate an import statement for ObjectList .
package generated;

import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.digital.TextTools;
import com.github.tommyettinger.ds.BooleanList;
import com.github.tommyettinger.ds.CharList;
import com.github.tommyettinger.ds.DoubleList;
import com.github.tommyettinger.ds.FloatList;
import com.github.tommyettinger.ds.IntList;
import com.github.tommyettinger.ds.Junction;
import com.github.tommyettinger.ds.LongList;
import com.github.tommyettinger.ds.ObjectIntOrderedMap;
import com.github.tommyettinger.ds.ObjectList;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import com.github.tommyettinger.ds.support.util.PartialParser;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public class Data {
  public static final String[] __headerLine;

  public static final String[] __defaults;

  static {
    __headerLine = new String[]{"name:s^", "operation:s", "valueMul:i", "valueAdd:j{, }i", "type:s", "listensFor:j", "succRemove:s[, ]", "succPut:s[, ]", "removedBy:j", "description:s"};
    __defaults = new String[__headerLine.length];
    Arrays.fill(__defaults, "");
  }

  public String name;

  public String operation;

  public int valueMul;

  public ObjectIntOrderedMap<Junction<String>> valueAdd;

  public String type;

  public Junction<String> listensFor;

  public ObjectList<String> succRemove;

  public ObjectList<String> succPut;

  public Junction<String> removedBy;

  public String description;

  public long __code;

  public Data() {
    this(__defaults);
  }

  public Data(String name, String operation, int valueMul,
      ObjectIntOrderedMap<Junction<String>> valueAdd, String type, Junction<String> listensFor,
      ObjectList<String> succRemove, ObjectList<String> succPut, Junction<String> removedBy,
      String description, long __code) {
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

  public Data(String[] fields) {
    this.name = fields[0];
    this.operation = fields[1];
    this.valueMul = Base.BASE10.readInt(fields[2]);
    this.valueAdd = ObjectIntOrderedMap.parse(fields[3], ", ", ", ", PartialParser.DEFAULT_JUNCTION_STRING);
    this.type = fields[4];
    this.listensFor = Junction.parse(fields[5]);
    this.succRemove = ObjectList.parse(fields[6], ", ", PartialParser.DEFAULT_STRING);
    this.succPut = ObjectList.parse(fields[7], ", ", PartialParser.DEFAULT_STRING);
    this.removedBy = Junction.parse(fields[8]);
    this.description = fields[9];
    this.__code = Hasher.stringArrayHashBulk64.hash64(11111111L, fields);
  }

  public static ObjectObjectOrderedMap<String, Data> parseAll(List<String> lines) {
    if (lines == null || lines.isEmpty()) return new ObjectObjectOrderedMap<String, Data>();
    String firstLine = lines.get(0);
    String[] header = TextTools.split(firstLine, "\t");
    if (!Arrays.deepEquals(__headerLine, header)) {
      throw new IllegalArgumentException("Header lines do not match! Expected:\n" +
        TextTools.join("\t", __headerLine) + "\nbut got:\n" + firstLine);
    }
    int numLines = lines.size();
    ObjectObjectOrderedMap<String, Data> all = new ObjectObjectOrderedMap<String, Data>(numLines);
    for (int i = 1; i < numLines; i++) {
      String current = lines.get(i);
      Data parsed = new Data(TextTools.split(current, "\t"));
      all.put(parsed.key(), parsed);
    }
    return all;
  }

  public String key() {
    return name;
  }

  public long longHashCode() {
    return __code;
  }

  public int hashCode() {
    return (int)__code;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Data other = (Data) o;
    if (!Objects.equals(name, other.name)) return false;
    if (!Objects.equals(operation, other.operation)) return false;
    if (valueMul != other.valueMul) return false;
    if (!Objects.equals(valueAdd, other.valueAdd)) return false;
    if (!Objects.equals(type, other.type)) return false;
    if (!Objects.equals(listensFor, other.listensFor)) return false;
    if (!Objects.equals(succRemove, other.succRemove)) return false;
    if (!Objects.equals(succPut, other.succPut)) return false;
    if (!Objects.equals(removedBy, other.removedBy)) return false;
    if (!Objects.equals(description, other.description)) return false;
    return true;
  }
}
