package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.Invoke;
import net.year4000.utilities.reflection.Proxied;
import net.year4000.utilities.reflection.Static;

@Proxied("net.minecraft.network.EnumPacketDirection")
public interface ProxyEnumPacketDirection {
    /** Get the static instance of this */
    @Static
    static ProxyEnumPacketDirection get() {
        return Gateways.proxy(ProxyEnumPacketDirection.class);
    }

    // enum

    @Invoke
    @Static
    Object[] values();

    /** Get the enum value by the index */
    @Static
    default Object value(int index){
        Object[] values = values();
        Conditions.inRange(index, -1, values.length);
        return values[index];
    }
}