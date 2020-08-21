/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.settings;

import com.google.inject.Inject;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.ducktape.module.Loader;

/** The settings wrapper that will load and save the settings from the settings provider */
public class Settings<T> implements Loader {
    /** This is the instance of the settings, it will be init dynamic at the time this instance is injected */
    private T instance;
    @Inject private SaveLoad saveLoad;


    /** Save the settings from the provider */
    public void save() {
        try {
            this.saveLoad.save(instance);
        } catch (SettingsException error) {
            ErrorReporter.builder(error)
                .buildAndReport(System.err);
        }
    }

    /** Load the settings instance with the config from the provider */
    public void load() {
        try {
            this.instance = this.saveLoad.load((Class<T>) this.instance.getClass());
        } catch (SettingsException error) {
            ErrorReporter.builder(error)
                .buildAndReport(System.err);
        }
    }

    public T instance() {
        return this.instance;
    }
}
