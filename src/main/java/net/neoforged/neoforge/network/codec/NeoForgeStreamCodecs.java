package net.neoforged.neoforge.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class NeoForgeStreamCodecs {
    public static <B extends ByteBuf, E extends Enum<E>> StreamCodec<B, E> enumCodec(Class<E> enumClass) {
        E[] constants = enumClass.getEnumConstants();
        return StreamCodec.of(
            (buf, val) -> ByteBufCodecs.VAR_INT.encode(buf, val.ordinal()),
            buf -> {
                int ord = ByteBufCodecs.VAR_INT.decode(buf);
                if (ord < 0 || ord >= constants.length) {
                    return constants[0];
                }
                return constants[ord];
            }
        );
    }
}
