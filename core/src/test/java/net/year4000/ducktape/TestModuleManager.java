package net.year4000.ducktape;

import com.google.common.eventbus.Subscribe;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.api.events.ModuleLoadEvent;
import net.year4000.ducktape.core.loader.ClassModuleLoader;
import net.year4000.ducktape.core.module.AbstractModule;
import net.year4000.ducktape.core.module.ModuleManager;
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
