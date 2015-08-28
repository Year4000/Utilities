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

import java.util.function.Supplier;

public final class Constant<T> implements Accessor<T> {
    private T constant;
    private Supplier<T> supplier = () -> constant;

    private Constant(T var) {
        this.constant = var;
    }

    /** Get the specific constant */
    public T get() {
        return supplier.get();
    }

    /** Is the constant set to something */
    public boolean exists() {
        return constant != null;
    }

    /** A static method to return an instance of Variable */
    public static <T> Constant<T> var(T var, Supplier<T> getter) {
        Constant<T> constant = new Constant<>(var);

        if (getter != null) {
            constant.supplier = getter;
        }

        return constant;
    }

    /** A static method to return an instance of Variable */
    public static <T> Constant<T> var(T var) {
        return var(var, null);
    }
}
