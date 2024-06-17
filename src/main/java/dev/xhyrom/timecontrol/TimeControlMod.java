package dev.xhyrom.timecontrol;

import dev.xhyrom.timecontrol.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TimeControlMod implements ModInitializer {
	public static final Identifier CHANNEL_TIME_STATUS = Identifier.of("timecontrol", "time_status");

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(TimeControlTimePayload.ID, TimeControlTimePayload.CODEC);

		ModItems.init();
	}

	public static CustomPayloadS2CPacket createTimeStatusPacket(double timeRate, ServerPlayerEntity timeStopper) {
		return new CustomPayloadS2CPacket(new TimeControlTimePayload(timeRate, timeStopper == null ? -1 : timeStopper.getId()));
	}
}
