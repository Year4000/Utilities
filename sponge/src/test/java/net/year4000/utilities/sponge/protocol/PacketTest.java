/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.protocol;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PacketTest {
    public static PacketType type = PacketTypes.of(PacketTypes.State.PLAY, PacketTypes.Binding.OUTBOUND, 0x00);

    public static class FakePacket {
        int x;
        int y;
        int z;
    }

    public void check(Packet packet) {
        FakePacket fakePacket = (FakePacket) packet.mcPacket();
        Assert.assertEquals(1, fakePacket.x);
        Assert.assertEquals(2, fakePacket.y);
        Assert.assertEquals(3, fakePacket.z);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void packetTest() {
        Packet packet = new Packet(type, FakePacket.class, new FakePacket());
        Assert.assertEquals(type, packet.packetType());
        Assert.assertEquals(FakePacket.class, packet.mcPacketClass());
        Assert.assertNotNull(packet.mcPacket());

        packet.inject(ImmutableMap.of("x", 1, "y", 2, "z", 3));
        packet.inject(clazz -> packet.mcPacket());
        check(packet);
    }

    @Test
    public void packetAutoInjectorTest() {
        Packet packet = new Packet(type, FakePacket.class, new FakePacket());
        packet.injector()
            .add(1)
            .add(2)
            .add(3)
            .inject();
        check(packet);
    }

    @Test
    public void packetInjectorFailTest() {
        thrown.expect(IllegalArgumentException.class);
        Packet packet = new Packet(type, FakePacket.class, new FakePacket());
        packet.injector()
            .add(1)
            .skip()
            .add(3)
            .add(4)
            .inject();
        check(packet);
    }

    @Test
    public void packetInjectorTest() {
        Packet packet = new Packet(type, FakePacket.class, new FakePacket());
        packet.injector()
            .add(0, 1)
            .add(1, 2)
            .add(2, 3)
            .inject();
        check(packet);
    }

    @Test
    public void packetAccessorTest() {
        Packet packet = new Packet(type, FakePacket.class, new FakePacket());
        Assert.assertEquals(0, packet.accessor().skip().get(0).toInt());
        Assert.assertEquals(2, packet.injector().skip().add(2).inject().accessor().get(1).toInt());
    }
}
