package dev.xhyrom.timecontrol.accessor;

import net.minecraft.server.network.ServerPlayerEntity;

public interface MinecraftServerAccessor {
    double getTimeRate();

    void setTimeRate(double var1);

    ServerPlayerEntity getTimeStopper();

    void setTimeStopper(ServerPlayerEntity var1);

    void sendTimeStatus(ServerPlayerEntity var1);
}
