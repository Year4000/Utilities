package net.year4000.utilities.sponge.protocol;

import io.netty.channel.*;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;
import org.spongepowered.api.entity.living.player.Player;

/** The handles for the minecraft pipeline */
final class PipelineHandles {
    private PipelineHandles() {
        UtilityConstructError.raise();
    }

    /** The name of the outbound packet interceptor id */
    public static final String OUTBOUND_NAME = "outbound_packet_interceptor";
    /** The name of the inbound packet interceptor id */
    public static final String INBOUND_NAME = "inbound_packet_interceptor";
    /** Outbound packets are intercepted */
    public static final ChannelOutboundHandlerAdapter OUTBOUND_PACKET_INTERCEPTOR = new ChannelOutboundHandlerAdapter() {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            if (!intercept(ctx, msg.getClass(), msg)) {
                super.write(ctx, msg, promise);
            }
        }
    };
    /** Inbound packets are intercepted */
    public static final ChannelInboundHandlerAdapter INBOUND_PACKET_INTERCEPTOR = new ChannelInboundHandlerAdapter() {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!intercept(ctx, msg.getClass(), msg)) {
                super.channelRead(ctx, msg);
            }
        }
    };

    /** Intercept the packet and decided what to do with it */
    private static boolean intercept(ChannelHandlerContext ctx, Class<?> clazz, Object msg) {
        PacketManager manager = ctx.channel().attr(PacketManager.PACKET_MANAGER_KEY).get();
        if (manager.containsListener(clazz)) {
            Value<PacketType> packetType = PacketTypes.fromClass(clazz);
            if (packetType.isPresent()) {
                Packet packet = new Packet(packetType.get(), msg);
                PacketListener listener = manager.getListener(clazz);
                if (listener != null) {
                    Player player = ctx.channel().attr(PacketManager.PLAYER_KEY).get();
                    try {
                        return listener.apply(player, packet);
                    } catch (Exception error) {
                        ErrorReporter.builder(error)
                            .add("PacketManager: ", Integer.toHexString(manager.hashCode()))
                            .add("PacketManager Plugin: ", manager.plugin)
                            .add("PacketManager ID: ", manager.id)
                            .add("PacketManager Listener(s)", manager.listeners.size())
                            .add("Player: ", player.getName())
                            .add("Packet ID: ", Integer.toHexString(packet.packetType().id()))
                            .add("Packet State: ", PacketTypes.State.values()[packet.packetType().state()])
                            .add("Packet Bounded: ", PacketTypes.Binding.values()[packet.packetType().bounded()])
                            .add("Packet Class: ", packet.mcPacketClass())
                            .add("Packet Object: ", packet.mcPacket())
                            .buildAndReport(System.out);
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
