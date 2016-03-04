/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.year4000.utilities.ducktape;

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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class will support for loading Groovy files
 * into the class loader for the current running JVM.
 */
public class GroovyModuleLoader implements ModuleLoader {

    /** Load any groovy script that ends with .groovy */
    @Override
    public Collection<Class<?>> load(Path dir) throws IOException {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        Class<?> clazzLoader = GroovyModuleLoader.class;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(checkNotNull(dir))) {
            for (Path path : stream) {
                if (!path.endsWith(".groovy")) {
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
