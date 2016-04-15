package net.year4000.utilities.sponge.protocol.proxy;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.Getter;
import net.year4000.utilities.reflection.Proxied;
import net.year4000.utilities.sponge.protocol.PacketType;

@Proxied("net.minecraft.network.NetworkManager")
public interface ProxyNetworkManager {
    /** The netty channel that is attached to the network manager */
    @Getter("field_150746_k")
    Channel channel();

    @Getter
    AttributeKey<Object> keys();

    /** Generate the packet */
    default Object packet(PacketType type) {
        Object object = channel().attr(keys()).get();
        ProxyEnumConnectionState state = Gateways.proxy(ProxyEnumConnectionState.class, object);
        return state.convertPacket(type);
    }
}
