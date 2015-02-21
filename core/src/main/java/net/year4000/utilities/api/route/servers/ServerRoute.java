package net.year4000.utilities.api.route.servers;

import lombok.Setter;
import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.Server;

public class ServerRoute extends Route<Server> {

    @Setter
    String server;

    public ServerRoute(String server) {
        super(APIManager.getRouteUrl("servers"), Server.class);
        this.server = server;
    }

    @Override
    public String getExtraData() {
        return "/" + server;
    }
}
