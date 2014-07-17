package net.year4000.ducktape.message;

import com.ewized.utilities.core.util.locale.LocaleManager;

public class MessageManager extends LocaleManager {
    private static MessageManager inst = null;
    protected static final String LOCALE_PATH = "/net/year4000/ducktape/locales/";
    private static String[] localeCodes = {"en_US", "pt_BR", "pt_PT"};

    private MessageManager(Class clazz) {
        super(clazz);
    }

    public static MessageManager get(Class clazz) {
        if (inst == null) {
            inst = new MessageManager(clazz);
        }

        return inst;
    }

    @Override
    protected void loadLocales(String path) {
        for (String locale : localeCodes) {
            loadLocale(locale, clazz.getResourceAsStream(LOCALE_PATH + locale + ".properties"));
        }
    }
}
