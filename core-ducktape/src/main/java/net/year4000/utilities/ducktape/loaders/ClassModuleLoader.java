/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import com.google.common.collect.Sets;

import java.util.Collection;

/** Create the module loader with a fixed set of classes */
public class ClassModuleLoader implements ModuleLoader {
    private final Class<?>[] classes;

    public ClassModuleLoader(Class<?>... classes) {
        this.classes = classes;
    }

    @Override
    public Collection<Class<?>> load() {
        return Sets.newHashSet(this.classes);
    }
}
