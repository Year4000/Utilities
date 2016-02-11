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

package net.year4000.utilities.sdk.routes;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Route<T> {
    private static final Gson GSON = new Gson();
    protected T response;

    /** Generate the route based on its correct type */
    public static <R extends Route, T> R generate(Class<R> route, T response) {
        try {
            // Give us access to the default constructor
            Constructor<R> constructor = route.getDeclaredConstructor();
            constructor.setAccessible(true);
            R reply = constructor.newInstance();
            reply.response = response;
            return reply;
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the raw response from the route */
    public T getRawResponse() {
        return response;
    }

    @Override
    public String toString() {
        return GSON.toJson(response);
    }
}
