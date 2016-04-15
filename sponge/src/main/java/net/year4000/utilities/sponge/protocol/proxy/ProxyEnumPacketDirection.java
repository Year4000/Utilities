package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.Invoke;
import net.year4000.utilities.reflection.Proxied;

@Proxied("net.minecraft.network.EnumPacketDirection")
public interface ProxyEnumPacketDirection {
    static ProxyEnumPacketDirection get() {
        return Gateways.proxy(ProxyEnumPacketDirection.class);
    }

    @Invoke
    Object[] values();

    /** Get the enum value by the index */
    default Object value(int index){
        Object[] values = values();
        Conditions.inRange(index, -1, values.length);
        return values[index];
    }
}
