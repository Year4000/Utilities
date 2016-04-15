package net.year4000.utilities.sponge.protocol.proxy;

import com.google.common.collect.BiMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.Getter;
import net.year4000.utilities.reflection.Invoke;
import net.year4000.utilities.reflection.Proxied;
import net.year4000.utilities.sponge.protocol.PacketType;

import java.util.Map;

@Proxied("net.minecraft.network.EnumConnectionState")
public interface ProxyEnumConnectionState {
    @Getter("field_179247_h")
    Map<Object, BiMap<Integer, Class<?>>> classMap();

    static ProxyEnumConnectionState get() {
        return Gateways.proxy(ProxyEnumConnectionState.class);
    }

    @Invoke
    Object[] values();

    /** Get the enum value by the index */
    default ProxyEnumConnectionState value(int index){
        Object[] values = values();
        Conditions.inRange(index, -1, values.length);
        return Gateways.proxy(ProxyEnumConnectionState.class, values[index]);
    }

    /** Use magic to get the object of the packet */
    default Class<?> packet(PacketType type) {
        ProxyEnumPacketDirection direction = ProxyEnumPacketDirection.get();
        return classMap().get(direction.value(type.bounded())).get(type.id());
    }
}
