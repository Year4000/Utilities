package net.year4000.utilities.api.route.playerlist;

import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.PlayerList;

public class PlayerListRoute extends Route<PlayerList> {

    String server;
    boolean separate;
    boolean uuids;

    public PlayerListRoute(String server, boolean separate, boolean uuids) {
        super(APIManager.getRouteUrl("player_list"), PlayerList.class);
        this.server = server;
        this.separate = separate;
        this.uuids = uuids;
    }

    public PlayerListRoute(boolean separate, boolean uuids) {
        this ("", separate, uuids);
    }

    public PlayerListRoute(String server, boolean uuids) {
        this(server, false, uuids);
    }

    public PlayerListRoute(boolean separate) {
        this("", separate, false);
    }

    public PlayerListRoute(String server) {
        this(server, false, false);
    }

    public PlayerListRoute() {
        this(false);
    }

    @Override
    public String getExtraData() {
        return (server != "" ? "/" + server : "") + (separate ? (uuids ? "?separate&uuid" : "?separate") : (uuids ? "?uuid" : ""));
    }
}
