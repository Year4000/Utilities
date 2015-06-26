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
public class API {
    public static final String BASE_URL = "https://api.year4000.net/";
    public static final String ACCOUNTS_PATH = "accounts";
    public static final String SERVERS_PATH = "servers";
    public static final String PLAYER_COUNT_PATH = "player-count";
    public static final Type SERVERS_TYPE = new TypeToken<Map<String, ServerJson>>() {}.getType();
    private String key = null;

    /** The base url builder for creating API urls */
    public URLBuilder api() {
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
            PlayerCountJson response = HttpFetcher.get(url.build(), PlayerCountJson.class);
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

    /** Get a custom route */
    public <R extends Route, T> R getRoute(Class<R> route, Class<T> type, String path) {
        URLBuilder url = api().addPath(path);
        return getRoute(route, type, url);
    }

    /** Get a custom route */
    public <R extends Route, T> R getRoute(Class<R> route, Class<T> type, URLBuilder url) {
        try {
            T response = HttpFetcher.get(url.build(), type);
            return Route.generate(route, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get a custom route */
    public <R extends Route> R getRoute(Class<R> route, Type type, String path) {
        URLBuilder url = api().addPath(path);
        return getRoute(route, type, url);
    }

    /** Get a custom route */
    public <R extends Route, T> R getRoute(Class<R> route, Type type, URLBuilder url) {
        try {
            T response = HttpFetcher.get(url.build(), type);
            return Route.generate(route, response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Get a custom route async */
    public <R extends Route, T> void getRouteAsync(Class<R> route, Class<T> type, String path, Callback<R> callback) {
        URLBuilder url = api().addPath(path);
        getRouteAsync(route, type, url, callback);
    }

    /** Get a custom route async */
    public <R extends Route, T> void getRouteAsync(Class<R> route, Class<T> type, URLBuilder url, Callback<R> callback) {
        HttpFetcher.get(url.build(), type, (response, error) -> {
            R routeObject = error != null ? null : Route.generate(route, response);
            callback.callback(routeObject, error);
        });
    }

    /** Get a custom route async */
    public <R extends Route> void getRouteAsync(Class<R> route, Type type, String path, Callback<R> callback) {
        URLBuilder url = api().addPath(path);
        getRouteAsync(route, type, url, callback);
    }

    /** Get a custom route async */
    public <R extends Route> void getRouteAsync(Class<R> route, Type type, URLBuilder url, Callback<R> callback) {
        HttpFetcher.get(url.build(), type, (response, error) -> {
            R routeObject = error != null ? null : Route.generate(route, response);
            callback.callback(routeObject, error);
        });
    }
}