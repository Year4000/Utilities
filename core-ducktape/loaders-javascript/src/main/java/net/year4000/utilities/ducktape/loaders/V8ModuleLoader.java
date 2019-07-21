/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import net.year4000.utilities.ducktape.ModuleInitException;

import java.util.Collection;
import java.util.Collections;

public class V8ModuleLoader implements ModuleLoader {
    @Override
    public Collection<Class<?>> load() throws ModuleInitException {
        return Collections.emptySet();
    }
}
