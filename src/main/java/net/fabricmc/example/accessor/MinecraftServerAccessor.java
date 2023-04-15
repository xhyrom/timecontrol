package net.fabricmc.example.accessor;

import net.minecraft.server.network.ServerPlayerEntity;

public interface MinecraftServerAccessor {
    public double getTimeRate();

    public void setTimeRate(double var1);

    public ServerPlayerEntity getTimeStopper();

    public void setTimeStopper(ServerPlayerEntity var1);

    public void sendTimeStatus(ServerPlayerEntity var1);
}
