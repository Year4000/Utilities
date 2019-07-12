/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GroovyModuleLoaderTest {
    private static final Path modulePath = Paths.get("src/test/resources/modules");
    private int groovyFiles;

    @Before
    public void getGroovyFiles() throws IOException {
        this.groovyFiles = 0;
        try (DirectoryStream<Path> pathStream = Files.newDirectoryStream(modulePath)) {
            for (Path path : pathStream) {
                if (path.toString().endsWith(".groovy")) {
                    this.groovyFiles++;
                }
            }
        }
    }

    @Test
    public void groovyLoaderTest() {
        GroovyModuleLoader loader = new GroovyModuleLoader(modulePath);
        Assert.assertEquals(groovyFiles, loader.load().size());
    }
}
