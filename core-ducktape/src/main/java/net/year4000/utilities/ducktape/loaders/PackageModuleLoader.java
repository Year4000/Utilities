/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PackageModuleLoader extends AbstractPathLoader implements ModuleLoader {
    public PackageModuleLoader(Path directory) {
        super(AbstractPathLoader.createDirectories(directory));
    }

    /** Get all the class names */
    public Set<String> getClassNames(Path classDir) {
        return recursiveGetClasses(classDir.toFile(), "");
    }

    /** Get all the class names */
    @SuppressWarnings("ConstantConditions")
    public Set<String> recursiveGetClasses(File dir, String parentName) {
        File[] files = dir.listFiles();
        if (files != null) {
            return Stream.of(dir.listFiles())
                .flatMap(file -> {
                    if (file.isDirectory()) {
                        return recursiveGetClasses(file, parentName + file.getName() + ".").stream();
                    } else if (file.getName().endsWith(".class")) {
                        return Stream.of(parentName + Loaders.formatPath(file.getName()));
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    protected Collection<Class<?>> load(Path directory) throws IOException {
        ClassLoader loader = Loaders.classLoaderFromPath(directory);
        return getClassNames(directory).stream()
            .map(entry -> {
                try {
                    return loader.loadClass(entry);
                } catch (ClassNotFoundException error) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }
}
