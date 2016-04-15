package net.year4000.utilities.sponge.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.year4000.utilities.utils.UtilityConstructError;

import java.util.List;

/** The handles for the minecraft pipeline */
final class PipelineHandles {
    private PipelineHandles() {
        UtilityConstructError.raise();
    }

    /** Convert our packet system into the minecraft version */
    static class PacketSerializer extends MessageToMessageEncoder<Packet> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
            // todo copy packet values into mcPacket
            out.add(msg.mcPacket());
        }
    }
}
