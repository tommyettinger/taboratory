package com.github.tommyettinger.headless;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.github.tommyettinger.Taboratory;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "taboratory", version = "Taboratory 0.0.1-SNAPSHOT",
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
		HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
		configuration.updatesPerSecond = -1;
		try {
			new HeadlessApplication(new Taboratory(input), configuration){
				{
					try {
						mainLoopThread.join();
					} catch (InterruptedException e) {
						System.out.println("Interrupted!");
					}
				}
			};

		} catch (Exception e) {
			System.out.println("Parameters are not valid. Run with -h to show help.");
			return -1;
		}
		return 0;
	}
}