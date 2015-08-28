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

package net.year4000.utilities.variables;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Variable<T> implements Accessor<T>, Mutator<T> {
    private T variable;
    private Consumer<T> consumer = var -> variable = var;
    private Supplier<T> supplier = () -> variable;

    private Variable(T var) {
        set(var);
    }

    /** Get the specific variable */
    public T get() {
        return supplier.get();
    }

    /** Set the specific variable */
    public void set(T var) {
        consumer.accept(var);
    }

    /** Is the variable set to something */
    public boolean exists() {
        return variable != null;
    }

    /** Clear the reference of the variable */
    public void clear() {
        set(null);
    }

    /** A static method to return an instance of Variable */
    public static <T> Variable<T> var(T var, Supplier<T> getter, Consumer<T> setter) {
        Variable<T> variable = new Variable<>(var);

        if (getter != null) {
            variable.supplier = getter;
        }

        if (setter != null) {
            variable.consumer = setter;
        }

        return variable;
    }

    /** A static method to return an instance of Variable */
    public static <T> Variable<T> var(T var, Consumer<T> setter) {
        return var(var, null, setter);
    }

    /** A static method to return an instance of Variable */
    public static <T> Variable<T> var(T var, Supplier<T> getter) {
        return var(var, getter, null);
    }

    /** A static method to return an instance of Variable */
    public static <T> Variable<T> var(T var) {
        return var(var, null, null);
    }
}
