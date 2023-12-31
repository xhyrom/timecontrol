package dev.xhyrom.timecontrol;

import dev.xhyrom.timecontrol.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeControlMod implements ModInitializer {
	public static final Identifier CHANNEL_TIME_STATUS = new Identifier("timecontrol", "time_status");

	@Override
	public void onInitialize() {
		ModItems.init();
	}

	public static CustomPayloadS2CPacket createTimeStatusPacket(double timeRate, ServerPlayerEntity timeStopper) {
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeDouble(timeRate);
		packetByteBuf.writeInt(timeStopper == null ? -1 : timeStopper.getId());
		return new CustomPayloadS2CPacket(CHANNEL_TIME_STATUS, packetByteBuf);
	}
}
