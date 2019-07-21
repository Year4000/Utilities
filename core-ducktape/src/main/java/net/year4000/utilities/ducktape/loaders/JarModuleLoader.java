/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import net.year4000.utilities.ErrorReporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarModuleLoader extends AbstractPathLoader implements ModuleLoader {
    public JarModuleLoader(Path directory) {
        super(AbstractPathLoader.createDirectories(directory));
    }

    /** Load any groovy script that ends with .groovy */
    @Override
    protected Collection<Class<?>> load(Path directory) throws IOException, RuntimeException {
        if (directory.toString().endsWith(".jar")) {
            ClassLoader loader = Loaders.classLoaderFromPath(directory);
            // And then the files in the jar
            return new JarFile(directory.toFile()).stream()
                .filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                .map(entry -> {
                    try {
                        return Class.forName(Loaders.formatPath(entry.getName()), true, loader);
                    } catch (ClassNotFoundException error) {
                        throw ErrorReporter.builder(error).buildAndReport();
                    }
                })
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
