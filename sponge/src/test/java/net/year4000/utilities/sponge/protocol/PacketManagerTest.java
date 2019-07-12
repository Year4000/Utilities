/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol;

import org.junit.Assert;
import org.junit.Test;

public class PacketManagerTest {
    private final PacketManager packets = new PacketManager();
    private final Class<?> clazz = getClass();
    private final PacketListener packetListener = (player, packet) -> false;

    @Test
    public void managerTest() {
        packets.registerListener(clazz, packetListener);
        Assert.assertEquals(packetListener, packets.getListener(clazz));
        Assert.assertTrue(packets.containsListener(clazz));
        packets.removeListener(clazz);
        Assert.assertFalse(packets.containsListener(clazz));
    }
}
