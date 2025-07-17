package com.github.tommyettinger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Taboratory {

    private Taboratory() {
    }

    public static void run(String input){
        int nameStart = Math.max(input.lastIndexOf('/'), input.lastIndexOf('\\')) + 1;
//        String trimmed = input.substring(nameStart, input.lastIndexOf('.', nameStart));
        try {
            TSVReader reader = new TSVReader();
            Path path = Paths.get(input);
            reader.read(path.getFileName().toString(), Files.readAllLines(path));
			CodeWriterJdkgdxds writer = new CodeWriterJdkgdxds();
			System.out.println(writer.write(reader));
			writer.writeTo(reader, new File(""));
        }catch (IOException ignored){}
    }
}
