package com.github.tommyettinger;

import com.github.tommyettinger.digital.Base;
import com.github.tommyettinger.digital.TextTools;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CodeWriter
{
    public String toolsPackage = "com.github.tommyettinger.ds";
    public String mapTypeString = "ObjectObjectOrderedMap";
    public String listTypeString = "ObjectList";
    public ClassName mapClass = ClassName.get(toolsPackage, mapTypeString);
    public ClassName listClass = ClassName.get(toolsPackage, listTypeString);

    public String[] headerLine = null;
//    public String[][] contentLines;
    public String name;

    public CodeWriter(String filePath) throws IOException {
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
//        contentLines = new String[allLines.size() - 1][];
//        for (int i = 0; i < contentLines.length; i++) {
//            contentLines[i] = readLine(allLines.get(i+1), headerLine);
//        }
    }
    public static String[] readLine(String dataLine, String[] headerLine) {
        if(dataLine == null || headerLine == null || headerLine.length == 0) return new String[0];
        int idx = -1;
        String[] result = new String[headerLine.length];
        for (int j = 0; j < headerLine.length - 1; j++) {
            if ("".equals(headerLine[j])) {
                result[j] = "";
                idx = dataLine.indexOf('\t', idx + 1);
            } else {
                result[j] = TextTools.safeSubstring(dataLine, idx + 1, idx = dataLine.indexOf('\t', idx + 1));
            }
        }
        if ("".equals(headerLine[headerLine.length - 1])) {
            result[headerLine.length - 1] = "";
        } else {
            result[headerLine.length - 1] = dataLine.substring(idx + 1);
        }
        return result;
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
        partials.put(STR, "com.github.tommyettinger.ds.support.util.PartialParser.DEFAULT_STRING$L");
        partials.put(JUNC, "com.github.tommyettinger.ds.support.util.PartialParser.DEFAULT_JUNCTION_STRING$L");
        partials.put(ClassName.get(toolsPackage, "IntList"), "com.github.tommyettinger.ds.support.util.PartialParser.intCollectionParser(IntList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "LongList"), "com.github.tommyettinger.ds.support.util.PartialParser.intCollectionParser(LongList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "FloatList"), "com.github.tommyettinger.ds.support.util.PartialParser.intCollectionParser(FloatList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "DoubleList"), "com.github.tommyettinger.ds.support.util.PartialParser.intCollectionParser(DoubleList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "CharList"), "com.github.tommyettinger.ds.support.util.PartialParser.intCollectionParser(CharList::new, $S, false))");
        partials.put(ClassName.get(toolsPackage, "BooleanList"), "com.github.tommyettinger.ds.support.util.PartialParser.intCollectionParser(BooleanList::new, $S, false))");
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

    public TypeName extract(TypeName tn) {
        return (tn instanceof ParameterizedTypeName) ? ((ParameterizedTypeName)tn).rawType : tn;
    }

    public JavaFile write()
    {
        String packageName = "generated";
        TypeSpec.Builder tb = TypeSpec.classBuilder(name).addModifiers(mods);
        tb.addMethod(MethodSpec.constructorBuilder().addModifiers(mods).build());
        MethodSpec.Builder make = MethodSpec.constructorBuilder().addModifiers(mods);
        MethodSpec.Builder parse = MethodSpec.constructorBuilder().addModifiers(mods);
        parse.addParameter(ArrayTypeName.of(STR), "fields");
//        ClassName tlt = ClassName.get(toolsPackage, toolsClass);
        String section, field, tmp;
        int fieldCount = headerLine.length;
//        // The plan is to compare the header String[] against the given headerLine at runtime, as a verification.
//        long headerCode = Hasher.charSequenceArrayHashBulk64.hash64(Hasher.C, headerLine);
        TypeName typename, typenameExtra1 = null, typenameExtra2 = null;
        TypeName[] typenameFields = new TypeName[fieldCount];
        TypeName[] typenameExtras1 = new TypeName[fieldCount];
        TypeName[] typenameExtras2 = new TypeName[fieldCount];
        boolean[] stringFields = new boolean[fieldCount];
        boolean[] stringExtras = new boolean[fieldCount];
        boolean[] junctionFields = new boolean[fieldCount];
        boolean[] junctionExtras = new boolean[fieldCount];
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
                    headerLine[i] = section;
                    tmp = section.substring(colon + 1);
                    crossFields[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                    typename = colon < 0 ? STR : typenames.getOrDefault(tmp, crossFields[i]);
                    stringFields[i] = typename.equals(STR);
                    if(stringFields[i])
                        keyColumn = colon < 0 ? section : section.substring(0, colon);
                    junctionFields[i] = typename.equals(JUNC);
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
                        junctionFields[i] = typename.equals(JUNC);
                    }
                }
            }
            else if(arrayStart >= 0 && mapStart >= 0) { // map case, array values
                tmp = section.substring(colon + 1, mapStart);
                crossFields[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                typenameExtra1 = typenames.getOrDefault(tmp, crossFields[i]).box();
                typenameExtras1[i] = typenameExtra1;
                tmp = section.substring(mapEnd + 1, arrayStart);
                crossExtras[i] = typenames.containsKey(tmp) ? VOI : ClassName.get(packageName, tmp);
                typenameExtra2 = typenames.getOrDefault(tmp, crossExtras[i]);
                stringFields[i] = typenameExtra1.equals(STR);
                stringExtras[i] = typenameExtra2.equals(STR);
                junctionFields[i] = typenameExtra1.equals(JUNC);
                junctionExtras[i] = typenameExtra2.equals(JUNC);
                typenameExtra2 = ParameterizedTypeName.get(listClass, typenameExtra2.box());
                typenameExtra2 = lists.getOrDefault(typenameExtra2, typenameExtra2);
                typenameExtras2[i] = typenameExtra2;
                typename = ParameterizedTypeName.get(mapClass, typenameExtra1, typenameExtra2);
                arraySeparators[i] = section.substring(mapStart+1, mapEnd);
                extraSeparators[i] = section.substring(arrayStart+1, section.indexOf(']'));

            }
            else if(arrayStart >= 0) {
                crossFields[i] = typenames.containsKey(tmp = section.substring(colon + 1, arrayStart)) ? VOI : ClassName.get(packageName, tmp);
                typename = typenames.getOrDefault(tmp, crossFields[i]);
                stringFields[i] = typename.equals(STR);
                junctionFields[i] = typename.equals(JUNC);
                typename = ParameterizedTypeName.get(listClass, typename.box());
                typename = lists.getOrDefault(typename, typename);
                arraySeparators[i] = section.substring(arrayStart+1, section.indexOf(']'));
            }
            else { // map case
                crossFields[i] = typenames.containsKey(tmp = section.substring(colon + 1, mapStart)) ? VOI : ClassName.get(packageName, tmp);
                typenameExtras1[i] = typenameExtra1 = typenames.getOrDefault(tmp, crossFields[i]).box();
                crossExtras[i] = typenames.containsKey(tmp = section.substring(mapEnd + 1)) ? VOI : ClassName.get(packageName, tmp);
                typenameExtras2[i] = typenameExtra2 = typenames.getOrDefault(tmp, crossExtras[i]).box();
                stringFields[i] = typenameExtra1.equals(STR);
                stringExtras[i] = typenameExtra2.equals(STR);
                junctionFields[i] = typenameExtra1.equals(JUNC);
                junctionExtras[i] = typenameExtra2.equals(JUNC);
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
        tb.addField(TypeName.LONG, "__code", Modifier.PRIVATE);
        tb.addField(ArrayTypeName.of(STR), "__headerLine", Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL).addStaticBlock(CodeBlock.builder().addStatement("__headerLine = new String[]{$L}", stringLiterals(headerLine)).build());
        make.addParameter(TypeName.LONG, "__code").addStatement("this.__code = __code");
        parse.addStatement("this.__code = com.github.tommyettinger.digital.Hasher.stringArrayHashBulk64.hash64(11111111L, fields)");
        tb.addMethod(make.build());
        tb.addMethod(parse.build());
        ClassName cn = ClassName.get(packageName, name);
        tb.addMethod(MethodSpec.methodBuilder("key").addModifiers(Modifier.PUBLIC).returns(STR).addStatement("return $N", keyColumn).build());

        makeHashCode(tb);
//        makeHashCode(tb, fieldNames, typenameFields);
        makeEquals(tb, cn, fieldNames, typenameFields);
//        if(contentLines.length > 0) {
//            ArrayTypeName atn = ArrayTypeName.of(cn);
//            CodeBlock.Builder cbb = CodeBlock.builder();
//            cbb.beginControlFlow("new $T", atn);
//            for (int i = 0; i < contentLines.length; i++) {
//                cbb.add("new $T(", cn);
//                int j = 0;
//                for (; j < fieldCount; j++) {
//                    if(VOI.equals(typenameFields[j]))
//                        continue;
//                    if (extraSeparators[j] != null) { // a map with array values
//                        if (!contentLines[i][j].contains(arraySeparators[j]))
//                        {
//                            if(typenameExtras1[j].isBoxedPrimitive() && typenameExtras2[j].isBoxedPrimitive())
//                                cbb.add("$T.$L()", extract(typenameFields[j]), typenameExtras1[j], typenameExtras2[j], makeMethod);
//                            else if(typenameExtras1[j].isBoxedPrimitive())
//                                cbb.add("$T.<$T>$L()", extract(typenameFields[j]), typenameExtras2[j], makeMethod);
//                            else if(typenameExtras2[j].isBoxedPrimitive())
//                                cbb.add("$T.<$T>$L()", extract(typenameFields[j]), typenameExtras1[j], makeMethod);
//                            else
//                                cbb.add("$T.<$T, $T>$L()", extract(typenameFields[j]), typenameExtras1[j], typenameExtras2[j], makeMethod);
//                        }
//                        else
//                            cbb.add("$T.$L($L)", extract(typenameFields[j]), makeMethod,
//                                    stringMapArrayLiterals((stringFields[j] ? 0 : -1), crossFields[j], crossExtras[j], 80,
//                                            contentLines[i][j], arraySeparators[j], extraSeparators[j], typenameExtras2[j]));
//                    } else if (arraySeparators[j] != null) {
//                        if (typenameExtras1[j] != null) {
//                            if (!contentLines[i][j].contains(arraySeparators[j])) {
//                                if(typenameExtras1[j].isBoxedPrimitive() && typenameExtras2[j].isBoxedPrimitive())
//                                    cbb.add("$T.$L()", extract(typenameFields[j]), typenameExtras1[j], typenameExtras2[j], makeMethod);
//                                else if(typenameExtras1[j].isBoxedPrimitive())
//                                    cbb.add("$T.<$T>$L()", extract(typenameFields[j]), typenameExtras2[j], makeMethod);
//                                else if(typenameExtras2[j].isBoxedPrimitive())
//                                    cbb.add("$T.<$T>$L()", extract(typenameFields[j]), typenameExtras1[j], makeMethod);
//                                else
//                                    cbb.add("$T.<$T, $T>$L()", extract(typenameFields[j]), typenameExtras1[j], typenameExtras2[j], makeMethod);
//
//                            } else {
//                                cbb.add("$T.$L($L)", extract(typenameFields[j]), makeMethod,
//                                        stringLiterals((stringFields[j] ? 1 : 0) + (stringExtras[j] ? 2 : 0)
//                                                        + (junctionFields[j] ? 4 : 0) + (junctionExtras[j] ? 8 : 0) - 1, crossFields[j], crossExtras[j], 80,
//                                                TextTools.split(contentLines[i][j], arraySeparators[j])));
//                            }
//                        } else {
//                            cbb.add("new $T {$L}", typenameFields[j],
//                                    stringLiterals((stringFields[j] ? 2 : -1), crossFields[j], null,
//                                            80, TextTools.split(contentLines[i][j], arraySeparators[j])));
//                        }
//                    } else if (junctionFields[j] || junctionExtras[j]) {
//                        cbb.add("$T.parse($L)", JUNC.rawType, stringLiteral(contentLines[i][j], crossFields[j]));
//                    } else if (stringFields[j] || stringExtras[j] || !VOI.equals(crossFields[j])) {
//                        cbb.add("$L", stringLiteral(contentLines[i][j], crossFields[j]));
//                    } else {
//                        cbb.add("$L", contentLines[i][j].isEmpty()
//                                ? Objects.toString(defaults.get(typenameFields[j]))
//                                : bareLiteral(contentLines[i][j], typenameFields[j]));
//                    }
////                    if (j < fieldCount - 1)
//                    cbb.add(", ");
//                }
//
//                cbb.add("$LL", Hasher.hashBulk64(Hasher.hashBulk64(Hasher.C, name), contentLines[i]));
//
//                cbb.add("),\n");
//
//            }
//            cbb.unindent();
//            cbb.add("}");
//
////            tb.addField(FieldSpec.builder(atn, "ENTRIES", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).initializer(cbb.build()).build());
//            if (mappingKeyIndex >= 0) {
//                CodeBlock.Builder cbb2 = CodeBlock.builder();
//                String[] keyStuff = new String[contentLines.length];
//                for (int i = 0; i < contentLines.length; i++) {
//                    keyStuff[i] = contentLines[i][mappingKeyIndex];
//                }
//                cbb2.add("new $T(\nnew String[]{$L},\n$L)", mappingTypename, stringLiterals(keyStuff), cbb.build()); // alternationCode: (stringFields[mappingKeyIndex] ? 0 : -1)
//                tb.addField(FieldSpec.builder(mappingTypename, "MAPPING", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL).initializer(cbb2.build()).build());
//                MethodSpec.Builder mb = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(myName).addParameter(STR, "item").addCode("return MAPPING.get($N);\n", "item");
//                tb.addMethod(mb.build());
//            }
//        }
        return JavaFile.builder(packageName, tb.build()).skipJavaLangImports(true).build();
    }

    private String bareLiteral(String s, TypeName type) {
        if(TypeName.CHAR.equals(type))
            return "'" + s + "'";
        if(TypeName.LONG.equals(type))
            return s + "L";
        if(TypeName.FLOAT.equals(type))
            return s + "f";
        return s;

    }

    private void makeHashCode(TypeSpec.Builder tb)
    {
        tb.addMethod(MethodSpec.methodBuilder("longHashCode").addModifiers(Modifier.PUBLIC).returns(TypeName.LONG).addStatement("return __code").build());
        tb.addMethod(MethodSpec.methodBuilder("hashCode").addModifiers(mods).returns(TypeName.INT).addStatement("return (int)__code").build());
    }
    private void makeEquals(TypeSpec.Builder tb, ClassName cn, String[] fieldNames, TypeName[] fieldTypes)
    {
        TypeName tn, arrays = TypeName.get(Arrays.class), objects = TypeName.get(Objects.class);
//        tb.addMethod(MethodSpec.methodBuilder("stringArrayEquals").addModifiers(Modifier.PRIVATE, Modifier.STATIC).returns(TypeName.BOOLEAN).addParameter(ArrayTypeName.of(STR), "left").addParameter(ArrayTypeName.of(STR), "right")
//                .addCode("if (left == right) return true;\n" +
//                        "if (left == null || right == null) return false;\n" +
//                        "final int len = left.length;\n" +
//                        "if(len != right.length) return false;\n" +
//                        "for (int i = 0; i < len; i++) { if(!$T.equals(left[i], right[i])) return false; }\n" +
//                        "return true;\n", objects).build());

        MethodSpec.Builder mb = MethodSpec.methodBuilder("equals").addModifiers(Modifier.PUBLIC).returns(TypeName.BOOLEAN).addParameter(TypeName.OBJECT, "o");
        mb.addCode("if (this == o) return true;\nif (o == null || getClass() != o.getClass()) return false;\n$T other = ($T) o;\n", cn, cn);
        int len = Math.min(fieldNames.length, fieldTypes.length);
        String fn;
        for (int i = 0; i < len; i++) {
            fn = fieldNames[i];
            tn = fieldTypes[i];
            if(fn == null || fn.isEmpty()) continue;
            if(tn.isPrimitive())
            {
                mb.addStatement("if ($N != other.$N) return false", fn, fn);
            }
            else if(tn instanceof ArrayTypeName)
            {
                tn = ((ArrayTypeName)tn).componentType;
                if(tn.isPrimitive()) {
                    mb.addStatement("if(!$T.equals($N, other.$N)) return false", arrays, fn, fn);
                }
//                else if(tn.equals(STR))
//                {
//                    mb.addStatement("if(!stringArrayEquals($N, other.$N)) return false", fn, fn);
//                }
                else
                {
                    mb.addStatement("if(!$T.deepEquals($N, other.$N)) return false", arrays, fn, fn);
                }
            }
            else
            {
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
     * Returns the string literal representing {@code value}, including wrapping double quotes.
     * From CodePoet source (com.squareup.javapoet.Util), with small changes.
     * @param value the value to escape as a String
     * @return the string literal representing {@code value}, including wrapping double quotes.
     */
    private String stringLiteral(String value, ClassName cross1) {
        StringBuilder result = new StringBuilder(value.length() + 2);
        if(!VOI.equals(cross1) && !STR.equals(cross1)) {
            result.append(cross1.simpleName()).append(".get(\"").append(value).append("\")");
        }
        else {
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
            result.append('"');
        }
        return result.toString();
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
    /**
     * Returns the string literals, separated by ", " representing {@code values}, including wrapping double quotes.
     * From CodePoet source (com.squareup.javapoet.Util), with small changes.
     * @param values the values to escape as a String
     * @return the string literals representing {@code values}, including wrapping double quotes and comma separators.
     */
    private String stringLiterals(int alternationCode, String... values) {
        StringBuilder result = new StringBuilder(values.length * 8);
        for (int s = 0; s < values.length;) {
            if(alternationCode >= 2 || (s & 1) == alternationCode) {
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
                if (++s < values.length)
                    result.append("\", ");
                else
                    result.append('"');
            }
            else
            {
                if (s + 1 < values.length)
                    result.append(values[s++]).append(", ");
                else
                    result.append(values[s++]);
            }
        }
        return result.toString();
    }
    /**
     * Returns the string literals, separated by ", " representing {@code values}, including wrapping double quotes.
     * From CodePoet source (com.squareup.javapoet.Util), with small changes.
     * @param values the values to escape as a String
     * @return the string literal representing {@code value}, including wrapping double quotes and comma separators.
     */
    private String stringLiterals(int alternationCode, ClassName cross1, ClassName cross2, int lineLength, String... values) {
        StringBuilder result = new StringBuilder(values.length * 8),
                work = new StringBuilder(40 + lineLength);
        int latestBreak = 0;
        String value;
        for (int s = 0; s < values.length;) {
            value = values[s];
            if(value == null || value.isEmpty()) {
                ++s;
                continue;
            }
            if((cross2 == null || (s & 1) == 0) && !VOI.equals(cross1)) {
                work.setLength(0);
                work.append(cross1.simpleName()).append(".get(\"").append(value).append("\")");
                if (++s < values.length) {
                    work.append(",");
                    if(result.length() + work.length() + 1 - latestBreak < lineLength)
                        result.append(work).append(' ');
                    else
                    {
                        latestBreak = result.length();
                        result.append(work).append('\n');
                    }
                } else {
                    result.append(work);
                }
            }
            else if((s & 1) == 1 && cross2 != null && !VOI.equals(cross2)){
                work.setLength(0);
                work.append(cross2.packageName()).append('.').append(cross2.simpleName()).append(".get(\"").append(value).append("\")");
                if (++s < values.length) {
                    work.append(",");
                    if(result.length() + work.length() + 1 - latestBreak < lineLength)
                        result.append(work).append(' ');
                    else
                    {
                        latestBreak = result.length();
                        result.append(work).append('\n');
                    }
                } else {
                    result.append(work);
                }
            }
            else if(alternationCode == 2 || (s & 1) == ((alternationCode + 1 >>> 1) & 1)) {
                // if both are Strings or the current alternation is a String
                work.setLength(0);
                work.append('"');
                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);
                    // trivial case: single quote must not be escaped
                    if (c == '\'') {
                        work.append("'");
                        continue;
                    }
                    // trivial case: double quotes must be escaped
                    if (c == '\"') {
                        work.append("\\\"");
                        continue;
                    }
                    // default case: just let character literal do its work
                    work.append(characterLiteral(c));
                }
                if (++s < values.length) {
                    work.append("\",");
                    if(result.length() + work.length() + 1 - latestBreak < lineLength)
                        result.append(work).append(' ');
                    else
                    {
                        latestBreak = result.length();
                        result.append(work).append('\n');
                    }
                } else {
                    result.append(work).append('"');
                }
            }
            else if(alternationCode == 11 || (s & 1) == (alternationCode + 1 >>> 3)) {
                // both are junctions or the current alternation is a junction
                if (++s < values.length) {
                    if (result.length() + value.length() + 2 - latestBreak < lineLength)
                        result.append("Junction.parse(\"").append(value).append("\"), ");
                    else if (result.length() + value.length() + 1 - latestBreak < lineLength)
                    {
                        result.append("Junction.parse(\"").append(value).append("\"),");
                        latestBreak = result.length();
                        result.append('\n');
                    }
                    else {
                        latestBreak = result.length();
                        result.append("Junction.parse(\"").append(value).append("\"),\n");
                    }
                } else {
                    result.append(value);
                }
            }
            else {
                if (++s < values.length) {
                    if (result.length() + value.length() + 2 - latestBreak < lineLength)
                        result.append(value).append(", ");
                    else if (result.length() + value.length() + 1 - latestBreak < lineLength)
                    {
                        result.append(value).append(',');
                        latestBreak = result.length();
                        result.append('\n');
                    }
                    else {
                        latestBreak = result.length();
                        result.append(value).append(",\n");
                    }
                } else {
                    result.append(value);
                }
            }
        }
        return result.toString();
    }

    private String stringMapArrayLiterals(int alternationCode, ClassName cross1, ClassName cross2, int lineLength,
                                          String content, String majorSeparator, String minorSeparator, TypeName valueType) {
        String[] values = TextTools.split(content, majorSeparator);
        StringBuilder result = new StringBuilder(values.length * 8),
                work = new StringBuilder(40 + lineLength);
        int latestBreak = 0;
        String value;
        for (int s = 0; s < values.length;) {
            value = values[s];
            if(value == null || value.isEmpty()) {
                ++s;
                continue;
            }
            if((cross2 == null || (s & 1) == 0) && !VOI.equals(cross1)) {
                work.setLength(0);
                work.append(cross1.simpleName()).append(".get(\"").append(value).append("\")");
                if (++s < values.length) {
                    work.append(",");
                    if(result.length() + work.length() + 1 - latestBreak < lineLength)
                        result.append(work).append(' ');
                    else
                    {
                        latestBreak = result.length();
                        result.append(work).append('\n');
                    }
                } else {
                    result.append(work);
                }
            }
            else if((s & 1) == 1 && cross2 != null && !VOI.equals(cross2)){
                work.setLength(0);
                work.append(cross2.packageName()).append('.').append(cross2.simpleName()).append(".get(\"").append(value).append("\")");
                if (++s < values.length) {
                    work.append(",");
                    if(result.length() + work.length() + 1 - latestBreak < lineLength)
                        result.append(work).append(' ');
                    else
                    {
                        latestBreak = result.length();
                        result.append(work).append('\n');
                    }
                } else {
                    result.append(work);
                }
            }
            else if(alternationCode == 0 && (s & 1) == alternationCode) {
                work.setLength(0);
                work.append('"');
                for (int i = 0; i < value.length(); i++) {
                    char c = value.charAt(i);
                    // trivial case: single quote must not be escaped
                    if (c == '\'') {
                        work.append("'");
                        continue;
                    }
                    // trivial case: double quotes must be escaped
                    if (c == '\"') {
                        work.append("\\\"");
                        continue;
                    }
                    // default case: just let character literal do its work
                    work.append(characterLiteral(c));
                }
                if (++s < values.length) {
                    work.append("\",");
                    if(result.length() + work.length() + 1 - latestBreak < lineLength)
                        result.append(work).append(' ');
                    else
                    {
                        latestBreak = result.length();
                        result.append(work).append('\n');
                    }
                } else {
                    result.append(work).append('"');
                }
            }
            else
            {
                if((s & 1) == 1)
                    value = CodeBlock.of("new $T {$L}", valueType,
                            stringLiterals(alternationCode >= 1 ? 2 : -1, cross2, null, 80,
                                    TextTools.split(value, minorSeparator))).toString();
                if (++s < values.length) {
                    if (result.length() + value.length() + 2 - latestBreak < lineLength)
                        result.append(value).append(", ");
                    else if (result.length() + value.length() + 1 - latestBreak < lineLength)
                    {
                        result.append(value).append(',');
                        latestBreak = result.length();
                        result.append('\n');
                    }
                    else {
                        latestBreak = result.length();
                        result.append(value).append(",\n");
                    }
                } else {
                    result.append(value);
                }
            }
        }
        return result.toString();
    }

    /**
     * Makes a LinkedHashMap (LHM) with key and value types inferred from the types of k0 and v0, and considers all
     * parameters key-value pairs, casting the Objects at positions 0, 2, 4... etc. to K and the objects at positions
     * 1, 3, 5... etc. to V. If rest has an odd-number length, then it discards the last item. If any pair of items in
     * rest cannot be cast to the correct type of K or V, then this inserts nothing for that pair.
     * @param k0 the first key; used to infer the types of other keys if generic parameters aren't specified.
     * @param v0 the first value; used to infer the types of other values if generic parameters aren't specified.
     * @param rest an array or vararg of keys and values in pairs; should contain alternating K, V, K, V... elements
     * @param <K> the type of keys in the returned LinkedHashMap; if not specified, will be inferred from k0
     * @param <V> the type of values in the returned LinkedHashMap; if not specified, will be inferred from v0
     * @return a freshly-made LinkedHashMap with K keys and V values, using k0, v0, and the contents of rest to fill it
     */
    @SuppressWarnings("unchecked")
    public static <K, V> LinkedHashMap<K, V> makeMap(K k0, V v0, Object... rest)
    {
        if(rest == null || rest.length == 0)
        {
            LinkedHashMap<K, V> lhm = new LinkedHashMap<>(2);
            lhm.put(k0, v0);
            return lhm;
        }
        LinkedHashMap<K, V> lhm = new LinkedHashMap<>(1 + (rest.length / 2));
        lhm.put(k0, v0);

        for (int i = 0; i < rest.length - 1; i+=2) {
            try {
                lhm.put((K) rest[i], (V) rest[i + 1]);
            }catch (ClassCastException ignored) {
            }
        }
        return lhm;
    }
}
