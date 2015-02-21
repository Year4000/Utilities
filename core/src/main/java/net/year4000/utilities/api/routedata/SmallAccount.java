package net.year4000.utilities.api.routedata;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmallAccount {
    @SerializedName("last_login")
    final long lastLogin;
    @SerializedName("first_login")
    final long firstLogin;
    final MinecraftPlayer minecraft;
}
