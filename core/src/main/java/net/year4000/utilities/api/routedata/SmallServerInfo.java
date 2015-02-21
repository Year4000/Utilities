package net.year4000.utilities.api.routedata;

import lombok.Data;

@Data
public class SmallServerInfo {
    /**
     * Name of the server
     */
    final String name;
    /**
     * Hostname the server is hosted on
     * <b>Requires API key</b>
     * @see net.year4000.utilities.api.APIManager#setApikey(String key)
     */
    final String hostname;
    /**
     * Port the server is running on
     * <b>Requires API key</b>
     * @see net.year4000.utilities.api.APIManager#setApikey(String key)
     */
    final int port;
    /**
     * The servergroup the server is in
     */
    final ServerGroup group;
    /**
     * The server's online status
     */
    final ServerStatus status;

    public String getHostname() {
        return hostname == null ? "mc.year4000.net" : hostname;
    }

    public int getPort() {
        return port == 0 ? 25565 : port;
    }

    public ServerStatus getStatus() {
        return status == null ? new ServerStatus("Offline", new ServerOnlinePlayerCountWithPlayers(0, 0, new SmallPlayer[0])) : status;
    }
}
