package net.year4000.utilities.api.routedata;

import lombok.Data;

@Data
public class Infraction {
    /**
     * Type of the infraction
     */
    final String type;
    /**
     * Player that issued the infraction
     */
    final MinecraftPlayer judge;
    /**
     * Reason for the infraction
     */
    final String message;
    /**
     * When the infraction was issued
     */
    final long time;
}
