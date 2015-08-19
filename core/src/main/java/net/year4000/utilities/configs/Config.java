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

package net.year4000.utilities.configs;

/**
 * Make it simple for a class to constructor the object from a JSON web page.
 * @param <T> The class that is being used from the config.
 */
@Deprecated
public abstract class Config<T> extends JsonConfig {

    /** Get the class object */
    protected T getInstance(Config self) {
        return (T) JsonConfig.getInstance(self);
    }
}
