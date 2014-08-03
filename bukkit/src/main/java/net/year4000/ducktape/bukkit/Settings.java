package net.year4000.ducktape.bukkit;

import net.year4000.ducktape.GlobalSettings;
import net.year4000.utilities.config.InvalidConfigurationException;

import java.io.File;

public class Settings extends GlobalSettings {
    private static Settings inst = null;

    private Settings() {
        try {
            init(new File(DuckTape.get().getDataFolder(), "config.yml"));
        } catch (InvalidConfigurationException e) {
            DuckTape.debug(e, false);
        }
    }

    public static Settings get() {
        if (inst == null) {
            inst = new Settings();
        }

        return inst;
    }

    /** Get the data folder for the modules */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getModulesFolder() {
        File file = new File(DuckTape.get().getDataFolder(), getModulesData());

        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    /** Get the data folder for the modules */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getModulesDataFolder() {
        File file = new File(DuckTape.get().getDataFolder(), getModulesPath());

        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }
}
