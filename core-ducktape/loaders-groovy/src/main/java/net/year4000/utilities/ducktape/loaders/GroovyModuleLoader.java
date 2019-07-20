/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import com.google.common.collect.Sets;
import groovy.lang.GroovyClassLoader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Set;

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
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (!path.toString().endsWith(".groovy")) {
                    continue;
                }
                URL url = path.toUri().toURL();
                GroovyClassLoader loader = AccessController.doPrivileged((PrivilegedAction<GroovyClassLoader>) () -> {
                    // Convert the Groovy script to a Java Class
                    Class<?> clazz = getClass();
                    GroovyClassLoader groovyLoader = new GroovyClassLoader(new URLClassLoader(new URL[] {url}, clazz.getClassLoader()));
                    groovyLoader.addURL(clazz.getResource("/"));
                    groovyLoader.addURL(url);
                    return groovyLoader;
                });
                Class<?> clazz = loader.parseClass(path.toFile());
                classes.add(clazz);
            }
        }
        return classes;
    }
}
