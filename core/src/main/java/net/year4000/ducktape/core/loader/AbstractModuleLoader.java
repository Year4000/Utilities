package net.year4000.ducktape.core.loader;

import net.year4000.ducktape.core.module.ModuleManager;

public abstract class AbstractModuleLoader<T extends AbstractModuleLoader> {
    /** The module manager to load the classes into */
    protected ModuleManager manager;

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
