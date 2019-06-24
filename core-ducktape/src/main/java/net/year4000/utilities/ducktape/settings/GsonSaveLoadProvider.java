/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSaveLoadProvider implements SaveLoad {
    private static final Gson GSON = new GsonBuilder().create();
    //@Inject private Path path;

    @Override
    public <T> void save(T settings) throws SettingsException {
        GSON.toJson(settings);
    }

    @Override
    public <T> T load(Class<T> settings) throws SettingsException {
        return GSON.fromJson("", settings);
    }
}
