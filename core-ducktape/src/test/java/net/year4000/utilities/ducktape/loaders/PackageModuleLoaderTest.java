/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class PackageModuleLoaderTest extends AbstractModuleClasses {
    private static final Path modulePath = Paths.get("src/test/resources/modules/classes");

    @Test
    public void packageTest() {
        PackageModuleLoader packageModuleLoader = new PackageModuleLoader(modulePath);
        Collection<Class<?>> classes = packageModuleLoader.load();
        System.out.println(classes);
        Assert.assertArrayEquals(MODULE_CLASSES, classStreamStringMap(classes));
    }
}
