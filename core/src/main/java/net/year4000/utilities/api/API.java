package net.year4000.utilities.api;

import com.google.gson.JsonObject;
import net.year4000.utilities.Callback;
import net.year4000.utilities.URLBuilder;
import net.year4000.utilities.api.routes.Route;
import net.year4000.utilities.api.routes.accounts.AccountRoute;

public final class API {
    public static final String BASE_URL = "https://api.year4000.net/";
    private String key = null;

    /** The base url builder for creating API urls */
    private URLBuilder api() {
        URLBuilder api = URLBuilder.builder(BASE_URL);

        if (key != null) {
            api.addQuery("key", key);
        }

        return api;
    }

    /** Set this instances api key */
    public void setKey(String key) {
        this.key = key;
    }

    /** Remove this instances api key */
    public void removeKey() {
        setKey(null);
    }

    /** Get the account with the id of the user */
    public AccountRoute getAccount(String id) {
        try {
            URLBuilder url = api().addPath("accounts").addPath(id);
            JsonObject response = HttpFetcher.get(url.build(), JsonObject.class);
            return Route.generate(AccountRoute.class, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the account with the id of the user */
    public void getAccountAsync(String id, Callback<AccountRoute> callback) {
        JsonObject response = null;
        Throwable error = null;

        try {
            URLBuilder url = api().addPath("accounts").addPath(id);
            response = HttpFetcher.get(url.build(), JsonObject.class);
        }
        catch (Exception e) {
            error = e;
        }
        finally {
            callback.callback(Route.generate(AccountRoute.class, response), error);
        }
    }
}
