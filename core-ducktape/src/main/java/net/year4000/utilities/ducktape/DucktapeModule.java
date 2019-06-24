/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.ModuleInfo;
import net.year4000.utilities.ducktape.module.ModuleWrapper;

import java.util.Map;

public class DucktapeModule extends AbstractModule {
    private DucktapeManager manager;
    private final Map<ModuleInfo, ModuleWrapper> modules;

    public DucktapeModule(DucktapeManager manager) {
        this.manager = manager;
        this.modules = manager.modules;
    }

    @Override
    protected void configure() {
        install(new SettingsModule());

        bind(DucktapeManager.class).toInstance(manager);
        bindScope(Module.class, Scopes.SINGLETON);

        // Register the construction of module with the system, order matters so place in a linked hash map
        bindListener(Matchers.any(), new ProvisionListener() {
            @Override
            public <T> void onProvision(ProvisionInvocation<T> provision) {
                Object source = provision.getBinding().getSource();
                if (source instanceof Class<?>) {
                    Class<?> clazz = (Class<?>) source;
                    if (clazz.isAnnotationPresent(Module.class)) {
                        ModuleInfo info = new ModuleInfo(clazz);
                        synchronized (DucktapeModule.this.modules) {
                            DucktapeModule.this.modules.put(info, null);
                        }
                        info.constructing();
                    }
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
