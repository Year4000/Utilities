/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

/** The provider that will use the feature to save and/or load settings */
interface SettingsProvider<T> {
    /** The save */
    void save(T settings) throws SettingsException;

    void load(T settings) throws SettingsException;
}
