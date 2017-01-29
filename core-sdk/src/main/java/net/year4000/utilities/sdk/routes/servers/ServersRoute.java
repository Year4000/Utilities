/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk.routes.servers;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.year4000.utilities.sdk.routes.Route;

import java.util.Map;

public class ServersRoute extends Route<Map<String, ServerJson>> {
    private ServersRoute() {}

    /** Get a immutable version of the map */
    public ImmutableMap<String, ServerJson> getServersMap() {
        return new ImmutableMap.Builder<String, ServerJson>().putAll(response).build();
    }

    /** Get a collection of all the ServerJson objects returned by this route */
    public ImmutableCollection<ServerJson> getServersCollection() {
        return new ImmutableList.Builder<ServerJson>().addAll(response.values()).build();
    }

    /** Get a server object by its name */
    public ServerJson getServer(String server) {
        return response.get(server);
    }
}
