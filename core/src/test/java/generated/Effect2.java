// Generated code produced by taboratory.
// PartialParser (This is here to generate an import statement.)
// Junction (This is here to generate an import statement.)
// Hasher (This is here to generate an import statement.)
// IntList (This is here to generate an import statement.)
// LongList (This is here to generate an import statement.)
// FloatList (This is here to generate an import statement.)
// DoubleList (This is here to generate an import statement.)
// CharList (This is here to generate an import statement.)
// BooleanList (This is here to generate an import statement.)
// ObjectList (This is here to generate an import statement.)
package generated;

import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.ds.BooleanList;
import com.github.tommyettinger.ds.CharList;
import com.github.tommyettinger.ds.DoubleList;
import com.github.tommyettinger.ds.FloatList;
import com.github.tommyettinger.ds.IntList;
import com.github.tommyettinger.ds.Junction;
import com.github.tommyettinger.ds.LongList;
import com.github.tommyettinger.ds.ObjectIntOrderedMap;
import com.github.tommyettinger.ds.ObjectList;
import com.github.tommyettinger.ds.support.util.PartialParser;
import java.util.Objects;

public class Effect2 {
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

    public Effect2() {
    }

    public Effect2(String name, String operation, int valueMul,
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

    public Effect2(String[] fields) {
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
        Effect2 other = (Effect2) o;
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
