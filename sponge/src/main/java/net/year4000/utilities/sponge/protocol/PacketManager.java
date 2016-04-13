package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.sponge.protocol.proxy.ProxyEntityPlayerMP;
import org.spongepowered.api.entity.living.player.Player;

/** The packet manager that inject packets into the netty pipeline */
public class PacketManager implements Packets {
    /** The implementation of sending a custom packet to the player */
    @Override
    public void sendPacket(Player player, Object packet) {
        ProxyEntityPlayerMP entityPlayer = ProxyEntityPlayerMP.of(player);
        entityPlayer.netHandlerPlayServer().networkManager().channel().writeAndFlush(packet);
    }
}
