/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol.proxy;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Gateways;

import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Proxied;
import net.year4000.utilities.reflection.annotations.Static;

@Proxied("net.minecraft.network.EnumPacketDirection")
public interface ProxyEnumPacketDirection {
    /** Get the static instance of this */
    @Static
    static ProxyEnumPacketDirection get() {
        return Gateways.proxy(ProxyEnumPacketDirection.class);
    }

    /** Get the object that this proxy is using */
    Object $this();

    // enum

    @Invoke(signature = "()[Lnet/minecraft/network/EnumPacketDirection;")
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
