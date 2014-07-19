package net.year4000.ducktape.bungee;

import com.ewized.utilities.core.util.locale.URLLocaleManager;

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
