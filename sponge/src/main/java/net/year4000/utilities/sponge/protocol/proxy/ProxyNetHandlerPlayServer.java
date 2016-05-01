package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.reflection.annotations.Bridge;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Proxied;

@Proxied("net.minecraft.network.NetHandlerPlayServer")
public interface ProxyNetHandlerPlayServer {
    /** Get the network manager */
    @Getter("field_147371_a")
    @Bridge(ProxyNetworkManager.class)
    ProxyNetworkManager networkManager();
}
