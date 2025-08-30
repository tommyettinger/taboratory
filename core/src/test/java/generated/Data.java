package generated;

import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.digital.TextTools;
import com.github.tommyettinger.ds.Junction;
import com.github.tommyettinger.ds.ObjectIntOrderedMap;
import com.github.tommyettinger.ds.ObjectList;
import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import com.github.tommyettinger.ds.support.util.PartialParser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Data {
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

  public ObjectList<String> succRemove;

  public ObjectList<String> succPut;

  public Junction<String> removedBy;

  public String description;

  private long __code;

  public Data() {
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

    /**
     * Not actually generated code at the moment!
     * @param line a single line of tab-separated fields to be parsed by fixed logic generated at class generation
     * @return a new Data item parsed from line
     */
  public static Data parse(String line) {
      String[] split = TextTools.split(line, "\t");
      String p0 = split[0];
      String p1 = split[1];
      int p2 = Base.BASE10.readInt(split[2]);
      ObjectIntOrderedMap<Junction<String>> p3 = new ObjectIntOrderedMap<>();
      p3.putLegible(split[3], ", ", ", ", PartialParser.DEFAULT_JUNCTION_STRING);
      String p4 = split[4];
      Junction<String> p5 = Junction.parse(split[5]);
      ObjectList<String> p6 = new ObjectList<>();
      p6.addLegible(split[6], ", ", PartialParser.DEFAULT_STRING);
      ObjectList<String> p7 = new ObjectList<>();
      p6.addLegible(split[7], ", ", PartialParser.DEFAULT_STRING);
      Junction<String> p8 = Junction.parse(split[8]);
      String p9 = split[9];
      long hash = Hasher.tau.hashBulk64(line);
      return new Data(p0, p1, p2, p3, p4, p5, p6, p7, p8, p9, hash);
  }

    public static ObjectObjectOrderedMap<String, Data> parseAll(List<String> lines) {
        if (lines == null || lines.isEmpty()) return new ObjectObjectOrderedMap<>();

        String firstLine = lines.get(0);
        String[] header = TextTools.split(firstLine, "\t");
        if (!Arrays.deepEquals(__headerLine, header))
            throw new IllegalArgumentException("Header lines do not match! Expected:\n" +
                    TextTools.join("\t", __headerLine) + "\nbut got:\n" + firstLine);

        int numLines = lines.size();
        ObjectObjectOrderedMap<String, Data> all = new ObjectObjectOrderedMap<>(numLines);
        for (int i = 1; i < numLines; i++) {
            String current = lines.get(i);
            Data parsed = parse(current);
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
