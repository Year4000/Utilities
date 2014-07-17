package net.year4000.ducktape.bungee;

import com.ewized.utilities.bungee.BungeePlugin;
import lombok.Getter;
import net.year4000.ducktape.bungee.module.BungeeModule;
import net.year4000.ducktape.core.loader.ClassFolderModuleLoader;
import net.year4000.ducktape.core.loader.ClassModuleLoader;
import net.year4000.ducktape.core.loader.JarFileModuleLoader;
import net.year4000.ducktape.core.module.ModuleManager;

public class DuckTape extends BungeePlugin {
    private static DuckTape inst;
    @Getter
    private ModuleManager<BungeeModule> modules = new ModuleManager<>();

    @Override
    public void onLoad() {
        inst = this;

        // register internals
        new ClassModuleLoader(modules).add(DuckTapeModule.class);

        // register jar files
        new JarFileModuleLoader(modules).load(Settings.get().getModulesFolder());

        // register class files
        new ClassFolderModuleLoader(modules).load(Settings.get().getModulesFolder());

        // load modules
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
