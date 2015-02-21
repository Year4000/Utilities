package net.year4000.utilities.api.routedata;

import lombok.Data;

@Data
public class ServerOnlinePlayerCountWithPlayers {
    /**
     * Ammount of players online
     */
    final int online;
    /**
     * Max ammount of players that can be online
     */
    final int max;
    /**
     * Online players
     */
    final SmallPlayer[] sample;
}
