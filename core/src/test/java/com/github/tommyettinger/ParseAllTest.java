package com.github.tommyettinger;

import com.github.tommyettinger.ds.ObjectObjectOrderedMap;
import generated.Data;
import generated.Effect3;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParseAllTest {
    @Test
    public void testCompare() throws IOException {
        ObjectObjectOrderedMap<String, Data> data = Data.parseAll(Files.readAllLines(Paths.get("../Data.tsv"), StandardCharsets.UTF_8));
        ObjectObjectOrderedMap<String, Effect3> effects = Effect3.parseAll(Files.readAllLines(Paths.get("../Data.tsv"), StandardCharsets.UTF_8));
        Assert.assertEquals(data.size(), effects.size());
        Assert.assertFalse(data.isEmpty());
        for (int i = 0; i < data.size(); i++) {
            Assert.assertEquals(data.keyAt(i), effects.keyAt(i));
            Assert.assertEquals(data.getAt(i).description, effects.getAt(i).description);
            Assert.assertEquals(data.getAt(i).valueAdd, effects.getAt(i).valueAdd);
        }
    }
}
