/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.locale;

import java.util.Optional;
import java.util.function.Function;

/**
 * An interface used for Enums or Classes that represents a locale key.
 *
 * @param <A> The actor to grab the locale for.
 * @param <R> The return type.
 */
public interface LocaleKeys<A, R> extends Function<Optional<A>, Translatable<R>> {
    /** Translate the object to the specific string */
    default R get(Optional<A> actor, Object... args) {
        if (this instanceof Enum) {
            return apply(actor).get(Enum.class.cast(this), args);
        }
        else {
            return apply(actor).get(toString(), args);
        }
    }

    /** Translate the object to the specific string */
    default R get(A actor, Object... args) {
        return get(Optional.ofNullable(actor), args);
    }

    /** Translate the object to the specific string */
    default R get(Object... args) {
        return get(Optional.empty(), args);
    }
}
