package com.github.tommyettinger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Taboratory {

    private Taboratory() {
    }

    public static void run(String input){
//        int nameStart = Math.max(input.lastIndexOf('/'), input.lastIndexOf('\\')) + 1;
//        String trimmed = input.substring(nameStart, input.lastIndexOf('.', nameStart));
        try {
			CodeWriterJdkgdxds writer = new CodeWriterJdkgdxds(input);
			System.out.println(writer.write());
			writer.writeTo(new File(""));
        }catch (IOException ioe){
            throw new RuntimeException("Input or output failed.");
        }
    }
}
