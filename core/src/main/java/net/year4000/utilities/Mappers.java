/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import net.year4000.utilities.utils.UtilityConstructError;

import java.util.function.Function;

public final class Mappers {

    private Mappers() {
        UtilityConstructError.raise();
    }

    /** Simple cast to map a template ? to Object */
    public static Function<? super Object, Object> object() {
        return arg -> (Object) arg;
    }
}
