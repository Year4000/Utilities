/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.year4000.utilities.ducktape.settings.GsonSaveLoadProvider;
import net.year4000.utilities.ducktape.settings.SaveLoad;
import net.year4000.utilities.ducktape.settings.Settings;
import net.year4000.utilities.reflection.Reflections;

public class SettingsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SaveLoad.class).to(GsonSaveLoadProvider.class);

        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                //System.out.println("hear: " + type);

                // inject the settings
                if (type.getRawType() == Settings.class) {
                    //System.out.println("load settings: " + type);
                    String typeString = type.toString();
                    String genericType = typeString.substring(typeString.indexOf("<") + 1, typeString.indexOf(">"));
                    Class<?> settingsClass = Reflections.clazz(genericType).getOrThrow();
                    //System.out.println("foundSettingsClass: " + settingsClass);

                    // when members are injected
                    encounter.register((MembersInjector<I>) instance -> {
                        Gson gson = new GsonBuilder().create();

                        // create instance right now, later have something else handle it
                        //((Settings) instance).instance = Reflections.instance(settingsClass).getOrThrow();
                        //System.out.println("MembersInjector settings: " + instance);

                    });
                    // after members has been injected
                    encounter.register((InjectionListener<I>) instance -> {
                        // todo load settings here
                        //System.out.println("InjectionListener settings: " + instance);
                    });
                }
            }
        });
    }
}
