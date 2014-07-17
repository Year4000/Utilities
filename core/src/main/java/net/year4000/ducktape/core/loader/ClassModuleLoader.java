package net.year4000.ducktape.core.loader;

import net.year4000.ducktape.core.module.ModuleManager;

public class ClassModuleLoader extends AbstractModuleLoader<ClassModuleLoader> {
    public ClassModuleLoader(ModuleManager manager) {
        super(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassModuleLoader add(Class<?> clazz) {
        manager.add(clazz);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassModuleLoader remove(Class<?> clazz) {
        manager.remove(clazz);
        return this;
    }
}
