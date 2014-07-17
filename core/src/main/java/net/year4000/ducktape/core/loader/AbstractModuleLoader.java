package net.year4000.ducktape.core.loader;

import lombok.Getter;
import net.year4000.ducktape.core.module.ModuleManager;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractModuleLoader<T extends AbstractModuleLoader> {
    /** The module manager to load the classes into */
    protected final ModuleManager manager;

    /** The classes that were added in this loader's instance */
    @Getter
    protected final Set<Class<?>> classes = new HashSet<>();

    public AbstractModuleLoader(ModuleManager manager) {
        this.manager = manager;
    }

    /** Add the class to ModuleManager */
    public abstract T add(Class<?> clazz);

    /** Remove the class from ModuleManager */
    public abstract T remove(Class<?> clazz);

    /** Format the path */
    public String formatPath(String path) {
        if (path.length() < 6) return path;
        return path.substring(0, path.length() - 6).replaceAll("/", ".");
    }
}
