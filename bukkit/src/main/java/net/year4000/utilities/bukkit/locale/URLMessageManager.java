package net.year4000.utilities.bukkit.locale;

import net.year4000.utilities.bukkit.Utilities;
import net.year4000.utilities.cache.QuickCache;
import net.year4000.utilities.locale.URLLocaleManager;

class URLMessageManager extends URLLocaleManager {
    private static QuickCache<URLMessageManager> inst = QuickCache.builder(URLMessageManager.class).build();
    private static final String LOCALE_URL = "https://raw.githubusercontent.com/Year4000/Locales/master/net/year4000/utilities/locales/";

    public URLMessageManager() {
        super(Utilities.getInst().getLog(), LOCALE_URL, parseJson(LOCALE_URL + LOCALES_JSON));
    }

    static URLMessageManager get() {
        return inst.get();
    }
}
