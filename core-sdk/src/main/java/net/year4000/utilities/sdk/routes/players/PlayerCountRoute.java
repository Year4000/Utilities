/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk.routes.players;

import com.google.common.collect.ImmutableMap;
import net.year4000.utilities.sdk.routes.Route;

public class PlayerCountRoute extends Route<PlayerCountJson> {
    private PlayerCountRoute() {}

    /** Get the player count of the network */
    public PlayerCountJson.Count getNetworkPlayerCount() {
        return response.getNetwork();
    }

    /** Get the player count of the network */
    public ImmutableMap<String, PlayerCountJson.Count> getGroupsPlayerCount() {
        return new ImmutableMap.Builder<String, PlayerCountJson.Count>().putAll(response.getGroups()).build();
    }

    /** Get the player count of the specific server */
    public PlayerCountJson.Count getGroupPlayerCount(String group) {
        return response.getGroups().get(group);
    }

    /** Get the player count of the network */
    public ImmutableMap<String, PlayerCountJson.Count> getServersPlayerCount() {
        return new ImmutableMap.Builder<String, PlayerCountJson.Count>().putAll(response.getServers()).build();
    }

    /** Get the player count of the specific server */
    public PlayerCountJson.Count getServerPlayerCount(String server) {
        return response.getServers().get(server);
    }
}
