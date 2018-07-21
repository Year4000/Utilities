/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class DucktapeModule extends AbstractModule {

    @Override
    protected void configure() {
        bindScope(ModPlugin.class, Scopes.SINGLETON);

        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                Class<?> clazz = type.getRawType();
            }
        });
    }
}
