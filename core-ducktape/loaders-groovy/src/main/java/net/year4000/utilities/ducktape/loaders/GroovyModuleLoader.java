/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import groovy.lang.GroovyClassLoader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Collections;

/**
 * This class will support for loading Groovy files
 * into the class loader for the current running JVM.
 */
public class GroovyModuleLoader extends AbstractPathLoader implements ModuleLoader {
    public GroovyModuleLoader(Path directory) {
        super(AbstractPathLoader.createDirectories(directory));
    }

    /** Load any groovy script that ends with .groovy */
    protected Collection<Class<?>> load(Path directory) throws IOException {
        if (directory.toString().endsWith(".groovy")) {
            URL[] urls = new URL[] {directory.toUri().toURL()};
            GroovyClassLoader loader = AccessController.doPrivileged((PrivilegedAction<GroovyClassLoader>) () -> new GroovyClassLoader(new URLClassLoader(urls)));
            return Collections.singleton(loader.parseClass(directory.toFile()));
        }
        return Collections.emptySet();
    }
}
