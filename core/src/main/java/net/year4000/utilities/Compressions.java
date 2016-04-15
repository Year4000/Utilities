package net.year4000.utilities;

import io.netty.buffer.ByteBuf;
import net.year4000.utilities.utils.UtilityConstructError;

public final class Compressions {
    private Compressions() {
        UtilityConstructError.raise();
    }

    /** Write the var int to the bytebuf */
    public static void writeVarInt(int number, ByteBuf byteBuf) {
        while ((number & 0xFFFFFF80) != 0) {
            byteBuf.writeByte(number & 0x7F | 0x80);
            number >>>= 7;
        }
        byteBuf.writeByte(number);
    }
}
