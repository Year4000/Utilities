/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import net.year4000.utilities.ducktape.module.Load;
import net.year4000.utilities.ducktape.module.Module;

@Module(id = "b", injectors = {ModuleB.ModuleBModule.class})
public class ModuleB {
    @Inject @Named("test") private String string;

    public ModuleB() {
        System.out.println("ModuleB Constructor");
    }

    @Load
    public void loadMethodDoesNotNeedToBeCalledLoad() {
        System.out.println("ModuleB loader: " + string);
    }

    public static class ModuleBModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Names.named("test")).toInstance("HAHAHA");
            bind(String.class).annotatedWith(Names.named("bob")).toInstance("bobby");
        }
    }
}
