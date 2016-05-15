package net.year4000.utilities.ducktape;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * This is a functional interface that will load
 * a module from the select file path.
 */
public interface ModuleLoader {

    /** This will search the path and load the module */
    Collection<Class<?>> load(Path path) throws IOException;

    /** This will create a file object from a string and load the class paths */
    default Collection<Class<?>> load(String path) throws IOException {
        return load(Paths.get(path));
    }

    /** This will create a file object from a string and load the class paths */
    default Collection<Class<?>> load(File file) throws IOException {
        return load(file.toPath());
    }
}
