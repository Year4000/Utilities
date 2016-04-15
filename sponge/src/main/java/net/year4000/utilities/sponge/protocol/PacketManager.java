package net.year4000.utilities.sponge.protocol;

import io.netty.channel.Channel;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEntityPlayerMP;
import org.spongepowered.api.entity.living.player.Player;

/** The packet manager that inject packets into the netty pipeline */
public class PacketManager implements Packets {

    /** The implementation of sending a custom packet to the player */
    @Override
    public void sendPacket(Player player, Packet packet) {
        ProxyEntityPlayerMP entityPlayer = ProxyEntityPlayerMP.of(player);
        Channel channel = entityPlayer.netHandlerPlayServer().networkManager().channel();

        // Inject our own handler that will transmute our packets
        if (channel.pipeline().get("my_handler") == null) {
            channel.pipeline().addFirst("my_handler", new PipelineHandles.PacketSerializer());
        }

        // We are writing our own packet type that will transmute into the type minecraft uses
        channel.writeAndFlush(packet);
    }
}
