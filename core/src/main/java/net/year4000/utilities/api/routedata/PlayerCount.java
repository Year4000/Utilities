package net.year4000.utilities.api.routedata;

import lombok.Data;

import java.util.Map;

@Data
public class PlayerCount {
    /**
     * Player count on the whole network
     */
    final ServerOnlinePlayerCount network;
    /**
     * Player count per server group
     */
    final Map<String, ServerOnlinePlayerCount> groups;
    /**
     * Player count per-server
     */
    final Map<String, ServerOnlinePlayerCount> servers;
}
