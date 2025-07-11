package com.github.tommyettinger;

import com.github.tommyettinger.digital.Hasher;
import com.github.tommyettinger.digital.TextTools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TSVReader {
    public String[] headerLine;
    public String[][] contentLines;
    public String name;
    public String keyColumn;
    public TSVReader()
    {
    }
    public void read(String filename, String text)
    {
        read(filename, Arrays.asList(text.split("\\v+")));
   }
    public void read(String filename, List<String> allLines)
    {
        //allLines = text.split("\r\n|[\n-\r\u0085\u2028\u2029]");

        String line;
        if((line = allLines.get(allLines.size() - 1)) == null || line.isEmpty())
            allLines.remove(allLines.size() - 1);
        int idx;
        keyColumn = null;
        if(filename == null)
            name = "Untitled";
        else if((idx = filename.indexOf('.')) >= 0)
            name = TextTools.safeSubstring(filename, 0, idx);
        else
            name = filename;
        headerLine = TextTools.split(allLines.get(0), "\t");
        contentLines = new String[allLines.size() - 1][];
        for (int i = 0; i < contentLines.length; i++) {
            contentLines[i] = readLine(allLines.get(i+1));
        }
    }

    public String[] readLine(String temp) {
        int idx = -1;
        String[] result = new String[headerLine.length];
        for (int j = 0; j < headerLine.length - 1; j++) {
            if("".equals(headerLine[j]))
            {
                result[j] = "";
                idx = temp.indexOf('\t', idx+1);
            }
            else
            {
                result[j] = TextTools.safeSubstring(temp, idx+1, idx = temp.indexOf('\t', idx+1));
            }
        }
        if("".equals(headerLine[headerLine.length - 1]))
            result[headerLine.length - 1] = "";
        else
        {
            result[headerLine.length-1] = temp.substring(idx+1);
        }
        return result;
    }

    public void readFile(String filename)
    {
        try {
            Path path = Paths.get(filename);
            read(path.getFileName().toString(), Files.readAllLines(path));
        } catch (IOException e) {
            System.err.println("Could not read file (check that path is correct): " + filename);
        }
    }
    private static boolean stringArrayEquals(String[] left, String[] right) {
        if (left == right) return true;
        if (left == null || right == null) return false;
        final int len = left.length;
        if(len != right.length) return false;
        for (int i = 0; i < len; i++) { if(!java.util.Objects.equals(left[i], right[i])) return false; }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TSVReader tsvReader = (TSVReader) o;

        if (!stringArrayEquals(headerLine, tsvReader.headerLine)) return false;
        if (!Arrays.deepEquals(contentLines, tsvReader.contentLines)) return false;
        if (!Objects.equals(name, tsvReader.name)) return false;
        return Objects.equals(keyColumn, tsvReader.keyColumn);
    }

    @Override
    public int hashCode() {
        return Hasher.hashBulk(Hasher.Q, headerLine) ^ Hasher.hashBulk(Hasher.R, contentLines)
                ^ Hasher.hashBulk(Hasher.S, name) ^ Hasher.hashBulk(Hasher.T, keyColumn);
    }
}
