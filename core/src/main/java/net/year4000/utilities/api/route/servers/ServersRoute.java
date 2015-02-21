package net.year4000.utilities.api.route.servers;

import net.year4000.utilities.api.APIManager;
import net.year4000.utilities.api.Route;
import net.year4000.utilities.api.routedata.Servers;

public class ServersRoute extends Route<Servers> {
    public ServersRoute() {
        super(APIManager.getRouteUrl("servers"), Servers.class);
    }
}
