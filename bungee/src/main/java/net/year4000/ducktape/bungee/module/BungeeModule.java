package net.year4000.ducktape.bungee.module;

import net.year4000.ducktape.bungee.DuckTape;
import net.year4000.ducktape.bungee.Settings;
import net.year4000.ducktape.core.module.AbstractModule;

import java.io.File;

public class BungeeModule extends AbstractModule {
    @Override
    public File getDataFolder() {
        return new File(Settings.get().getModulesFolder(), getModuleInfo().name());
    }

    @Override
    public void registerCommand(Class<?> clazz) {
        DuckTape.get().registerCommand(clazz);
    }
}
