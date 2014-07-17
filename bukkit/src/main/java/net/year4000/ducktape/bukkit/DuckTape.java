package net.year4000.ducktape.bukkit;

import com.ewized.utilities.bukkit.BukkitPlugin;
import lombok.Getter;
import net.year4000.ducktape.bukkit.module.BukkitModule;
import net.year4000.ducktape.core.loader.ClassFolderModuleLoader;
import net.year4000.ducktape.core.loader.ClassModuleLoader;
import net.year4000.ducktape.core.loader.JarFileModuleLoader;
import net.year4000.ducktape.core.module.ModuleManager;

public class DuckTape extends BukkitPlugin {
    private static DuckTape inst;
    @Getter
    private ModuleManager<BukkitModule> modules = new ModuleManager<>(getLog());

    @Override
    public void onLoad() {
        inst = this;

        // register internals
        new ClassModuleLoader(modules).add(DuckTapeModule.class);

        // register jar files
        new JarFileModuleLoader(modules).load(Settings.get().getModulesFolder());

        // register class files
        new ClassFolderModuleLoader(modules).load(Settings.get().getModulesFolder());

        // register modules
        modules.loadModules();
    }

    @Override
    public void onEnable() {
        // enable modules
        modules.enableModules();
    }

    @Override
    public void onDisable() {
        // disable modules
        modules.disableModules();
    }

    public static DuckTape get() {
        return inst;
    }
}
