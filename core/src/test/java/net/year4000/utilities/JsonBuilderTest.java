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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;

public class JsonBuilderTest {

    @Test
    public void testJsonObjects() throws Exception {
        // Test Json Objects
        JsonObject rawObject = new JsonObject();
        rawObject.addProperty("key", "value");
        JsonObject builderObject = JsonBuilder.newObject().addProperty("key", "value").toJsonObject();
        Assert.assertEquals(rawObject, builderObject);
        Assert.assertEquals(rawObject.toString(), builderObject.toString());
    }

    @Test
    public void testJsonArrays() throws Exception {
        // Test Json Arrays
        JsonArray rawArray = new JsonArray();
        rawArray.add(new JsonPrimitive("value"));
        JsonArray builderArray = JsonBuilder.newArray().addValue("value").toJsonArray();
        Assert.assertEquals(rawArray, builderArray);
        Assert.assertEquals(rawArray.toString(), builderArray.toString());
    }

    @Test
    public void testAdvance() throws Exception {
        // Test Advance
        JsonObject advanceObject = new JsonObject();
        advanceObject.addProperty("id", "id");
        JsonObject advanceObjectChild = new JsonObject();
        advanceObjectChild.addProperty("server", "year4000");
        advanceObjectChild.addProperty("ip", "mc.year4000.net");
        JsonArray advanceArrayChild = new JsonArray();
        advanceArrayChild.add(new JsonPrimitive("ewized"));
        advanceObjectChild.add("players", advanceArrayChild);
        advanceObject.add("data", advanceObjectChild);

        JsonObject advanceBuilder = JsonBuilder.newObject()
            .addProperty("id", "id")
            .addJsonObject("data")
            .addProperty("server", "year4000")
            .addProperty("ip", "mc.year4000.net")
            .addJsonArray("players")
            .addValue("ewized")
            .parent()
            .parent()
            .toJsonObject();

        Assert.assertEquals(advanceObject, advanceBuilder);
        Assert.assertEquals(advanceObject.toString(), advanceBuilder.toString());
    }
}
