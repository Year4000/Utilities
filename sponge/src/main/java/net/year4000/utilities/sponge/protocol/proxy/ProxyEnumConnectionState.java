/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol.proxy;

import com.google.common.collect.BiMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.Gateways;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Proxied;
import net.year4000.utilities.reflection.annotations.Static;
import net.year4000.utilities.sponge.protocol.PacketType;

import java.util.Map;

@Proxied("net.minecraft.network.EnumConnectionState")
public interface ProxyEnumConnectionState {
    /** Get the proxy for the object */
    static ProxyEnumConnectionState of(Object object) {
        Conditions.nonNull(object, "object");
        return Gateways.proxy(ProxyEnumConnectionState.class, object);
    }

    /** Get the static version of this proxy */
    @Static
    static ProxyEnumConnectionState get() {
        return Gateways.proxy(ProxyEnumConnectionState.class);
    }

    /** Get the object that this proxy is using */
    Object $this();

    @Getter(signature = "Ljava/util/Map;", index = 1)
    Map<Object, BiMap<Integer, Class<?>>> classMap();

    /** Use magic to get the object of the packet */
    @Static
    default Class<?> packet(PacketType type) {
        ProxyEnumPacketDirection direction = ProxyEnumPacketDirection.get();
        return classMap().get(direction.value(type.bounded())).get(type.id());
    }

    // enum

    @Invoke(signature = "()[Lnet/minecraft/network/EnumConnectionState;")
    @Static
    Object[] values();

    /** Get the enum value by the index */
    @Static
    default ProxyEnumConnectionState value(int index){
        Object[] values = values();
        Conditions.inRange(index, -1, values.length);
        return of(values[index]);
    }
}
