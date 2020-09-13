/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import net.year4000.utilities.ducktape.loaders.ClassModuleLoader;
import net.year4000.utilities.ducktape.module.ModuleInfo;
import net.year4000.utilities.ducktape.modules.*;
import org.junit.Assert;
import org.junit.Test;

public class DucktapeTest {

    @Test
    public void test() {
        Ducktape ducktape = Ducktape.builder()
            .addLoader(new ClassModuleLoader(ModuleA.class, ModuleB.class, ModuleC.class, ModuleD.class))
            //.setSaveLoadProvider(new GsonSaveLoadProvider())
            .build();
        ducktape.init();
    }

    @Test
    public void errorText() {
        Ducktape ducktape = Ducktape.builder()
            .addLoader(new ClassModuleLoader(ModuleE.class, ModuleError.class))
            .build();
        ducktape.init();
        Assert.assertArrayEquals(ducktape.getModules().stream().map(ModuleInfo::getPhase).toArray(),
            new ModuleInfo.Phase[] {ModuleInfo.Phase.ENABLED, ModuleInfo.Phase.ERROR});
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
        Assert.assertArrayEquals(sortedLoader.getModules().stream().map(ModuleInfo::getId).toArray(),
            randomLoader.getModules().stream().map(ModuleInfo::getId).toArray());
    }
}
