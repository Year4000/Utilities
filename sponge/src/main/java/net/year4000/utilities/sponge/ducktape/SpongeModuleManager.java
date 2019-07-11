/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.sponge.ducktape;

import com.google.inject.Guice;
import com.google.inject.Injector;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.ducktape.*;
import net.year4000.utilities.ducktape.loaders.ModuleLoader;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.internal.ModuleInfo;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;

import java.lang.reflect.Method;
import java.util.*;

/** Uses the SpongeAPI to add additional modules for Utilities */
public class SpongeDucktapeManager extends DucktapeManager {
    private static final String MOD_PATH = "mods/";
    private final List<WrappedPluginContainer> modules = new ArrayList<>();

    protected SpongeDucktapeManager(Injector injector, Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap) {
        super(injector, loaderMap);
    }

    /** Load all classes from the selected path */
    protected Set<Class<?>> loadAll() throws ModuleInitException {
        Set<Class<?>> classes = new HashSet<>();
        this.loaders.forEach((key, value) -> {
            if (loaded.contains(key)) {
                throw new ModuleInitException(ModuleInfo.Phase.LOADING, new IllegalStateException("Can not load the same loader twice."));
            }
            Collection<Class<?>> loadedClasses = value.load();
                loadedClasses.forEach(clazz -> {
                Module module = clazz.getAnnotation(Module.class);
                if (module != null) {
                    modules.add(new WrappedPluginContainer(clazz));
                }
            });
            classes.addAll(loadedClasses);
            loaded.add(key);
        });
        return classes;
    }

    public Collection<WrappedPluginContainer> getWrappedModules() {
        return this.modules;
    }

//    /** Inject modules with the parent injector */
//    public void injectModules(Injector injector) {
//        Set<Class<?>> pluginClasses = Sets.newHashSet();
//        // Find all modules
//        loadAll(Paths.get(MOD_PATH), collection -> {
//            collection.forEach(clazz -> {
//                Plugin plugin = clazz.getAnnotation(Plugin.class);
//                if (plugin != null) {
//                    pluginClasses.add(clazz);
//                }
//            });
//        });
//        // Init the modules and inject the fields
//        pluginClasses.forEach(clazz -> {
//            try {
//                Object plugin = Reflections.instance(clazz).getOrThrow();
//                injector.injectMembers(plugin); // Inject
//                modules.put(new WrappedPluginContainer(plugin), plugin);
//            } catch (Exception error) {
//                ErrorReporter.builder(error)
//                    .add("Problem constructing module")
//                    .add("Class: ", clazz)
//                    .buildAndReport(System.err);
//            }
//        });
//    }

    /** Register the listeners found in the modules */
    public void registerListeners() {
        modules.forEach(container -> {
            try {
                registerModuleListeners(container, getModule(container.getId()));
            } catch (Throwable throwable) {
                ErrorReporter.builder(throwable)
                    .hideStackTrace()
                    .add("Problem registering the listeners")
                    .add("ID: ", container.getId())
                    .add("Name: ", container.getName())
                    .add("Error: ", throwable.getMessage())
                    .buildAndReport(System.err);
            }
        });
    }

    /** Register the listeners for the module */
    @SuppressWarnings("unchecked")
    private void registerModuleListeners(WrappedPluginContainer container, Object module) {
        for (Method method : module.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(Listener.class) == null) {
                continue; // Must register methods with a listener annotation
            }
            Class<?>[] types = method.getParameterTypes();
            Conditions.condition(types.length > 0, "Must have more than 0 arguments");
            Conditions.condition(Event.class.isAssignableFrom(types[0]), "First arg must be an Event");
            Class<? extends Event> eventClass = (Class<? extends Event>) types[0];
            Sponge.getEventManager().registerListener(Utilities.get(), eventClass, event -> {
                try { // Proxy the event to the module
                    Reflections.invoke(module, method, event);
                } catch (Throwable throwable) {
                    ErrorReporter.builder(throwable)
                        .add("Parameter Count: ", method.getParameterCount())
                        .add("Module: ", container)
                        .add("Event: ", eventClass, event)
                        .buildAndReport(System.err);
                }
            });
        }
    }

    public static SpongeDucktapeManagerBuilder builder() {
        return new SpongeDucktapeManagerBuilder();
    }

    /** This will build the ducktape manager environment with the correct module loaders and guice injector */
    public static class SpongeDucktapeManagerBuilder extends DucktapeManagerBuilder {
        @Override
        public Ducktape build() {
            Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap = loaderMapReduce();
            return new SpongeDucktapeManager(injectorValue.getOrElse(Guice.createInjector()), loaderMap);
        }
    }
}
