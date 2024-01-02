package dev.xhyrom.timecontrol.mixin;

import dev.xhyrom.timecontrol.TimeControlMod;
import dev.xhyrom.timecontrol.accessor.MinecraftServerAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.BooleanSupplier;

@Mixin(value = MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerAccessor {
    @Shadow
    private PlayerManager playerManager;
    @Unique
    private double timeRate = 1.0;
    @Unique
    private UUID timeStopperId = Util.NIL_UUID;

    @Override
    public double getTimeRate() {
        return this.timeRate;
    }

    @Override
    public void setTimeRate(double timeRate) {
        this.timeRate = timeRate;
        this.sendTimeStatus();
    }

    @Override
    public ServerPlayerEntity getTimeStopper() {
        if (this.timeStopperId == Util.NIL_UUID) {
            return null;
        }
        ServerPlayerEntity timeStopper = this.playerManager.getPlayer(this.timeStopperId);
        if (timeStopper == null) {
            this.timeStopperId = Util.NIL_UUID;
        }
        return timeStopper;
    }

    @Override
    public void setTimeStopper(ServerPlayerEntity timeStopper) {
        this.timeStopperId = timeStopper == null ? Util.NIL_UUID : timeStopper.getUuid();
        this.sendTimeStatus();
    }

    @Override
    public void sendTimeStatus(ServerPlayerEntity serverPlayerEntity) {
        serverPlayerEntity.networkHandler.sendPacket(TimeControlMod.createTimeStatusPacket(this.getTimeRate(), this.getTimeStopper()));
    }

    private void sendTimeStatus() {
        this.playerManager.sendToAll(TimeControlMod.createTimeStatusPacket(this.getTimeRate(), this.getTimeStopper()));
    }

    @Inject(method={"tick"}, at=@At("HEAD"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerPlayerEntity timeStopper = this.getTimeStopper();
        if (timeStopper != null && timeStopper.isDisconnected()) {
            this.setTimeStopper(null);
        }
    }

    @ModifyConstant(method={"runServer"}, constant={@Constant(longValue=50L)})
    private long modifyTickDelay(long delay) {
        return (long)((double)delay / this.getTimeRate());
    }
}