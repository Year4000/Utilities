package net.year4000.ducktape.bukkit.module;

import com.google.common.eventbus.Subscribe;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.bukkit.DuckTape;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManagerListener {
    private Map<BukkitModule, Set<Listener>> listeners = new ConcurrentHashMap<>();

    @Subscribe
    public void onEnable(ModuleEnableEvent event) {
        ModuleListeners listeners = event.getModule().getClass().getAnnotation(ModuleListeners.class);
        Set<Listener> listenerClasses = new HashSet<>();

        if (listeners != null) {
            for (Class<? extends Listener> listener : listeners.value()) {
                try {
                    Constructor<? extends Listener> con = listener.getConstructor();
                    con.setAccessible(true);
                    Listener listen = con.newInstance();

                    Bukkit.getPluginManager().registerEvents(listen, DuckTape.get());
                    listenerClasses.add(listen);
                    DuckTape.debug(listener.getTypeName() + " registered listener.");
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                    DuckTape.debug(e, true);
                }
            }

            this.listeners.put((BukkitModule) event.getModule(), listenerClasses);
        }
    }

    @Subscribe
    public void onDisable(ModuleDisableEvent event) {
        Set<Listener> listenerClasses = listeners.get(event.getModule());

        listenerClasses.forEach(HandlerList::unregisterAll);
    }
}
