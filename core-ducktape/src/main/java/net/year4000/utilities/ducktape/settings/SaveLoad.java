/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.settings;

/** The provider that will use the feature to save and/or load settings */
public interface SaveLoad {
    /** The save */
    <T> void save(T settings) throws SettingsException;

    <T> T load(Class<T> settings) throws SettingsException;
}
