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

package net.year4000.ducktape;

import com.google.common.eventbus.Subscribe;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.api.events.ModuleLoadEvent;
import net.year4000.ducktape.loader.ClassModuleLoader;
import net.year4000.ducktape.module.AbstractModule;
import net.year4000.ducktape.module.ModuleManager;
import net.year4000.ducktape.modules.TestModule;
import org.junit.Test;

public class TestModuleManager {
    @Test
    public void testManager() {
        ModuleManager<AbstractModule> modules = new ModuleManager<>();
        modules.registerEvent(this);

        new ClassModuleLoader(modules)
            .add(TestModule.class);

        modules.loadModules();

        modules.enableModules();

        modules.disableModules();
    }

    @Subscribe
    public void onLoad(ModuleLoadEvent event) {
        System.out.println(event.getInfo().description());
    }

    @Subscribe
    public void onEnable(ModuleEnableEvent event) {
        System.out.println(event.getInfo().description());
    }

    @Subscribe
    public void onDisable(ModuleDisableEvent event) {
        System.out.println(event.getInfo().description());
    }
}
