package com.github.tommyettinger;

import java.io.File;
import java.io.IOException;

/**
 * Only exists so PicoCLI can run this. Delegates to {@link Generator} in almost every way.
 */
public final class Taboratory {

    private Taboratory() {
    }

    public static void run(String input){
        try {
			Generator writer = new Generator(input);
			System.out.println(writer.write());
            // writes to the same directory as the input, in the subfolder "generated", which is also the package
			writer.writeTo(new File(""));
        }catch (IOException ioe){
            throw new RuntimeException("Input or output failed.", ioe);
        }
    }
}
