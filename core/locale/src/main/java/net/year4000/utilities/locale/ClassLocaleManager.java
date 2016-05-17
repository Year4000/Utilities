/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.locale;

import net.year4000.utilities.LogUtil;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unused")
public class ClassLocaleManager extends AbstractLocaleManager {
    private Class clazz;

    /**
     * Load the manager using this class as a reference point.
     *
     * @param path    The path to use.
     * @param locales The locale codes to use.
     */
    public ClassLocaleManager(String path, String... locales) {
        this(ClassLocaleManager.class, path, locales);
    }

    /**
     * Load the manager with resources inside the class.
     *
     * @param clazz   The class to use as a reference.
     * @param path    The path to use.
     * @param locales The locale codes to use.
     */
    public ClassLocaleManager(Class clazz, String path, String... locales) {
        super(path, locales);
        this.clazz = checkNotNull(clazz);
    }

    /**
     * Load the class files and provide a LogUtil
     *
     * @param log     The LogUtil to use when creating the locale manager.
     * @param path    The path to use.
     * @param locales The locale codes to use.
     */
    public ClassLocaleManager(LogUtil log, String path, String... locales) {
        super(path, locales, log);
    }

    /** Load all the locales that are in the folder */
    @Override
    public void loadLocales() {
        try {
            for (String locale : codes) {
                loadLocale(locale, clazz.getResourceAsStream(path + locale + EXTENSION));
            }
        }
        catch (NullPointerException e) {
            log.log(e, true);
        }
    }
}
