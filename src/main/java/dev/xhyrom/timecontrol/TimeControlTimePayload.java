package dev.xhyrom.timecontrol;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record TimeControlTimePayload(double timeRate, int timeStopperId) implements CustomPayload {
    public static final CustomPayload.Id<TimeControlTimePayload> ID = new CustomPayload.Id<>(TimeControlMod.CHANNEL_TIME_STATUS);
    public static final PacketCodec<RegistryByteBuf, TimeControlTimePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE, TimeControlTimePayload::timeRate,
            PacketCodecs.INTEGER, TimeControlTimePayload::timeStopperId,
            TimeControlTimePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
