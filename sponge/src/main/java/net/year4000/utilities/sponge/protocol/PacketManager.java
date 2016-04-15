package net.year4000.utilities.sponge.protocol;

import io.netty.channel.Channel;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEntityPlayerMP;
import net.year4000.utilities.sponge.protocol.proxy.ProxyNetworkManager;
import org.spongepowered.api.entity.living.player.Player;

/** The packet manager that inject packets into the netty pipeline */
public class PacketManager implements Packets {

    /** The implementation of sending a custom packet to the player */
    @Override
    public void sendPacket(Player player, Packet packet) {
        ProxyEntityPlayerMP entityPlayer = ProxyEntityPlayerMP.of(player);
        ProxyNetworkManager network = entityPlayer.netHandlerPlayServer().networkManager();
        Object mcPacket = network.packet(packet.packetType());
        packet.inject(mcPacket);
        Channel channel = network.channel();

        // Inject our own handler that will transmute our packet
        if (channel.pipeline().get("my-encoder") == null) {
            channel.pipeline().addBefore("encoder", "my-encoder", new PipelineHandles.PacketSerializer());
        }

        // We are writing our own packet type that will transmute into the type minecraft uses
        channel.writeAndFlush(packet);
    }
}
