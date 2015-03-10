package net.year4000.utilities.api;

import com.google.gson.JsonObject;
import net.year4000.utilities.Callback;
import net.year4000.utilities.api.routes.Route;
import net.year4000.utilities.api.routes.accounts.AccountRoute;

public final class API {
    public static final String BASE_URL = "https://api.year4000.net/";

    /** Get the account with the id of the user */
    public static AccountRoute getAccount(String id) {
        try {
            JsonObject response = HttpFetcher.get(BASE_URL + "accounts/" + id, JsonObject.class);
            return Route.generate(AccountRoute.class, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the account with the id of the user */
    public static void getAccountAsync(String id, Callback<AccountRoute> callback) {
        JsonObject response = null;
        Throwable error = null;

        try {
            response = HttpFetcher.get(BASE_URL + "accounts/" + id, JsonObject.class);
        }
        catch (Exception e) {
            error = e;
        }
        finally {
            callback.callback(Route.generate(AccountRoute.class, response), error);
        }
    }
}
