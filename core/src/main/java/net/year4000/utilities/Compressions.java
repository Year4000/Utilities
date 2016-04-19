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

    /** Read the var int from the bytebuf */
    public static int readVarInt(ByteBuf byteBuf) {
        int i = 0, j = 0, k;

        do {
            k = byteBuf.readByte();
            i |= (k & 0x7F) << j++ * 7;
            Conditions.isSmaller(j, 5);
        } while ((k & 0x80) != 128);

        return i;
    }
}
