package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.reflection.Getter;
import net.year4000.utilities.reflection.Invoke;
import net.year4000.utilities.reflection.Proxied;
import net.year4000.utilities.sponge.protocol.PacketType;

@Proxied("net.minecraft.network.EnumConnectionState")
public interface ProxyEnumConnectionState {
    @Getter
    Object[] states();

    @Invoke
    Object getPacket(Object direction, int id);

    /** Use magic to get the object of the packet */
    default Object convertPacket(PacketType type) {
        return getPacket(states()[type.bounded()], type.id());
    }
}
