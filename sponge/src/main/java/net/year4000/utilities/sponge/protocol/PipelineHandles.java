package net.year4000.utilities.sponge.protocol;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;

/** The handles for the minecraft pipeline */
final class PipelineHandles {
    private PipelineHandles() {
        UtilityConstructError.raise();
    }

    /** Convert our packet system into the minecraft version */
    static class PacketEncoder extends MessageToMessageEncoder<Packet> {
        public static final String NAME = "my_packet_encoder";
        private PacketManager manager;

        PacketEncoder(PacketManager manager) {
            this.manager = Conditions.nonNull(manager, "manager");
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
            out.add(msg.mcPacket());
        }
    }

    static class PacketInterceptor extends ChannelDuplexHandler {
        public static final String NAME = "my_packet_interceptor";
        private PacketManager manager;

        PacketInterceptor(PacketManager manager) {
            this.manager = Conditions.nonNull(manager, "manager");
        }

        /** Intercept the packet and decided what to do with it */
        private boolean intercept(ChannelHandlerContext ctx, Class<?> clazz, Object msg) {
            if (manager.containsListener(clazz)) {
                Value<PacketType> packetType = PacketTypes.fromClass(clazz);
                if (packetType.isPresent()) {
                    Packet packet = new Packet(packetType.get(), msg);
                    Player player = ctx.channel().attr(PacketManager.PLAYER_KEY).get();
                    PacketListener listener = manager.getListener(clazz, player);
                    if (listener != null) {
                        try {
                            return listener.apply(player, packet);
                        } catch (Exception error) {
                            error.printStackTrace();
                            return false;
                        }
                    }
                }
            }
            return false;
        }

        /** Outbound packets */
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            if (!intercept(ctx, msg.getClass(), msg)) {
                super.write(ctx, msg, promise);
            }
        }

        /** Inbound packets */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!intercept(ctx, msg.getClass(), msg)) {
                super.channelRead(ctx, msg);
            }
        }
    }
}
