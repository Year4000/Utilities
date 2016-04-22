package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiConsumer;

/** Our packet class that bridges the minecraft type with our type */
public class Packet {
    private PacketType type;
    private Class<?> clazz;
    private Object injectedPacket;

    /** Create a packet from the type and fetch a new instance of the injected packet*/
    public Packet(PacketType type) {
        this(type, PacketTypes.fromType(type).get(), null);
    }

    /** Create a packet based on the object */
    public Packet(PacketType type, Object packet) {
        this(type, PacketTypes.fromType(type).get(), packet);
    }

    /** Used to create the packet instance */
    Packet(PacketType type, Class<?> clazz, Object packet) {
        this.type = Conditions.nonNull(type, "type");
        this.clazz = Conditions.nonNull(clazz, "clazz");
        this.injectedPacket = (packet == null) ? Reflections.instance(clazz).getOrThrow() : packet;
    }

    /** The type this packet if for */
    public PacketType packetType() {
        return type;
    }

    /** Get the class type of the minecraft packet */
    public Class<?> mcPacketClass() {
        return clazz;
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
        transmute((clazz, inst) -> fields.forEach((key, value) -> Reflections.setter(clazz, inst, key, value)));
    }

    /** Inject data of the packet using the packet builder */
    public Injector injector() {
        return new Injector(this);
    }

    /** A injector that will inject the data into the object's instance */
    public static class Injector {
        private final Packet packet;
        private final int length;
        private final Object[] values;
        private int fieldCounter = 0;

        /** Create the values*/
        Injector(Packet packet) {
            this.packet = Conditions.nonNull(packet, "packet");
            this.length = packet.mcPacketClass().getDeclaredFields().length;
            this.values = new Object[length];
        }

        /** Allow skipping a ordered field */
        public Injector skip() {
            Conditions.isSmaller(fieldCounter++, length - 1);
            return this;
        }

        /** Set a field at the ordered the methods are chained at */
        public Injector add(Object object) {
            Conditions.isSmaller(fieldCounter, length - 1);
            add(fieldCounter++, object);
            return this;
        }

        /** Set a field at a specific instance */
        public Injector add(int index, Object object) {
            Conditions.inRange(index, 0, length - 1);
            values[index] = object;
            return this;
        }

        /** Inject the fields into the instance and return the injected instance */
        public Packet inject() {
            Field[] fields = packet.mcPacketClass().getDeclaredFields();
            for (int i = 0; i < length; i++) {
                Object value = values[i];
                if (value != null) {
                    Reflections.setter(packet.mcPacket(), fields[i], value);
                }
            }
            return packet;
        }
    }
}
