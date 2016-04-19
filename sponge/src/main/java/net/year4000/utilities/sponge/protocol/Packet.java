package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;

import java.util.Map;
import java.util.function.BiConsumer;

/** Our packet class that bridges the minecraft type with our type */
public class Packet {
    private PacketType type;
    private Object injectedPacket;

    /** Create a packet from the type and fetch a new instance of the injected packet*/
    public Packet(PacketType type) {
        this.type = Conditions.nonNull(type, "type");
        this.injectedPacket = Conditions.nonNull(inject(), "inject()");
    }

    /** Create a packet based on the object */
    public Packet(PacketType type, Object packet) {
        this.type = Conditions.nonNull(type, "type");
        this.injectedPacket = Conditions.nonNull(packet, "packet");
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
        return PacketTypes.fromType(type).getOrThrow();
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
