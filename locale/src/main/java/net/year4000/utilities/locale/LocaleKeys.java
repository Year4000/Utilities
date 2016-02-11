package net.year4000.utilities.locale;

import java.util.function.Function;

/**
 * An interface used for Enums or Classes that represents a locale key.
 * @param <A> The actor to grab the locale for.
 */
public interface LocaleKeys<A> extends Function<A, LocaleUtil> {
    /** Translate the object to the specific string */
    default String get(A actor, Object... args) {
        if (this instanceof Enum) {
            return apply(actor).get(Enum.class.cast(this), args);
        }
        else {
            return apply(actor).get(toString(), args);
        }
    }

    /** Translate the object to the specific string */
    default String get(Object... args) {
        return get(null, args);
    }
}
