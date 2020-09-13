/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.*;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.internal.ModuleInfo;
import net.year4000.utilities.ducktape.module.ModuleWrapper;

import java.util.Map;

/** This module for Guice will register the modules life cycles with Ducktape's Manager */
public class DucktapeModule extends AbstractModule {
    private final Map<ModuleInfo, ModuleWrapper> modules;

    public DucktapeModule(Map<ModuleInfo, ModuleWrapper> modules) {
        this.modules = modules;
    }

    @Override
    protected void configure() {
        bindScope(Module.class, Scopes.SINGLETON);

        // Register the construction of module with the system, order matters so place in a linked hash map
        bindListener(Matchers.any(), new ProvisionListener() {
            @Override
            public <T> void onProvision(ProvisionInvocation<T> provision) {
                Class<?> type = provision.getBinding().getKey().getTypeLiteral().getRawType();
                if (type.isAnnotationPresent(Module.class)) {
                    ModuleInfo info = new ModuleInfo(type);
                    synchronized (DucktapeModule.this.modules) {
                        DucktapeModule.this.modules.put(info, null);
                    }
                    info.constructing();
                }
            }
        });

        // Modules has been constructed, load them in the correct order for dependencies not the same order as they were constructed
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                // If the module has been constructed load it
                if (type.getRawType().isAnnotationPresent(Module.class)) {
                    encounter.register((InjectionListener<I>) injectee -> {
                        ModuleInfo info = new ModuleInfo(injectee.getClass());
                        ModuleWrapper state = new ModuleWrapper(info, injectee);
                        synchronized (DucktapeModule.this.modules) {
                            DucktapeModule.this.modules.remove(info); // remove instance
                            DucktapeModule.this.modules.put(info, state); // re add to keep new order
                        }
                        state.load();
                    });
                }
            }
        });
    }
}
