/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import java.util.Collection;
import java.util.stream.Collectors;

/** Shared code to test if the modules have been loaded properly */
public abstract class AbstractModuleClasses {
    protected static final String[] MODULE_CLASSES = new String[] {
        "class net.year4000.utilities.ducktape.modules.ModuleA",
        "class net.year4000.utilities.ducktape.modules.ModuleA$ModuleASettings",
        "class net.year4000.utilities.ducktape.modules.ModuleB",
        "class net.year4000.utilities.ducktape.modules.ModuleB$ModuleBModule",
        "class net.year4000.utilities.ducktape.modules.ModuleC",
        "class net.year4000.utilities.ducktape.modules.ModuleC$ModuleCSettings",
        "class net.year4000.utilities.ducktape.modules.ModuleD",
    };

    protected String[] classStreamStringMap(Collection<Class<?>> classes) {
        return classes.stream()
            .map(Class::toString)
            .sorted()
            .collect(Collectors.toList())
            .toArray(new String[] {});
    }
}
