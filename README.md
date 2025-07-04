# taboratory
Generate classes from TSV tables; reload on demand!


# Notes

Build a Graal Native Image exe by finding the GraalVM 24 bin folder and using its native-image:

`native-image.cmd -cp picocli-4.7.7.jar --enable-native-access=ALL-UNNAMED -jar taboratory.jar`
