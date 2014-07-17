package net.year4000.ducktape.bukkit.module;

import com.google.common.eventbus.Subscribe;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.bukkit.DuckTape;
import net.year4000.ducktape.core.module.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.*;

public class ModuleManagerListener {
    private Map<AbstractModule, Set<Listener>> listeners = new HashMap<>();

    @Subscribe
    public void onEnable(ModuleEnableEvent event) {
        ModuleListener listeners = event.getModule().getClass().getAnnotation(ModuleListener.class);
        Set<Listener> listenerClasses = new HashSet<>();

        if (listeners != null) {
            for (Class<?> listener : listeners.listeners()) {
                try {
                    Listener listen = (Listener) listener.newInstance();
                    Bukkit.getPluginManager().registerEvents(listen, DuckTape.get());
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

        listenerClasses.forEach(HandlerList::unregisterAll);
    }
}
