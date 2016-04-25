/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.plugin.PluginManager;

import java.util.Map;
import java.util.Optional;

/** A simple way of adding default functions of a sponge plugin */
public abstract class AbstractSpongePlugin {

    /** The Logger that Sponge injects into when the GameConstructs plugins */
    @Inject
    protected Logger logger;

    /** The PluginManager that Sponge injects into when the GameConstructs plugins */
    @Inject
    protected PluginManager pluginManager;

    /** The instance of the current plugin */
    private static final Map<Class<?>, AbstractSpongePlugin> plugin = Maps.newConcurrentMap();

    /** Simple way of letting child grab the current instance */
    @SuppressWarnings("unchecked")
    protected static <T extends AbstractSpongePlugin> T instance(Class<?> clazz) throws IllegalStateException {
        return (T) Optional.ofNullable(plugin.get(clazz)).orElseThrow(IllegalStateException::new);
    }

    /** Let the {@link #instance(Class clazz)} work by populating {@link #plugin} */
    @Listener
    public void onAbstractSpongePluginInit(GameConstructionEvent event) {
        AbstractSpongePlugin.plugin.put(getClass(), this);
    }

    /** Log a message using info, use System.out when plugin is not inited */
    protected static void log(AbstractSpongePlugin instance, Object object, Object... args) {
        if (plugin.isEmpty()) {
            System.out.println(String.format(object.toString(), args));
        }
        else {
            instance.logger.info(object.toString(), args);
        }
    }

    /** Debug a message using warn, use System.err when plugin is not inited */
    protected static void debug(AbstractSpongePlugin instance, Object object, Object... args) {
        if (plugin.isEmpty()) {
            System.err.println(String.format(object.toString(), args));
        }
        else {
            instance.logger.warn(object.toString(), args);
        }
    }
}
