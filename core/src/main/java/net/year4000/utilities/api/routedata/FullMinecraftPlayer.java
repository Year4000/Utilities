package net.year4000.utilities.api.routedata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.UUID;

@Data
public class FullMinecraftPlayer {
    /**
     * In-game name
     */
    final String username;
    /**
     * Unique identifier
     */
    final UUID uuid;
    /**
     * Older usernames the player had
     */
    final String[] usernames;
    /**
     * Minecraft-version the player uses
     */
    final int version;
    /**
     * The server the player is currently on
     */
    @SerializedName("current_server")
    final String currentServer;

    public String[] getUsernames() {
        return usernames == null ? new String[0] : usernames;
    }

    public String getCurrentServer() {
        return currentServer == null ? "Offline" : currentServer;
    }
}
