package net.year4000.ducktape.loader;

import net.year4000.ducktape.module.ModuleManager;

public class ClassModuleLoader extends AbstractModuleLoader<ClassModuleLoader> {
    public ClassModuleLoader(ModuleManager manager) {
        super(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassModuleLoader add(Class<?> clazz) {
        classes.add(clazz);
        manager.add(clazz);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassModuleLoader remove(Class<?> clazz) {
        classes.remove(clazz);
        manager.remove(clazz);
        return this;
    }
}
