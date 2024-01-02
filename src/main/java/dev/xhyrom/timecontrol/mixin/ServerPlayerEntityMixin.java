package dev.xhyrom.timecontrol.mixin;

import com.mojang.authlib.GameProfile;
import dev.xhyrom.timecontrol.accessor.MinecraftServerAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ServerPlayerEntity.class})
public abstract class ServerPlayerEntityMixin
        extends PlayerEntity {
    @Shadow
    @Final
    public MinecraftServer server;

    @Shadow
    public abstract void tick();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method={"moveToWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/server/PlayerManager;sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;)V", ordinal=0)})
    private void onCommandTree0(CallbackInfoReturnable<Entity> cir) {
        ((MinecraftServerAccessor)this.getServer()).sendTimeStatus((ServerPlayerEntity) (Object) this);
    }

    @Inject(method="teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", at={@At(value="INVOKE", target="Lnet/minecraft/server/PlayerManager;sendCommandTree(Lnet/minecraft/server/network/ServerPlayerEntity;)V", ordinal=0)})
    private void onCommandTree1(CallbackInfo ci) {
        ((MinecraftServerAccessor)this.getServer()).sendTimeStatus((ServerPlayerEntity) (Object) this);
    }

    @Redirect(method={"playerTick"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;tick()V", ordinal=0))
    private void redirectTick(PlayerEntity playerEntity) {
        ServerPlayerEntity timeStopper = ((MinecraftServerAccessor)this.server).getTimeStopper();
        if (timeStopper != null && timeStopper != playerEntity) {
            return;
        }
        super.tick();
    }
}
