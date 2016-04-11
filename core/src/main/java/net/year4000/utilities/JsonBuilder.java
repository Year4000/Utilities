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
import net.year4000.utilities.value.Value;

public final class JsonBuilder {
    private Type type;
    private JsonElement element;
    private JsonBuilder parent;
    private Value<String> key = Value.empty();

    private JsonBuilder(Type type, JsonBuilder parent, Value<String> key) {
        this(type);
        this.key = Conditions.nonNull(key, "key");
        this.parent = Conditions.nonNull(parent, "parent");
    }

    private JsonBuilder(Type type) {
        this.type = Conditions.nonNull(type, "type");

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
                Conditions.checkState(type, Type.values());
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
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");
        Conditions.nonNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, Number value) {
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");
        Conditions.nonNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, Character value) {
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");
        Conditions.nonNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add a property to object */
    public JsonBuilder addProperty(String key, Boolean value) {
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");
        Conditions.nonNull(value, "value");

        element.getAsJsonObject().addProperty(key, value);

        return this;
    }

    /** Add JsonObject to Object */
    public JsonBuilder addProperty(String key, JsonObject value) {
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");
        Conditions.nonNull(value, "value");

        element.getAsJsonObject().add(key, value);

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(Number value) {
        Conditions.checkState(type, Type.ARRAY);
        Conditions.nonNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(Boolean value) {
        Conditions.checkState(type, Type.ARRAY);
        Conditions.nonNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(String value) {
        Conditions.checkState(type, Type.ARRAY);
        Conditions.nonNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add a property to array */
    public JsonBuilder addValue(Character value) {
        Conditions.checkState(type, Type.ARRAY);
        Conditions.nonNull(value, "value");

        element.getAsJsonArray().add(new JsonPrimitive(value));

        return this;
    }

    /** Add JsonObject to Array */
    public JsonBuilder addValue(JsonObject value) {
        Conditions.checkState(type, Type.ARRAY);
        Conditions.nonNull(value, "value");

        element.getAsJsonArray().add(value);

        return this;
    }

    /** Internal JsonObject add */
    private JsonBuilder add(Type type, Value<String> name) {
        return new JsonBuilder(type, this, name);
    }

    /** Add JsonArray to object */
    public JsonBuilder addJsonArray(String key) {
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");

        return add(Type.ARRAY, Value.of(key));
    }

    /** Add JsonArray to Array */
    public JsonBuilder addJsonArray() {
        Conditions.checkState(type, Type.ARRAY);

        return add(Type.ARRAY, Value.empty());
    }

    /** Add JsonObject to object */
    public JsonBuilder addJsonObject(String key) {
        Conditions.checkState(type, Type.OBJECT);
        Conditions.nonNull(key, "key");

        return add(Type.OBJECT, Value.of(key));
    }

    /** Add JsonObject to Array */
    public JsonBuilder addJsonObject() {
        Conditions.checkState(type, Type.ARRAY);

        return add(Type.OBJECT, Value.empty());
    }

    public JsonBuilder parent() {
        Conditions.nonNull(parent, "parent");
        Conditions.nonNull(parent.element, "element");

        switch (parent.type) {
            case OBJECT:
                parent.element.getAsJsonObject().add(key.getOrElse("null"), element);
                break;
            case ARRAY:
                parent.element.getAsJsonArray().add(element);
                break;
            default:
                Conditions.checkState(parent.type, Type.OBJECT, Type.ARRAY);
                return null;
        }

        return parent;
    }

    /** Turn this JsonBuilder into JsonObject */
    public JsonObject toJsonObject() {
        Conditions.checkState(type, Type.OBJECT);
        return element.getAsJsonObject();
    }

    /** Turn this JsonBuilder into JsonArray */
    public JsonArray toJsonArray() {
        Conditions.checkState(type, Type.ARRAY);
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
                return Conditions.checkState(type, Type.OBJECT, Type.ARRAY).toString();
        }
    }

    private enum Type {OBJECT, ARRAY, NULL}
}
