/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.year4000.utilities;

import java.util.Optional;

@FunctionalInterface
public interface Callback<T> {
    /** The class back method that is exposed to the user */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void callback(Optional<T> data, Optional<Throwable> error);

    /** Provide a wrapper for the callback values */
    default void callback(T data, Throwable error) {
        callback(Optional.of(data), Optional.of(error));
    }

    /** Only wrap the data and use empty for the error */
    default void callback(T data) {
        callback(Optional.of(data), Optional.empty());
    }

    /** Only wrap the error and use empty for the data */
    default void callback(Throwable error) {
        callback(Optional.empty(), Optional.of(error));
    }
}
