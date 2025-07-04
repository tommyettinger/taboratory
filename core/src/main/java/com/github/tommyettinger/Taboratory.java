package com.github.tommyettinger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Taboratory {

    public static void main(String[] args) {
        if (args == null || args.length < 1)
            return;
        String toolsPackage = "com.github.tommyettinger.ds",
				toolsClass = "ObjectObjectOrderedMap",
				makeMethod = "with";
        String inputName = TextTools.join(" ", args);
        try {
            TSVReader reader = new TSVReader();
            Path path = Paths.get(inputName);
            reader.read(path.getFileName().toString(), Files.readAllLines(path));
			CodeWriterJdkgdxds writer = new CodeWriterJdkgdxds();
			System.out.println(writer.write(reader));
			writer.writeTo(reader, new File(""));
        }catch (IOException ignored){}
    }
}
