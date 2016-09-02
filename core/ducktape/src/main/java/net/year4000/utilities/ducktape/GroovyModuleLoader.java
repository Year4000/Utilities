/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import static com.google.common.base.Preconditions.checkNotNull;

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
public class GroovyModuleLoader implements ModuleLoader {

    /** Load any groovy script that ends with .groovy */
    @Override
    public Collection<Class<?>> load(Path dir) throws IOException {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        Class<?> clazzLoader = ModuleManager.class;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(checkNotNull(dir))) {
            for (Path path : stream) {
                if (!path.toString().endsWith(".groovy")) {
                    continue;
                }
                URL url = path.toUri().toURL();
                URL[] urls = new URL[] {url};
                ClassLoader parent = AccessController.doPrivileged((PrivilegedAction<URLClassLoader>) () ->
                    new URLClassLoader(urls, clazzLoader.getClassLoader())
                );
                // Convert the Groovy script to a Java Class
                GroovyClassLoader loader = new GroovyClassLoader(parent);
                loader.addURL(clazzLoader.getResource("/"));
                loader.addURL(url);
                Class clazz = loader.parseClass(path.toFile());
                classes.add(clazz);
            }
        }

        return classes;
    }
}
