/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk.routes.accounts;

import com.google.gson.JsonObject;
import net.year4000.utilities.sdk.routes.Route;

public class AccountRoute extends Route<JsonObject> {
    private AccountRoute() {}

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
