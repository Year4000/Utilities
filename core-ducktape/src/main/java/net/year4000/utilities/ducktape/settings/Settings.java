/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape.settings;

import com.google.inject.Inject;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.ducktape.module.Loader;

/** The settings wrapper that will load and save the settings from the settings provider */
public class Settings<T> implements Loader {
    @Inject private T instance;
    @Inject private SaveLoad saveLoad;


    /** Save the settings from the provider */
    public void save() {
        try {
            saveLoad.save(instance);
        } catch (SettingsException error) {
            ErrorReporter.builder(error)
                .buildAndReport(System.err);
        }
    }

    /** Load the settings instance with the config from the provider */
    public void load() {
        try {
            this.instance = saveLoad.load((Class<T>) this.instance.getClass());
        } catch (SettingsException error) {
            ErrorReporter.builder(error)
                .buildAndReport(System.err);
        }
    }

    public T instance() {
        return this.instance;
    }
}
