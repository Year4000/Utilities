package net.year4000.utilities.sponge.ducktape;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Injector;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.ducktape.ModuleManager;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** Uses the SpongeAPI to add additional modules for Utilities */
public class SpongeModuleManager extends ModuleManager {
    private static final String MOD_PATH = "mods/";
    private final Map<WrappedPluginContainer, Object> modules = Maps.newConcurrentMap();

    /** Get the modules that were added */
    public Collection<PluginContainer> getModules() {
        return ImmutableList.copyOf(modules.keySet());
    }

    /** Inject modules with the parent injector */
    public void injectModules(Injector injector) {
        Set<Class<?>> pluginClasses = Sets.newHashSet();
        // Find all modules
        loadAll(Paths.get(MOD_PATH), collection -> {
            collection.forEach(clazz -> {
                Plugin plugin = clazz.getAnnotation(Plugin.class);
                if (plugin != null) {
                    pluginClasses.add(clazz);
                }
            });
        });
        // Init the modules and inject the fields
        pluginClasses.forEach(clazz -> {
            try {
                Object plugin = Reflections.instance(clazz).getOrThrow();
                injector.injectMembers(plugin); // Inject
                modules.put(new WrappedPluginContainer(plugin), plugin);
            } catch (Exception error) {
                ErrorReporter.builder(error)
                    .add("Problem constructing module")
                    .add("Class: ", clazz)
                    .buildAndReport(System.err);
            }
        });
    }

    /** Register the listeners found in the modules */
    public void registerListeners() {
        try {
            modules.forEach(this::registerModuleListeners);
        } catch (Throwable throwable) {
            ErrorReporter.builder(throwable)
                .add("Problem registering the listeners")
                .buildAndReport(System.err);
        }
    }

    /** Register the listeners for the module */
    @SuppressWarnings("unchecked")
    private void registerModuleListeners(WrappedPluginContainer container, Object module) {
        for (Method method : module.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(Listener.class) == null) {
                continue; // Must register methods with a listener annotation
            }
            Class<?>[] types = method.getParameterTypes();
            if (types.length == 0) {
                continue; // Must have at least one parameter
            }
            if (!Event.class.isAssignableFrom(types[0])) {
                System.err.println("isAssignableFrom");
                continue; // First param must be an event
            }
            Class<? extends Event> eventClass = (Class<? extends Event>) types[0];
            Sponge.getEventManager().registerListener(Utilities.get(), eventClass, event -> {
                try {
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
}
