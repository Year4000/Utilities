package net.year4000.ducktape.bungee;

import net.year4000.utilities.locale.URLLocaleManager;

public class MessageManager extends URLLocaleManager {
    private static MessageManager inst = null;
    private static String url = Settings.get().getUrl();

    public MessageManager() {
        super(DuckTape.get().getLog(), url, parseJson(url + LOCALES_JSON));
    }

    public static MessageManager get() {
        if (inst == null) {
            inst = new MessageManager();
        }

        return inst;
    }
}
