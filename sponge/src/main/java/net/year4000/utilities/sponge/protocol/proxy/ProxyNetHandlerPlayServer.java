package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.reflection.annotations.Bridge;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Proxied;

@Proxied("net.minecraft.network.NetHandlerPlayServer")
public interface ProxyNetHandlerPlayServer {
    /** Get the object that this proxy is using */
    Object $this();

    /** Get the network manager */
    @Getter(signature = "Lnet/minecraft/network/NetworkManager;")
    @Bridge(ProxyNetworkManager.class)
    ProxyNetworkManager networkManager();
}
