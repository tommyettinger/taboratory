package com.github.tommyettinger;

import com.badlogic.gdx.ApplicationAdapter;
import com.github.tommyettinger.digital.TextTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Taboratory extends ApplicationAdapter {

    public String input, trimmed;
    public Taboratory(String input){
        this.input = input;
        int nameStart = Math.max(input.lastIndexOf('/'), input.lastIndexOf('\\')) + 1;
        trimmed = input.substring(nameStart, input.lastIndexOf('.', nameStart));

    }

    @Override
    public void create() {
        if (input == null)
            return;
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
