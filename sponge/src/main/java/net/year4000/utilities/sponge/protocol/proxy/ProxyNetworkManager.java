package net.year4000.utilities.sponge.protocol.proxy;

import io.netty.channel.Channel;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Proxied;

@Proxied("net.minecraft.network.NetworkManager")
public interface ProxyNetworkManager {
    /** The netty channel that is attached to the network manager */
    @Getter(signature = "Lio/netty/channel/Channel;")
    Channel channel();
}
