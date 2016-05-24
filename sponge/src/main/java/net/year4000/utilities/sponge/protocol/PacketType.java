/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

/** The type of packet */
public class PacketType {
    private final int state;
    private final int id;
    private final int bounded;

    public PacketType(int id, int state, int bounded) {
        this.id = Conditions.isLarger(id, -1);
        this.bounded = Conditions.inRange(bounded, -1, 2); // based on source
        this.state = Conditions.inRange(state, -2, 3);
    }

    /** The packet id for the type of packet */
    public int id() {
        return id;
    }

    /** The position of the way the packet is being sent */
    public int bounded() {
        return bounded;
    }

    /** The state of the packet type */
    public int state() {
        return state;
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }
}
