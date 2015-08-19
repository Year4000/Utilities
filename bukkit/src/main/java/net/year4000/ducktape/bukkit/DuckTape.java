package net.year4000.ducktape.bukkit;

import lombok.Getter;
import net.year4000.ducktape.bukkit.module.BukkitModule;
import net.year4000.ducktape.bukkit.module.ModuleManagerListener;
import net.year4000.ducktape.loader.ClassFolderModuleLoader;
import net.year4000.ducktape.loader.ClassModuleLoader;
import net.year4000.ducktape.loader.GroovyModuleLoader;
import net.year4000.ducktape.loader.JarFileModuleLoader;
import net.year4000.ducktape.module.ModuleManager;
import net.year4000.utilities.LogUtil;
import net.year4000.utilities.bukkit.BukkitPlugin;

public class DuckTape extends BukkitPlugin {
    private static DuckTape inst;
    @Getter
    private ModuleManager<BukkitModule> modules = new ModuleManager<>(getLog());

    @Override
    public void onLoad() {
        inst = this;
        log = new LogUtil();

        modules.getEventBus().register(new ModuleManagerListener());

        // register internals
        try {
            new ClassModuleLoader(modules).add(DuckTapeModule.class);
        }
        catch (Exception error) {
            log(error, false);
        }

        // register jar files
        try {
            new JarFileModuleLoader(modules).load(Settings.get().getModulesDataFolder());
        }
        catch (Exception error) {
            log(error, false);
        }

        // register class files
        try {
            new ClassFolderModuleLoader(modules).load(Settings.get().getModulesDataFolder());
        }
        catch (Exception error) {
            log(error, false);
        }

        // register groovy script files
        try {
            new GroovyModuleLoader(modules).load(Settings.get().getModulesDataFolder());
        }
        catch (Exception error) {
            log(error, false);
        }

        // register modules
        modules.loadModules();
    }

    @Override
    public void onEnable() {
        log = new LogUtil(getLogger());
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
