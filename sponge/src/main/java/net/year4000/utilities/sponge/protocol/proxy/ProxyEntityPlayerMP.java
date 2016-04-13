package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Bridge;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.Getter;
import net.year4000.utilities.reflection.Proxied;
import org.spongepowered.api.entity.living.player.Player;

@Proxied("net.minecraft.entity.player.EntityPlayerMP")
public interface ProxyEntityPlayerMP {
    /** Create the proxy of the player */
    static ProxyEntityPlayerMP of(Player player) {
        Conditions.nonNull(player, "player");
        return Gateways.proxy(ProxyEntityPlayerMP.class, player);
    }

    /** Grabs the current instance of the NetHandlerPlayServer */
    @Getter("field_71135_a")
    @Bridge(ProxyNetHandlerPlayServer.class)
    ProxyNetHandlerPlayServer netHandlerPlayServer();
}
