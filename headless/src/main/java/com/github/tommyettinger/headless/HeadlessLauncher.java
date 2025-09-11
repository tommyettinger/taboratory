package com.github.tommyettinger.headless;

import com.github.tommyettinger.Taboratory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "taboratory", version = "Taboratory 0.0.2",
		description = "Takes a .tsv tab-separated value file and writes Java sources inside './generated/' .",
		mixinStandardHelpOptions = true)
public class HeadlessLauncher implements Callable<Integer> {

	@CommandLine.Parameters(description = "The absolute or relative path to one or more tab-separated value (.tsv) files.", defaultValue = "Data.tsv")
	public String input = "Data.tsv";

	public static void main(String[] args) {
		int exitCode = new picocli.CommandLine(new HeadlessLauncher()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		try {
			// creates a file in the project root, next to settings.gradle
			Taboratory.run(input);
		} catch (Exception e) {
			System.out.println("Parameters are not valid, or something failed. Run with -h to show help.\n" +
                    "Original error message:\n" + e.getLocalizedMessage());
			return -1;
		}
		return 0;
	}
}