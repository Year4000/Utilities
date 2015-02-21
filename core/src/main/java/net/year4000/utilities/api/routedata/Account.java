package net.year4000.utilities.api.routedata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Account {
    /**
     * Player's ID in Y4K's database
     */
    final String id;
    /**
     * Latest locale the player used
     */
    final String locale;
    /**
     * Latest IP of the player
     * <b>Requires API key</b>
     * @see net.year4000.utilities.api.APIManager#setApikey(String key)
     */
    @SerializedName("last_ip")
    final String lastIp;
    /**
     * Last time the player logged on
     */
    @SerializedName("last_login")
    final long lastLogin;
    /**
     * First time the player logged on
     */
    @SerializedName("first_login")
    final long firstLogin;
    /**
     * Registered email of the player
     * <b>Requires API key</b>
     * @see net.year4000.utilities.api.APIManager#setApikey(String key)
     */
    final String email;
    /**
     * Rank prefix
     */
    final String badge;
    /**
     * Player's rank
     */
    final String rank;
    /**
     * Player's permissions
     * <b>Requires API key</b>
     * @see net.year4000.utilities.api.APIManager#setApikey(String key)
     */
    final String[] permissions;
    /**
     * Player's infractions
     */
    final Infraction[] infractions;
    /**
     * Info about the player's in-game character
     *
     * @return Info about the player's in-game character
     */
    final FullMinecraftPlayer minecraft;

    public Infraction[] getInfractions() {
        return infractions == null ? new Infraction[0] : infractions;
    }

    public boolean hasInfractions() {
        return getInfractions().length > 0;
    }
}
