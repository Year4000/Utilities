package net.year4000.utilities.sponge.protocol;

import com.google.common.collect.Maps;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.sponge.protocol.proxy.ProxyEnumConnectionState;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

/** Our packet class that bridges the minecraft type with our type */
public class Packet {
    private static final ConcurrentMap<PacketType, Class<?>> cache = Maps.newConcurrentMap();
    private PacketType type;
    private Object injectedPacket;

    public Packet(PacketType type) {
        this.type = Conditions.nonNull(type, "type");
        this.injectedPacket = Conditions.nonNull(inject(), "inject()");
    }

    /** Find the packet type and cache it */
    private Object inject() {
        return Reflections.instance(mcPacketClass()).getOrThrow("mcPacketClass()");
    }

    /** The type this packet if for */
    public PacketType packetType() {
        return type;
    }

    /** Get the class type of the minecraft packet */
    public Class<?> mcPacketClass() {
        return cache.computeIfAbsent(type, type -> {
            ProxyEnumConnectionState state = ProxyEnumConnectionState.get();
            ProxyEnumConnectionState instance = state.value(type.state());
            return instance.packet(type);
        });
    }

    /** The instance of the injected packet */
    public Object mcPacket() {
        return injectedPacket;
    }

    /** Transmute the packet's data */
    public void transmute(BiConsumer<Class<?>, Object> consumer) {
        consumer.accept(mcPacketClass(), mcPacket());
    }

    /** Inject the map of fields into the instance */
    public void inject(Map<String, Object> fields) {
        transmute((clazz, inst) -> fields.forEach((key, value) -> Reflections.field(clazz, inst, key, value)));
    }
}
