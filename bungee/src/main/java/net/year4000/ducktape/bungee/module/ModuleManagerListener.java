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

package net.year4000.ducktape.bungee.module;

import com.google.common.eventbus.Subscribe;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.bungee.DuckTape;
import net.year4000.ducktape.module.AbstractModule;

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
        }

        this.listeners.put(event.getModule(), listenerClasses);
    }

    @Subscribe
    public void onDisable(ModuleDisableEvent event) {
        Set<Listener> listenerClasses = listeners.get(event.getModule());

        listenerClasses.forEach(listener -> ProxyServer.getInstance().getPluginManager().unregisterListener(listener));
    }
}
