package net.year4000.utilities.api.routedata;

import lombok.Data;

import java.util.UUID;

@Data
public class MinecraftPlayer {
    /**
     * In-game name
     */
    final String username;
    /**
     * Unique identifier
     */
    final UUID uuid;
}
