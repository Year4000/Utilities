/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.year4000.utilities.reflection.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class DucktapeModule extends AbstractModule {
    @Override
    protected void configure() {
        bindScope(ModPlugin.class, Scopes.SINGLETON);


        // create the dependency graph
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                for (Field field : type.getRawType().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        System.out.println("Found an injection instance, check if its a module dependency");

                        if (field.getDeclaringClass().isAnnotationPresent(ModPlugin.class)) {
                            System.out.println("Found module dependency");


                        }
                    }
                }
            }
        });


        // inject the settings, todo register proper settings provider
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                for (Field field : type.getRawType().getDeclaredFields()) {
                    if (field.getType() == Settings.class && field.isAnnotationPresent(InjectSettings.class)) {
                        ParameterizedType configType = (ParameterizedType) field.getGenericType();
                        encounter.register((InjectionListener<? super I>) instance -> {

                            Object settingsDefault = Reflections.instance((Class<?>) configType.getActualTypeArguments()[0]).getOrThrow("The class is not static or has no default constructor");
                            Reflections.setter(instance, field, new Settings<>((I) settingsDefault, new ClassSettingsProvider<>()));
                        });
                    }
                }
            }
        });
    }
}
