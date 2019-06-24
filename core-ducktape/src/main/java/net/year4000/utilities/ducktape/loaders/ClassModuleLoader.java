/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public class ClassModuleLoader implements ModuleLoader {
    @Override
    public Collection<Class<?>> load(Path path) throws IOException {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        //asses.add(Class.forName(path.getName(), ClassModuleLoader.class.getClassLoader()));

        return classes;
    }
}
