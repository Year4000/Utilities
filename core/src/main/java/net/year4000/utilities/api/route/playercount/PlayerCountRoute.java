package net.year4000.utilities.api.route.playercount;

import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.PlayerCount;

public class PlayerCountRoute extends Route<PlayerCount> {
    public PlayerCountRoute() {
        super(APIManager.getRouteUrl("player_count"), PlayerCount.class);
    }
}
