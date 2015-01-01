package net.year4000.utilities.bukkit.locale;

import net.year4000.utilities.bukkit.Utilities;
import net.year4000.utilities.locale.ClassLocaleManager;

class ClassMessageManager extends ClassLocaleManager {
    private static ClassMessageManager inst;

    private ClassMessageManager() {
        super(Utilities.getInst().getLog(), "/net/year4000/utilities/locales/", "en_US", "en_PT", "pt_PT", "pt_BR");
    }

    static ClassMessageManager get() {
        if (inst == null) {
            inst = new ClassMessageManager();
        }

        return inst;
    }
}
