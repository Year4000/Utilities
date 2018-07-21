/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.inject.Inject;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;

/** The settings wrapper that will load and save the settings from the settings provider */
public class Settings<T> {
    private final T instance;
    private SettingsProvider<T> settingsProvider;

    public Settings() {
        this.instance = null;
    } // for testing

    @Inject
    private Settings(T instance, SettingsProvider<T> settingsProvider) {
        this.instance = Conditions.nonNull(instance, "instance");
        this.settingsProvider = Conditions.nonNull(settingsProvider, "settingsProvider");
    }

    /** Save the setttings from the provider */
    public void save() {
        try {
            settingsProvider.save(instance);
        } catch (SettingsException error) {
            ErrorReporter.builder(error)
                .buildAndReport(System.err);
        }
    }

    /** Load the settings instance with the config from the provider */
    public void load() {
        try {
            settingsProvider.load(instance);
        } catch (SettingsException error) {
            ErrorReporter.builder(error)
                .buildAndReport(System.err);
        }
    }
}
