/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.locale;

@SuppressWarnings("unused")
public interface Translatable<R> {
    /** Translate to the specific locale with formatting */
    R get(String key, Object... args);

    /** Translate to the specific locale with formatting */
    default R get(Enum<?> key, Object... args) {
        String formatted = key.name().toLowerCase()
            .replace("__", "--") // Double underscore represent a dash
            .replace("_", ".") // Underscore represents a dot
            .replace("--", "_") // Dash represents an underscore
            ;

        return get(formatted, args);
    }
}
