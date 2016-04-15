package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.Conditions;

public class Packet {
    private Object injectedPacket;
    PacketType packetType() {
        return null;
    }

    Object mcPacket() {
        return injectedPacket;
    }

    void inject(Object injectedPacket) {
        this.injectedPacket = Conditions.nonNull(injectedPacket, "injectedPacket");
    }
}
