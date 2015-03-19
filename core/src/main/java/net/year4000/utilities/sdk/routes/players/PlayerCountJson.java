package net.year4000.utilities.sdk.routes.players;

import lombok.Value;

import java.util.Map;

@Value
public class PlayerCountJson {
    private Count network;
    private Map<String, Count> groups;
    private Map<String, Count> servers;

    @Value
    public static class Count {
        private int online;
        private int max;
    }
}
