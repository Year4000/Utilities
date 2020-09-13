/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import com.google.common.collect.Sets;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.ducktape.ModuleInitException;
import net.year4000.utilities.ducktape.module.ModuleInfo;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

/** An abstract class that will handle the checking of directory */
public abstract class AbstractPathLoader {
    private final Path directory;

    public AbstractPathLoader(Path directory) {
        this.directory = Conditions.nonNull(directory, "directory must exist");
        Conditions.condition(Files.isDirectory(directory), "directory must be a valid directory");
    }

    /** Get the directory of the path */
    public Path directory() {
        return this.directory;
    }

    /** Create the parent directories and return the newly created directory */
    public static Path createDirectories(Path directory) {
        Conditions.nonNull(directory, "directory must exist");
        try {
            if (!Files.isDirectory(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException error) {
            throw ErrorReporter.builder(error)
                .add("Directory: ", directory.toAbsolutePath())
                .buildAndReport();
        }
        return directory;
    }

    /** Load classes from where ever and create the collection of classes for the modules */
    public Collection<Class<?>> load() throws ModuleInitException {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        try {
            classes.addAll(load(this.directory()));
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(this.directory())) {
                for (Path path : stream) {
                    classes.addAll(load(path));
                }
            }
        } catch (IOException error) {
            throw new ModuleInitException(ModuleInfo.Phase.LOADING, error);
        }
        return classes;
    }

    /** The implementation to gather the class for each directory */
    protected abstract Collection<Class<?>> load(Path directory) throws IOException;
}
