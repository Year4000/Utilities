package net.year4000.utilities.api.routedata;

import lombok.Data;

@Data
public class ServerVersion {
    /**
     * Version name
     */
    String name;
    /**
     * Protocol version
     */
    int protocol;
}
