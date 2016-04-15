package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.Conditions;

/** The type of packet */
public class PacketType {
    private int type;
    private int id;
    private int bounded;

    public PacketType(int id, int bounded, int type) {
        this.id = Conditions.isLarger(id, -1);
        this.bounded = Conditions.inRange(bounded, 0, 1); // based on source
        this.type = Conditions.inRange(type, -1, 2);
    }

    public int id() {
        return id;
    }

    public int bounded() {
        return bounded;
    }

    public int type() {
        return type;
    }
}
