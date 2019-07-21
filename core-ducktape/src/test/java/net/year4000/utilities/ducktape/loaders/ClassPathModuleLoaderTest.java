/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import net.year4000.utilities.ducktape.module.Module;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class ClassPathModuleLoaderTest extends AbstractModuleClasses {
    private static final String PACKAGE_PREFIX = "net.year4000.utilities.ducktape.modules";
    private static final String[] MODULE_CLASSES = new String[] {
        "class net.year4000.utilities.ducktape.modules.ModuleA",
        "class net.year4000.utilities.ducktape.modules.ModuleB",
        "class net.year4000.utilities.ducktape.modules.ModuleC",
        "class net.year4000.utilities.ducktape.modules.ModuleD",
    };

    @Test
    public void classPathTest() {
        // We are testing the package prefix to speed up the unit test
        ClassPathModuleLoader classPathModuleLoader = new ClassPathModuleLoader(PACKAGE_PREFIX, Module.class);
        Collection<Class<?>> classes = classPathModuleLoader.load();
        System.out.println(classes);
        Assert.assertArrayEquals(MODULE_CLASSES, classStreamStringMap(classes));
    }
}
