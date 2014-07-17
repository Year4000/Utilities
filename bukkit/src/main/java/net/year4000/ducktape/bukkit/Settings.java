package net.year4000.ducktape.bukkit;

import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.year4000.ducktape.core.GlobalSettings;

import java.io.File;

public class Settings extends GlobalSettings {
    private static Settings inst = null;

    private Settings() {
        try {
            CONFIG_HEADER = new String[] {"DuckTape Settings"};
            CONFIG_FILE = new File(DuckTape.get().getDataFolder(), "config.yml");
            init();
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
}
