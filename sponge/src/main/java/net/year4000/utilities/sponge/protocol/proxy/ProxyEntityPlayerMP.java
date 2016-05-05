package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.annotations.Bridge;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Proxied;
import net.year4000.utilities.sponge.protocol.Packet;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

@Proxied("net.minecraft.entity.player.EntityPlayerMP")
public interface ProxyEntityPlayerMP extends ProxyEntity {
    /** Create the proxy of the player */
    static ProxyEntityPlayerMP of(Player player) {
        Conditions.nonNull(player, "player");
        return Gateways.proxy(ProxyEntityPlayerMP.class, player);
    }

    /** Create the proxy of the User */
    static ProxyEntityPlayerMP of(User user) {
        Conditions.nonNull(user, "user");
        return Gateways.proxy(ProxyEntityPlayerMP.class, user);
    }

    /** Get the object that this proxy is using */
    Object $this();

    /** Grabs the current instance of the NetHandlerPlayServer */
    @Getter(signature = "Lnet/minecraft/network/NetHandlerPlayServer;")
    @Bridge(ProxyNetHandlerPlayServer.class)
    ProxyNetHandlerPlayServer netHandlerPlayServer();

    /** Send the packet for this player */
    default void sendPacket(Packet packet) {
        netHandlerPlayServer().networkManager().channel().writeAndFlush(packet);
    }
}
