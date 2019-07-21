/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class JarModuleLoaderTest extends AbstractModuleClasses {
    private static final Path modulePath = Paths.get("src/test/resources/modules/jars");

    @Test
    public void jarTest() {
        JarModuleLoader jarModuleLoader = new JarModuleLoader(modulePath);
        Collection<Class<?>> classes = jarModuleLoader.load();
        System.out.println(classes);
        Assert.assertArrayEquals(MODULE_CLASSES, classStreamStringMap(classes));
    }
}
