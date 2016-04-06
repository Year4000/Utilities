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

package net.year4000.utilities.sdk.routes.accounts;

import com.google.gson.JsonObject;
import net.year4000.utilities.sdk.routes.Route;

public class AccountRoute extends Route<JsonObject> {
    private AccountRoute() {
    }

    /** Get the username of this account */
    public String getRank() {
        return response.get("rank").getAsString();
    }

    /** Get the username of this account */
    public int getCredits() {
        return response.get("credits").getAsInt();
    }

    /** Get the username of this account */
    public String getUsername() {
        return response.get("minecraft").getAsJsonObject().get("username").getAsString();
    }

    /** Get the uuid of this account */
    public String getUUID() {
        return response.get("minecraft").getAsJsonObject().get("uuid").getAsString();
    }

    /** Get the last minecraft version of this account */
    public int getVersion() {
        return response.get("minecraft").getAsJsonObject().get("version").getAsInt();
    }
}
