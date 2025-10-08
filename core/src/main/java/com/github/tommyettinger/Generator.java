package com.github.tommyettinger;

import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.digital.TextTools;
import com.github.tommyettinger.ds.*;
import com.github.tommyettinger.ds.support.util.PartialParser;
import com.squareup.javapoet.*;
import org.jetbrains.annotations.NotNullByDefault;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Generator
{
    public String toolsPackage = "com.github.tommyettinger.ds";
    public String mapTypeString = "ObjectObjectOrderedMap";
    public String listTypeString = "ObjectList";
    public ClassName mapClass = ClassName.get(toolsPackage, mapTypeString);
    public ClassName listClass = ClassName.get(toolsPackage, listTypeString);

    public String[] headerLine;
    public String name;

    public Generator(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String filename = path.getFileName().toString();
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
//        List<String> lines = new ObjectList<>(fileText.split("\r\n|[\n-\r\u0085\u2028\u2029]"));
        String line;
        if((line = allLines.get(allLines.size() - 1)) == null || line.isEmpty())
            allLines.remove(allLines.size() - 1);
        int idx = filename.indexOf('.');
        if(idx >= 0)
            name = TextTools.safeSubstring(filename, 0, idx);
        else
            name = filename;
        headerLine = TextTools.split(allLines.get(0), "\t");
    }

    private final Modifier[] mods = {Modifier.PUBLIC};
    private final TypeName STR = TypeName.get(String.class);
    private final ParameterizedTypeName JUNC = ParameterizedTypeName.get(ClassName.get(toolsPackage, "Junction"), STR);
    private final ClassName VOI = ClassName.get(Void.class);
    public final HashMap<String, TypeName> typenames = new HashMap<>(32);
    public final HashMap<TypeName, TypeName> maps = new HashMap<>(32);
    public final HashMap<TypeName, TypeName> lists = new HashMap<>(32);
    public final HashMap<TypeName, String> defaults = new HashMap<>(32);
    public final HashMap<TypeName, String> partials = new HashMap<>(32);
    {
        typenames.put("String", STR);
        typenames.put("str", STR);
        typenames.put("s", STR);
        typenames.put("", STR);
        typenames.put("bool", TypeName.BOOLEAN);
        typenames.put("boolean", TypeName.BOOLEAN);
        typenames.put("b", TypeName.BOOLEAN);
        typenames.put("char", TypeName.CHAR);
        typenames.put("c", TypeName.CHAR);
        typenames.put("int", TypeName.INT);
        typenames.put("i", TypeName.INT);
        typenames.put("long", TypeName.LONG);
        typenames.put("l", TypeName.LONG);
        typenames.put("float", TypeName.FLOAT);
        typenames.put("f", TypeName.FLOAT);
        typenames.put("double", TypeName.DOUBLE);
        typenames.put("d", TypeName.DOUBLE);
        typenames.put("Junction", JUNC);
        typenames.put("j", JUNC);
        typenames.put("Object", TypeName.OBJECT);
        typenames.put("object", TypeName.OBJECT);
        typenames.put("o", TypeName.OBJECT);
        defaults.put(STR, "\"\"");
        defaults.put(TypeName.BOOLEAN, "false");
        defaults.put(TypeName.CHAR, "' '");
        defaults.put(TypeName.INT, "0");
        defaults.put(TypeName.LONG, "0L");
        defaults.put(TypeName.FLOAT, "0.0f");
        defaults.put(TypeName.DOUBLE, "0.0");
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.OBJECT, TypeName.FLOAT.box()),
                ParameterizedTypeName.get(ClassName.get(toolsPackage, "ObjectFloatOrderedMap"), TypeName.OBJECT));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.OBJECT, TypeName.LONG.box()),
                ParameterizedTypeName.get(ClassName.get(toolsPackage, "ObjectLongOrderedMap"), TypeName.OBJECT));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.OBJECT, TypeName.INT.box()),
                ParameterizedTypeName.get(ClassName.get(toolsPackage, "ObjectIntOrderedMap"), TypeName.OBJECT));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.LONG.box(), TypeName.OBJECT),
                ParameterizedTypeName.get(ClassName.get(toolsPackage, "LongObjectOrderedMap"), TypeName.OBJECT));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.LONG.box(), TypeName.FLOAT.box()),
                (ClassName.get(toolsPackage, "LongFloatOrderedMap")));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.LONG.box(), TypeName.LONG.box()),
                (ClassName.get(toolsPackage, "LongLongOrderedMap")));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.LONG.box(), TypeName.INT.box()),
                (ClassName.get(toolsPackage, "LongIntOrderedMap")));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.INT.box(), TypeName.OBJECT),
                ParameterizedTypeName.get(ClassName.get(toolsPackage, "IntObjectOrderedMap"), TypeName.OBJECT));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.INT.box(), TypeName.FLOAT.box()),
                (ClassName.get(toolsPackage, "IntFloatOrderedMap")));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.INT.box(), TypeName.LONG.box()),
                (ClassName.get(toolsPackage, "IntLongOrderedMap")));
        maps.put(ParameterizedTypeName.get(mapClass, TypeName.INT.box(), TypeName.INT.box()),
                (ClassName.get(toolsPackage, "IntIntOrderedMap")));
        lists.put(ParameterizedTypeName.get(listClass, TypeName.INT.box()),
                (ClassName.get(toolsPackage, "IntList")));
        lists.put(ParameterizedTypeName.get(listClass, TypeName.LONG.box()),
                (ClassName.get(toolsPackage, "LongList")));
        lists.put(ParameterizedTypeName.get(listClass, TypeName.FLOAT.box()),
                (ClassName.get(toolsPackage, "FloatList")));
        lists.put(ParameterizedTypeName.get(listClass, TypeName.DOUBLE.box()),
                (ClassName.get(toolsPackage, "DoubleList")));
        lists.put(ParameterizedTypeName.get(listClass, TypeName.CHAR.box()),
                (ClassName.get(toolsPackage, "CharList")));
        lists.put(ParameterizedTypeName.get(listClass, TypeName.BOOLEAN.box()),
                (ClassName.get(toolsPackage, "BooleanList")));
        partials.put(STR, "PartialParser.DEFAULT_STRING$L");
        partials.put(JUNC, "PartialParser.DEFAULT_JUNCTION_STRING$L");
        partials.put(ClassName.get(toolsPackage, "IntList"), "PartialParser.intCollectionParser(IntList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "LongList"), "PartialParser.intCollectionParser(LongList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "FloatList"), "PartialParser.intCollectionParser(FloatList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "DoubleList"), "PartialParser.intCollectionParser(DoubleList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "CharList"), "PartialParser.intCollectionParser(CharList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "BooleanList"), "PartialParser.intCollectionParser(BooleanList::new, $S, false))");
    }

    public String writeToString()
    {
        return write().toString();
    }

    public void writeTo(Appendable appendable)
    {
        try {
            write().writeTo(appendable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTo(File file) {
        try {
            Path p = file.toPath();
            JavaFile jf = write();
            jf.writeTo(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaFile write()
    {
        String packageName = "generated";
        TypeSpec.Builder tb = TypeSpec.classBuilder(name).addModifiers(mods).addAnnotation(NotNullByDefault.class);
        tb.addMethod(MethodSpec.constructorBuilder().addModifiers(mods).addStatement("this(__defaults)").build());
        MethodSpec.Builder make = MethodSpec.constructorBuilder().addModifiers(mods);
        MethodSpec.Builder parse = MethodSpec.constructorBuilder().addModifiers(mods);
        parse.addParameter(ArrayTypeName.of(STR), "fields");

        String section, field, tmp;
        int fieldCount = headerLine.length;
        TypeName typename, typenameExtra1, typenameExtra2;
        TypeName[] typenameFields = new TypeName[fieldCount];
        boolean[] stringFields = new boolean[fieldCount];
        ClassName[] crossFields = new ClassName[fieldCount];
        ClassName[] crossExtras = new ClassName[fieldCount];
        String[] arraySeparators = new String[fieldCount];
        String[] extraSeparators = new String[fieldCount];
        String[] fieldNames = new String[fieldCount];
        ParameterizedTypeName mappingTypename = null;
        int mappingKeyIndex = -1;
        ClassName myName = ClassName.get(packageName, name);
        String keyColumn = null;
        for (int i = 0; i < fieldCount; i++) {
            section = headerLine[i];
            if("".equals(section))
            {
                crossFields[i] = VOI;
                stringFields[i] = false;
                typenameFields[i] = VOI;
                fieldNames[i] = "";
                continue;
            }
            int caret = section.indexOf('^'), colon = section.indexOf(':'), arrayStart = section.indexOf('['),
                    mapStart = section.indexOf('{'), mapEnd = section.indexOf('}'),
                    typeLen = Math.max(arrayStart, mapStart);
            if(typeLen < 0) {
                if (caret >= 0) {
                    section = TextTools.safeSubstring(section, 0, caret);
                    tmp = section.substring(colon + 1);
                    crossFields[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                    typename = colon < 0 ? STR : typenames.getOrDefault(tmp, crossFields[i]);
                    stringFields[i] = typename.equals(STR);
                    if(stringFields[i])
                        keyColumn = colon < 0 ? section : section.substring(0, colon);
                } else {
                    if (colon < 0) {
                        crossFields[i] = VOI;
                        typename = STR;
                        stringFields[i] = true;
                        if(keyColumn == null)
                            keyColumn = section;
                    }
                    else {
                        tmp = section.substring(colon + 1);
                        crossFields[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                        typename = typenames.getOrDefault(tmp, crossFields[i]);
                        stringFields[i] = typename.equals(STR);
                        if(keyColumn == null && (stringFields[i]))
                            keyColumn = section.substring(0, colon);
                    }
                }
            }
            else if(arrayStart >= 0 && mapStart >= 0) { // map case, array values
                tmp = section.substring(colon + 1, mapStart);
                crossFields[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                typenameExtra1 = typenames.getOrDefault(tmp, crossFields[i]).box();
                tmp = section.substring(mapEnd + 1, arrayStart);
                crossExtras[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                typenameExtra2 = typenames.getOrDefault(tmp, crossExtras[i]);
                stringFields[i] = typenameExtra1.equals(STR);
                typenameExtra2 = ParameterizedTypeName.get(listClass, typenameExtra2.box());
                typenameExtra2 = lists.getOrDefault(typenameExtra2, typenameExtra2);
                typename = ParameterizedTypeName.get(mapClass, typenameExtra1, typenameExtra2);
                arraySeparators[i] = section.substring(mapStart+1, mapEnd);
                extraSeparators[i] = section.substring(arrayStart+1, section.indexOf(']'));

            }
            else if(arrayStart >= 0) {
                crossFields[i] = typenames.containsKey(tmp = section.substring(colon + 1, arrayStart)) ? VOI : ClassName.get(packageName, tmp);
                typename = typenames.getOrDefault(tmp, crossFields[i]);
                stringFields[i] = typename.equals(STR);
                typename = ParameterizedTypeName.get(listClass, typename.box());
                typename = lists.getOrDefault(typename, typename);
                arraySeparators[i] = section.substring(arrayStart+1, section.indexOf(']'));
            }
            else { // map case
                crossFields[i] = typenames.containsKey(tmp = section.substring(colon + 1, mapStart)) ? VOI : ClassName.get(packageName, tmp);
                typenameExtra1 = typenames.getOrDefault(tmp, crossFields[i]).box();
                crossExtras[i] = typenames.containsKey(tmp = section.substring(mapEnd + 1)) ? VOI : ClassName.get(packageName, tmp);
                typenameExtra2 = typenames.getOrDefault(tmp, crossExtras[i]).box();
                stringFields[i] = typenameExtra1.equals(STR);
                TypeName alternate = ParameterizedTypeName.get(mapClass,
                        (typenameExtra1.isBoxedPrimitive() ? typenameExtra1 : TypeName.OBJECT),
                        (typenameExtra2.isBoxedPrimitive() ? typenameExtra2 : TypeName.OBJECT));
                if(maps.containsKey(alternate)) {
                    TypeName tn = maps.get(alternate);
                    if (tn instanceof ParameterizedTypeName) {
//                        typename = ((ParameterizedTypeName) tn).rawType;
                        ArrayList<TypeName> extras = new ArrayList<>(2);
                        if (!typenameExtra1.isBoxedPrimitive())
                            extras.add(typenameExtra1);
                        if (!typenameExtra2.isBoxedPrimitive())
                            extras.add(typenameExtra2);
                        typename = ParameterizedTypeName.get(((ParameterizedTypeName)tn).rawType, extras.toArray(new TypeName[0]));
                    } else typename = tn;
                } else {
                    typename = ParameterizedTypeName.get(mapClass, typenameExtra1, typenameExtra2);
                }
                arraySeparators[i] = section.substring(mapStart+1, mapEnd);
            }
            typenameFields[i] = typename;
            field = TextTools.safeSubstring(section, 0, colon);
            tb.addField(typename, field, mods);
            fieldNames[i] = field;
            if(field.equals(keyColumn) && typename.equals(STR) && mappingKeyIndex < 0) {
                if (typeLen < 0) {
                    mappingTypename = ParameterizedTypeName.get(mapClass, STR, myName);
                    mappingKeyIndex = i;
                }
            }
            make.addParameter(typename, field).addStatement("this.$N = $N", field, field);

            if(STR.equals(typename))
                parse.addStatement("this.$N = fields[$L]", field, i);
            else if(JUNC.equals(typename))
                parse.addStatement("this.$N = $T.parse(fields[$L])", field, JUNC.rawType, i);
            else if(TypeName.INT.equals(typename))
                parse.addStatement("this.$N = $T.BASE10.readInt(fields[$L])", field, TypeName.get(Base.class), i);
            else if(TypeName.LONG.equals(typename))
                parse.addStatement("this.$N = $T.BASE10.readLong(fields[$L])", field, TypeName.get(Base.class), i);
            else if(TypeName.FLOAT.equals(typename))
                parse.addStatement("this.$N = $T.BASE10.readFloat(fields[$L])", field, TypeName.get(Base.class), i);
            else if(TypeName.DOUBLE.equals(typename))
                parse.addStatement("this.$N = $T.BASE10.readDouble(fields[$L])", field, TypeName.get(Base.class), i);
            else if(TypeName.BOOLEAN.equals(typename))
                parse.addStatement("this.$N = fields[$L].length() > 0 && fields[$L].charAt(0) == 't'", field, i, i);
            else if(TypeName.CHAR.equals(typename))
                parse.addStatement("this.$N = fields[$L].charAt(0)", field, i);
            else if(typename instanceof ParameterizedTypeName) {
                ParameterizedTypeName ptn = (ParameterizedTypeName)typename;
                List<TypeName> generics = ptn.typeArguments;
                ClassName raw = ptn.rawType;
                if(generics.size() == 2) {
                    // map case
                    if (extraSeparators[i] != null) {
                        // map with list values
                        parse.addStatement("this.$N = $T.parse(fields[$L], $S, $S, " + partials.get(generics.get(0)) +
                                        ", " + partials.get(generics.get(1)) + ")", field, raw, i,
                                arraySeparators[i], arraySeparators[i], "", extraSeparators[i]);
                    } else {
                        parse.addStatement("this.$N = $T.parse(fields[$L], $S, $S, " + partials.get(generics.get(0)) +
                                        ", " + partials.get(generics.get(1)) + ")", field, raw, i,
                                arraySeparators[i], arraySeparators[i], "", "");
                    }
                } else if(generics.size() == 1) {
                    // may be either map or list case
                    if(arrayStart >= 0){
                        // list case
                        parse.addStatement("this.$N = $T.parse(fields[$L], $S, " + partials.get(generics.get(0)) +
                                        ")", field, raw, i,
                                arraySeparators[i], "");
                    } else {
                        // map case with one primitive type
                        if (extraSeparators[i] != null) {
                            // map with primitive key and list value
                            parse.addStatement("this.$N = $T.parse(fields[$L], $S, $S, " +
                                            partials.get(generics.get(0)) + ")", field, raw, i,
                                    arraySeparators[i], arraySeparators[i], extraSeparators[i]);
                        } else {
                            // map with primitive key and scalar value or object key and primitive value
                            parse.addStatement("this.$N = $T.parse(fields[$L], $S, $S, " +
                                            partials.get(generics.get(0)) + ")", field, raw, i,
                                    arraySeparators[i], arraySeparators[i], "");
                        }
                    }
                } else {
                    // may be either a primitive-primitive map or primitive list
                    if(arrayStart >= 0) {
                        // list case
                        parse.addStatement("this.$N = $T.parse(fields[$L], $S)", field, raw, i,
                                arraySeparators[i]);
                    } else {
                        // map case
                        parse.addStatement("this.$N = $T.parse(fields[$L], $S, $S)", field, raw, i,
                                arraySeparators[i], arraySeparators[i]);
                    }
                }
            }
        }
        tb.addField(TypeName.LONG, "__code", Modifier.PUBLIC);
        tb.addField(ArrayTypeName.of(STR), "__headerLine", Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL);
        tb.addField(ArrayTypeName.of(STR), "__defaults", Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL);

        tb.addStaticBlock(CodeBlock.builder()
                .addStatement("__headerLine = new String[]{$L}", stringLiterals(headerLine))
                .addStatement("__defaults = new String[__headerLine.length]")
                .addStatement("$T.fill(__defaults, \"\")", Arrays.class)
                .build());
        make.addParameter(TypeName.LONG, "__code").addStatement("this.__code = __code");
        parse.addStatement("this.__code = Hasher.stringArrayHashBulk64.hash64(11111111L, fields)");
        tb.addMethod(make.build());
        tb.addMethod(parse.build());
        MethodSpec parseAll = MethodSpec.methodBuilder("parseAll").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(mappingTypename).addParameter(ParameterizedTypeName.get(List.class, String.class), "lines").addCode(
                "if (lines == null || lines.isEmpty()) return new $1T();\n" +
                "String firstLine = lines.get(0);\n" +
                "String[] header = $3T.split(firstLine, \"\\t\");\n" +
                "if (!$4T.deepEquals(__headerLine, header)) {\n" +
                "$>throw new IllegalArgumentException(\"Header lines do not match! Expected:\\n\" +\n" +
                "$>$3T.join(\"\\t\", __headerLine) + \"\\nbut got:\\n\" + firstLine);\n" +
                "$<$<}\n" +
                "int numLines = lines.size();\n" +
                "$1T all = new $1T(numLines);\n" +
                "for (int i = 1; i < numLines; i++) {\n" +
                "$>String current = lines.get(i);\n" +
                "$2T parsed = new $2T($3T.split(current, \"\\t\"));\n" +
                "all.put(parsed.key(), parsed);\n" +
                "$<}\n" +
                "return all;\n",
                mappingTypename, myName, ClassName.get(TextTools.class), ClassName.get(Arrays.class)).build();
        tb.addMethod(parseAll);

        ClassName cn = ClassName.get(packageName, name);
        tb.addMethod(MethodSpec.methodBuilder("key").addModifiers(Modifier.PUBLIC).returns(STR).addStatement("return $N", keyColumn).build());

        makeHashCode(tb);
        makeEquals(tb, cn, fieldNames, typenameFields);

        return JavaFile.builder(packageName, tb.build()).skipJavaLangImports(true)
                .addFileComment("Generated code produced by taboratory.")
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(PartialParser.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(Junction.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(Hasher.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(IntList.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(LongList.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(FloatList.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(DoubleList.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(CharList.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(BooleanList.class))
                .addFileComment("\nThis is here to generate an import statement for $T .", TypeName.get(ObjectList.class))
                .build();
    }

    private void makeHashCode(TypeSpec.Builder tb)
    {
        tb.addMethod(MethodSpec.methodBuilder("longHashCode").addModifiers(Modifier.PUBLIC).returns(TypeName.LONG).addStatement("return __code").build());
        tb.addMethod(MethodSpec.methodBuilder("hashCode").addModifiers(mods).returns(TypeName.INT).addStatement("return (int)__code").build());
    }
    private void makeEquals(TypeSpec.Builder tb, ClassName cn, String[] fieldNames, TypeName[] fieldTypes)
    {
        TypeName tn, objects = TypeName.get(Objects.class);

        MethodSpec.Builder mb = MethodSpec.methodBuilder("equals").addModifiers(Modifier.PUBLIC).returns(TypeName.BOOLEAN).addParameter(TypeName.OBJECT, "o");
        mb.addCode("if (this == o) return true;\n" +
                "if (o == null || getClass() != o.getClass()) return false;\n" +
                "$T other = ($T) o;\n", cn, cn);
        int len = Math.min(fieldNames.length, fieldTypes.length);
        String fn;
        for (int i = 0; i < len; i++) {
            fn = fieldNames[i];
            tn = fieldTypes[i];
            if(fn == null || fn.isEmpty()) continue;
            if(tn.isPrimitive()) {
                mb.addStatement("if ($N != other.$N) return false", fn, fn);
            }
            else {
                mb.addStatement("if (!$T.equals($N, other.$N)) return false", objects, fn, fn);
            }
        }
        mb.addStatement("return true");
        tb.addMethod(mb.build());
    }

    private static String characterLiteral(char c) {
        // see https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.10.6
        switch (c) {
            case '\b': return "\\b";  /* \\u0008: backspace (BS) */
            case '\t': return "\\t";  /* \\u0009: horizontal tab (HT) */
            case '\n': return "\\n";  /* \\u000a: linefeed (LF) */
            case '\f': return "\\f";  /* \\u000c: form feed (FF) */
            case '\r': return "\\r";  /* \\u000d: carriage return (CR) */
            case '\"': return "\"";   /* \\u0022: double quote (") */
            case '\'': return "\\'";  /* \\u0027: single quote (') */
            case '\\': return "\\\\"; /* \\u005c: backslash (\) */
            default:
                return Character.isISOControl(c) ? String.format("\\u%04x", (int) c) : Character.toString(c);
        }
    }

    /**
     * Returns the string literals, separated by ", " representing {@code values}, including wrapping double quotes.
     * From CodePoet source (com.squareup.javapoet.Util), with small changes.
     * @param values the values to escape as a String
     * @return the string literals representing {@code values}, including wrapping double quotes and comma separators.
     */
    private String stringLiterals(String... values) {
        StringBuilder result = new StringBuilder(values.length * 8);
        for (int s = 0; s < values.length;) {
            String value = values[s];
            result.append('"');
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                // trivial case: single quote must not be escaped
                if (c == '\'') {
                    result.append("'");
                    continue;
                }
                // trivial case: double quotes must be escaped
                if (c == '\"') {
                    result.append("\\\"");
                    continue;
                }
                // default case: just let character literal do its work
                result.append(characterLiteral(c));
            }
            if(++s < values.length)
                result.append("\", ");
            else
                result.append('"');
        }
        return result.toString();
    }

}
