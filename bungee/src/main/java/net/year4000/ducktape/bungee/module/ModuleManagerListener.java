package net.year4000.ducktape.bungee.module;

import com.google.common.eventbus.Subscribe;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.bungee.DuckTape;
import net.year4000.ducktape.core.module.AbstractModule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModuleManagerListener {
    private Map<AbstractModule, Set<Listener>> listeners = new HashMap<>();

    @Subscribe
    public void onEnable(ModuleEnableEvent event) {
        ModuleListeners listeners = event.getModule().getClass().getAnnotation(ModuleListeners.class);
        Set<Listener> listenerClasses = new HashSet<>();

        if (listeners != null) {
            for (Class<?> listener : listeners.value()) {
                try {
                    Listener listen = (Listener) listener.newInstance();
                    ProxyServer.getInstance().getPluginManager().registerListener(DuckTape.get(), listen);
                    listenerClasses.add(listen);
                    DuckTape.debug(listener.getTypeName() + " registered listener.");
                } catch (IllegalAccessException | InstantiationException e) {
                    DuckTape.debug(e, true);
                }
            }

            this.listeners.put(event.getModule(), listenerClasses);
        }
    }

    @Subscribe
    public void onDisable(ModuleDisableEvent event) {
        Set<Listener> listenerClasses = listeners.get(event.getModule());

        listenerClasses.forEach(listener -> ProxyServer.getInstance().getPluginManager().unregisterListener(listener));
    }
}
