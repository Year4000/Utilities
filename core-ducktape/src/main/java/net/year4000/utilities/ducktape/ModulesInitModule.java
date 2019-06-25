/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.reflection.Reflections;

import java.util.Collection;

/** This module will register all modules as singletons and their additional bindings */
public class ModulesInitModule extends AbstractModule {
    private final Collection<Class<?>> loadedClasses;

    public ModulesInitModule(Class<?>... classes) {
        this(Sets.newHashSet(classes));
    }

    public ModulesInitModule(Collection<Class<?>> classes) {
        this.loadedClasses = Conditions.nonNull(classes, "classes must not be null but can be empty");
    }

    @Override
    protected void configure() {
        for (Class<?> clazz : this.loadedClasses) {
            if (clazz.isAnnotationPresent(Module.class)) {
                Module module = clazz.getAnnotation(Module.class);
                // Install modules before the class is loaded in case the module uses the bindings
                for (Class<? extends com.google.inject.Module> installModule : module.injectors()) {
                    install(Reflections.instance(installModule).getOrThrow("Error constructing Guice module"));
                }
                // Modules are singleton instances but they can be enabled and disabled
                bind(TypeLiteral.get(clazz)).asEagerSingleton();
            }
        }
    }
}
