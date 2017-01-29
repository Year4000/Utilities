/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.locale;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

public class LocaleTest {
    private static final Logger log = Logger.getLogger(LocaleTest.class.getName());

    private static void test(AbstractLocaleManager manager) {
        Assert.assertTrue(manager.getLocales().size() > 0);

        manager.getLocales().forEach((code, property) -> {
            Translatable<String> locale = (key, args) -> String.format(property.getProperty(key), args);
            Assert.assertEquals(locale.get("locale.code").toLowerCase(), code.toString());
        });
    }

    @Test
    @Ignore // todo Fix paths for test locales
    public void localeTests() throws Exception {
        test(new ClassLocaleManager(System.getProperty("test.locales.class"), "en_US", "fr_FR"));

        test(new ClassLocaleManager(LocaleTest.class, System.getProperty("test.locales.class"), "en_US", "fr_FR"));

        test(new URLLocaleManager(System.getProperty("test.locales.url"), "en_US", "pt_BR"));

        test(new URLLocaleManager(new URL(System.getProperty("test.locales.url")), "en_US", "pt_BR"));

        test(new FileLocaleManager(new File(System.getProperty("test.locales.file")), "en_US", "fr_FR"));

        test(new FileLocaleManager(System.getProperty("test.locales.file"), "en_US", "fr_FR"));

    }
}
