package com.github.tommyettinger;

import com.github.tommyettinger.digital.TextTools;

import java.util.Arrays;
import java.util.List;

public class TSVReader {
    public String[] headerLine;
    public String[][] contentLines;
    public String name;
    public TSVReader()
    {
    }
    public void read(String filename, String text)
    {
        read(filename, Arrays.asList(text.split("\r\n|[\n-\r\u0085\u2028\u2029]")));
//        read(filename, Arrays.asList(text.split("\\v+")));
   }
    public void read(String filename, List<String> allLines)
    {
        String line;
        if((line = allLines.get(allLines.size() - 1)) == null || line.isEmpty())
            allLines.remove(allLines.size() - 1);
        int idx;
        if(filename == null)
            name = "Untitled";
        else if((idx = filename.indexOf('.')) >= 0)
            name = TextTools.safeSubstring(filename, 0, idx);
        else
            name = filename;
        headerLine = TextTools.split(allLines.get(0), "\t");
        contentLines = new String[allLines.size() - 1][];
        for (int i = 0; i < contentLines.length; i++) {
            contentLines[i] = readLine(allLines.get(i+1), headerLine);
        }
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

//    public void readFile(String filename) {
//        try {
//            Path path = Paths.get(filename);
//            read(path.getFileName().toString(), Files.readAllLines(path));
//        } catch (IOException e) {
//            System.err.println("Could not read file (check that path is correct): " + filename);
//        }
//    }

    private static boolean stringArrayEquals(String[] left, String[] right) {
        if (left == right) return true;
        if (left == null || right == null) return false;
        final int len = left.length;
        if(len != right.length) return false;
        for (int i = 0; i < len; i++) { if(!java.util.Objects.equals(left[i], right[i])) return false; }
        return true;
    }
}
