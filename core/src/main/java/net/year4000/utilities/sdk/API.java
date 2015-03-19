package net.year4000.utilities.sdk;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.year4000.utilities.Callback;
import net.year4000.utilities.URLBuilder;
import net.year4000.utilities.sdk.routes.Route;
import net.year4000.utilities.sdk.routes.accounts.AccountRoute;
import net.year4000.utilities.sdk.routes.players.PlayerCountJson;
import net.year4000.utilities.sdk.routes.players.PlayerCountRoute;
import net.year4000.utilities.sdk.routes.servers.ServerJson;
import net.year4000.utilities.sdk.routes.servers.ServersRoute;

import java.lang.reflect.Type;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public final class API {
    public static final String BASE_URL = "https://api.year4000.net/";
    public static final String ACCOUNTS_PATH = "accounts";
    public static final String SERVERS_PATH = "servers";
    public static final String PLAYER_COUNT_PATH = "player-count";
    private static final Type SERVERS_TYPE = new TypeToken<Map<String, ServerJson>>() {}.getType();
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
            URLBuilder url = api().addPath(ACCOUNTS_PATH).addPath(id);
            JsonObject response = HttpFetcher.get(url.build(), JsonObject.class);
            return Route.generate(AccountRoute.class, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the account with the id of the user */
    public void getAccountAsync(String id, Callback<AccountRoute> callback) {
        URLBuilder url = api().addPath(ACCOUNTS_PATH).addPath(id);
        HttpFetcher.get(url.build(), JsonObject.class, (response, error) -> {
            AccountRoute route = error != null ? null : Route.generate(AccountRoute.class, response);
            callback.callback(route, error);
        });
    }

    /** Get the servers route */
    public ServersRoute getServers() {
        try {
            URLBuilder url = api().addPath(SERVERS_PATH);
            Map<String, ServerJson> response = HttpFetcher.get(url.build(), SERVERS_TYPE);
            return Route.generate(ServersRoute.class, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the account with the id of the user */
    public void getServersAsync(Callback<ServersRoute> callback) {
        URLBuilder url = api().addPath(SERVERS_PATH);
        HttpFetcher.get(url.build(), SERVERS_TYPE, (response, error) -> {
            ServersRoute route = error != null ? null : Route.generate(ServersRoute.class, response);
            callback.callback(route, error);
        });
    }

    /** Get the player count route */
    public PlayerCountRoute getPlayerCount() {
        try {
            URLBuilder url = api().addPath(PLAYER_COUNT_PATH);
            PlayerCountRoute response = HttpFetcher.get(url.build(), PlayerCountJson.class);
            return Route.generate(PlayerCountRoute.class, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the player count async */
    public void getPlayerCountAsync(Callback<PlayerCountRoute> callback) {
        URLBuilder url = api().addPath(PLAYER_COUNT_PATH);
        HttpFetcher.get(url.build(), PlayerCountJson.class, (response, error) -> {
            PlayerCountRoute route = error != null ? null : Route.generate(PlayerCountRoute.class, response);
            callback.callback(route, error);
        });
    }
}
