/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.ducktape.settings.SaveLoad;
import net.year4000.utilities.ducktape.settings.Settings;
import net.year4000.utilities.ducktape.settings.SettingsBase;
import net.year4000.utilities.reflection.Reflections;

public class SettingsModule extends AbstractModule {
    private final SaveLoad saveLoad;

    public SettingsModule(SaveLoad saveLoad) {
        this.saveLoad = Conditions.nonNull(saveLoad, "save load must exist");
    }

    @Override
    protected void configure() {
        bind(SaveLoad.class).toInstance(this.saveLoad);
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                // inject the settings
                if (type.getRawType() == Settings.class) {
                    //System.out.println("load settings: " + type);
                    String typeString = type.toString();
                    String genericType = typeString.substring(typeString.indexOf("<") + 1, typeString.indexOf(">"));
                    Class<?> settingsClass = Reflections.clazz(genericType).getOrThrow();
//<<<<<<< HEAD
//                    System.out.println("foundSettingsClass: " + settingsClass);
//                    String fileName = settingsClass.getAnnotation(SettingsBase.class).value();
//                    System.out.println("foundSettingsClass: " + fileName);
//
//=======
//                    //System.out.println("foundSettingsClass: " + settingsClass);
//
//                    // when members are injected
//                    encounter.register((MembersInjector<I>) instance -> {
//                        Gson gson = new GsonBuilder().create();
//
//                        // create instance right now, later have something else handle it
//                        //((Settings) instance).instance = Reflections.instance(settingsClass).getOrThrow();
//                        //System.out.println("MembersInjector settings: " + instance);
//
//                    });
//>>>>>>> More ducktape work
                    // after members has been injected
                    encounter.register((InjectionListener<I>) instance -> {

                        Settings<?> settings = (Settings<?>) instance;
                        // todo load settings here
                        System.out.println("InjectionListener settings: " + Utils.toString(settings));
                        Reflections.setter(settings, Reflections.field(Settings.class, "instance").get(), Reflections.instance(settingsClass).get());
                    });
                }
            }
        });
    }
}
