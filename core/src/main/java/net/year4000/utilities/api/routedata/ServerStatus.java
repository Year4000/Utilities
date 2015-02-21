package net.year4000.utilities.api.routedata;

import lombok.Data;

@Data
public class ServerStatus {
    /**
     * The server's MOTD
     */
    final String description;
    /**
     * Online players on the server
     */
    final ServerOnlinePlayerCountWithPlayers players;
}
