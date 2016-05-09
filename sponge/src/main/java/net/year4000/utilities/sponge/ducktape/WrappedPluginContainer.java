package net.year4000.utilities.sponge.ducktape;

import static com.google.common.base.Preconditions.checkNotNull;

import net.year4000.utilities.Utils;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class WrappedPluginContainer implements PluginContainer {
    private final Object plugin;
    private final Plugin pluginData;

    public WrappedPluginContainer(Object object) {
        this.plugin = checkNotNull(object, "object");
        this.pluginData = object.getClass().getAnnotation(Plugin.class);
    }

    @Override
    public String getId() {
        return Optional.ofNullable(pluginData).map(Plugin::id).orElse("unknown");
    }

    @Override
    public String getUnqualifiedId() {
        return Optional.ofNullable(pluginData).map(Plugin::id).orElse("unknown");
    }

    @Override
    public String getName() {
        return Optional.ofNullable(pluginData).map(Plugin::name).orElse("Unknown");
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.ofNullable(pluginData).map(Plugin::version);
    }

    @Override
    public Optional<Object> getInstance() {
        return Optional.ofNullable(plugin);
    }

    @Override
    public String toString() {
        return Utils.toString(pluginData);
    }
}
