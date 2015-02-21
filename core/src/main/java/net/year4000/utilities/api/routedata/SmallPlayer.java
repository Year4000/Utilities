package net.year4000.utilities.api.routedata;

import lombok.Data;

import java.util.UUID;

@Data
public class SmallPlayer {
    /**
     * In-game name
     */
    String name;
    /**
     * Unique identifier
     */
    UUID id;
}
