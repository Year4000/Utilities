/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.year4000.ducktape.bukkit.module;

import com.google.common.eventbus.Subscribe;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.bukkit.DuckTape;
import net.year4000.ducktape.module.AbstractModule;
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
    private Map<AbstractModule, Set<Listener>> listeners = new ConcurrentHashMap<>();

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
        }

        this.listeners.put(event.getModule(), listenerClasses);
    }

    @Subscribe
    public void onDisable(ModuleDisableEvent event) {
        Set<Listener> listenerClasses = listeners.get(event.getModule());

        listenerClasses.forEach(HandlerList::unregisterAll);
    }
}
