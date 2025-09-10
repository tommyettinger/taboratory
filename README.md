# taboratory
Generate classes from TSV tables; reload on demand!


# Notes

Build a Graal Native Image exe by finding the GraalVM 24 bin folder and using its native-image:

`native-image.cmd -march=compatibility -cp picocli-4.7.7.jar -jar taboratory.jar`

For my machine, where I have Graal 24 installed by IDEA, it looks like this:

`%homedrive%%homepath%\.jdks\graalvm-jdk-24.36.1\bin\native-image.cmd -march=compatibility -cp picocli-4.7.7.jar -jar headless\build\libs\taboratory.jar`