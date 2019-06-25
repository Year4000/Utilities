/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import net.year4000.utilities.ducktape.loaders.ClassModuleLoader;
import net.year4000.utilities.ducktape.module.ModuleInfo;
import net.year4000.utilities.ducktape.modules.ModuleD;
import net.year4000.utilities.ducktape.modules.ModuleA;
import net.year4000.utilities.ducktape.modules.ModuleB;
import net.year4000.utilities.ducktape.modules.ModuleC;
import org.junit.Assert;
import org.junit.Test;

public class DucktapeTest {

    @Test
    public void test() {
        Ducktape ducktape = Ducktape.builder()
            .addLoader(new ClassModuleLoader(ModuleA.class, ModuleB.class, ModuleC.class, ModuleD.class))
            .build();
        ducktape.init();
    }

    @Test
    public void loadModules() {
        // Modules were created in a way to have the dependency graph the same when shuffled
        Ducktape sortedLoader = Ducktape.builder()
            .addLoader(new ClassModuleLoader(ModuleA.class, ModuleB.class, ModuleC.class, ModuleD.class))
            .build();
        sortedLoader.init();
        Ducktape randomLoader = Ducktape.builder()
            .addLoader(new ClassModuleLoader(ModuleB.class, ModuleD.class, ModuleA.class,  ModuleC.class))
            .build();
        randomLoader.init();
        Assert.assertArrayEquals(sortedLoader.getModules().stream().map(ModuleInfo::getId).toArray(), randomLoader.getModules().stream().map(ModuleInfo::getId).toArray());
    }
}
