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

import com.google.gson.*;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public final class JsonBuilder {
    public static JsonBuilder NULL = new JsonBuilder(Type.NULL);
    private enum Type {OBJECT, ARRAY, NULL}
    private Type type;
    private JsonElement element;
    private JsonBuilder parent;
    private Optional<String> key = Optional.empty();

    private JsonBuilder(Type type, JsonBuilder parent, Optional<String> key) {
        this(type);
        this.key = checkNotNull(key, "key");
        this.parent = checkNotNull(parent, "parent");
    }

    private JsonBuilder(Type type) {
        this.type = checkNotNull(type, "type");

        switch (type) {
            case OBJECT:
                this.element = new JsonObject();
                break;
            case ARRAY:
                this.element = new JsonArray();
                break;
            case NULL:
                this.element = JsonNull.INSTANCE;
                break;
            default:
                checkState(false, "invalid type");
        }
    }

    /** Construct a new Object */
    public static JsonBuilder newObject() {
        return new JsonBuilder(Type.OBJECT);
    }

    /** Construct a new Array */
    public static JsonBuilder newArray() {
        return new JsonBuilder(Type.ARRAY);
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, String value) {
        checkState(type == Type.OBJECT, "Must be of type Object");
        checkNotNull(key, "key");
        checkNotNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, Number value) {
        checkState(type == Type.OBJECT, "Must be of type Object");
        checkNotNull(key, "key");
        checkNotNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, Character value) {
        checkState(type == Type.OBJECT, "Must be of type Object");
        checkNotNull(key, "key");
        checkNotNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, Boolean value) {
        checkState(type == Type.OBJECT, "Must be of type Object");
        checkNotNull(key, "key");
        checkNotNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(Number value) {
        checkState(type == Type.ARRAY, "Must be of type Array");
        checkNotNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(Boolean value) {
        checkState(type == Type.ARRAY, "Must be of type Array");
        checkNotNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(String value) {
        checkState(type == Type.ARRAY, "Must be of type Array");
        checkNotNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(Character value) {
        checkState(type == Type.ARRAY, "Must be of type Array");
        checkNotNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Internal JsonObject add */
    private JsonBuilder add(Type type, Optional<String> name) {
        return new JsonBuilder(type, this, name);
    }

    /** Add JsonArray to object */
    public JsonBuilder addJsonArray(String key) {
        checkState(type == Type.OBJECT, "Must be of type Object");
        checkNotNull(key, "key");

        return add(Type.ARRAY, Optional.of(key));
    }

    /** Add JsonArray to Array */
    public JsonBuilder addJsonArray() {
        checkState(type == Type.ARRAY, "Must be of type Array");

        return add(Type.ARRAY, Optional.empty());
    }

    /** Add JsonObject to object */
    public JsonBuilder addJsonObject(String key) {
        checkState(type == Type.OBJECT, "Must be of type Object");
        checkNotNull(key, "key");

        return add(Type.OBJECT, Optional.of(key));
    }

    /** Add JsonObject to Array */
    public JsonBuilder addJsonObject() {
        checkState(type == Type.ARRAY, "Must be of type Array");

        return add(Type.OBJECT, Optional.empty());
    }

    public JsonBuilder parent() {
        checkNotNull(parent, "parent");
        checkNotNull(parent.element, "element");

        switch (parent.type) {
            case OBJECT:
                parent.element.getAsJsonObject().add(key.orElse("null"), element);
                break;
            case ARRAY:
                parent.element.getAsJsonArray().add(element);
                break;
            default:
                checkState(false, "invalid type");
                return NULL;
        }

        return parent;
    }

    /** Turn this JsonBuilder into JsonObject */
    public JsonObject toJsonObject() {
        checkState(type == Type.OBJECT, "Must be of type Object");
        return element.getAsJsonObject();
    }

    /** Turn this JsonBuilder into JsonArray */
    public JsonArray toJsonArray() {
        checkState(type == Type.ARRAY, "Must be of type Array");
        return element.getAsJsonArray();
    }

    /** Return a JsonString of this JsonBuilder */
    @Override
    public String toString() {
        switch (type) {
            case OBJECT:
                return toJsonObject().toString();
            case ARRAY:
                return toJsonArray().toString();
            default:
                checkState(false, "invalid type");
                return null;
        }
    }
}
