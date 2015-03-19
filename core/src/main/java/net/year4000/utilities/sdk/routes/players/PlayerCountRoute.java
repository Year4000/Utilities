package net.year4000.utilities.sdk.routes.players;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.year4000.utilities.sdk.routes.Route;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerCountRoute extends Route<PlayerCountJson> {
    /** Get the player count of the network */
    public PlayerCountJson.Count getNetworkPlayerCount() {
        return response.getNetwork();
    }

    /** Get the player count of the network */
    public ImmutableMap<String, PlayerCountJson.Count> getGroupsPlayerCount() {
        return new ImmutableMap.Builder<String, PlayerCountJson.Count>().putAll(response.getGroups()).build();
    }

    /** Get the player count of the specific server */
    public PlayerCountJson.Count getGroupPlayerCount(@NonNull String group) {
        return response.getGroups().get(group);
    }

    /** Get the player count of the network */
    public ImmutableMap<String, PlayerCountJson.Count> getServersPlayerCount() {
        return new ImmutableMap.Builder<String, PlayerCountJson.Count>().putAll(response.getServers()).build();
    }

    /** Get the player count of the specific server */
    public PlayerCountJson.Count getServerPlayerCount(@NonNull String server) {
        return response.getServers().get(server);
    }
}
