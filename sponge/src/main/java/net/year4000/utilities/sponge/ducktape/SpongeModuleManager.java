package net.year4000.utilities.sponge.ducktape;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import net.year4000.utilities.ducktape.ModuleManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Paths;
import java.util.Set;

public class SpongeModuleManager extends ModuleManager {
    private static final String MOD_PATH = "mods/";

    public void injectModules() {
        Set<Class<?>> pluginClasses = Sets.newHashSet();

        // Find all plugins
        loadAll(Paths.get(MOD_PATH), collection -> {
            collection.forEach(clazz -> {
                Plugin plugin = clazz.getAnnotation(Plugin.class);

                if (plugin != null) {
                    pluginClasses.add(clazz);
                }
            });
        });

        pluginClasses.forEach(clazz -> {
            try {
                Object plugin = clazz.newInstance();
                Sponge.getEventManager().registerListeners(new WrappedPluginContainer(plugin), plugin);
            }
            catch (Exception error) {
                Throwables.propagate(error);
            }
        });
    }
}
