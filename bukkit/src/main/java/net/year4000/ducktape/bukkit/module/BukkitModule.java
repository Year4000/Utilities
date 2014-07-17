package net.year4000.ducktape.bukkit.module;

import net.year4000.ducktape.bukkit.DuckTape;
import net.year4000.ducktape.bukkit.Settings;
import net.year4000.ducktape.core.module.AbstractModule;

import java.io.File;

public class BukkitModule extends AbstractModule {
    @Override
    public File getDataFolder() {
        return new File(Settings.get().getModulesFolder(), getModuleInfo().name());
    }

    @Override
    public void registerCommand(Class<?> clazz) {
        DuckTape.get().registerCommand(clazz);
    }

    public static void log(String message, Object... args) {
        DuckTape.debug(message, args);
    }

    public static void debug(String message, Object... args) {
        DuckTape.debug(message, args);
    }

    public static void debug(Exception e, boolean simple) {
        DuckTape.debug(e, simple);
    }

    public static void log(Exception e, boolean simple) {
        DuckTape.log(e, simple);
    }
}
