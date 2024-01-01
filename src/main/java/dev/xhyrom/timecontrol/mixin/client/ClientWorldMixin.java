package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value={ClientWorld.class})
public abstract class ClientWorldMixin
        extends World
        implements ClientWorldAccessor {
    private double timeRate = 1.0;
    private int timeStopperId = -1;

    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow
    @Nullable
    public abstract Entity getEntityById(int var1);

    @Override
    public double getTimeRate() {
        return this.timeRate;
    }
    @Override
    public int getTimeStopperId() {
        return this.timeStopperId;
    }
    @Override
    public void updateTimeStatus(double timeRate, int timeStopperId) {
        this.timeRate = timeRate;
        this.timeStopperId = timeStopperId;
    }
    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(CallbackInfo ci) {
        if (this.timeStopperId == -1) {
            return;
        }
        ci.cancel();
    }
    @Inject(method={"tickEntity"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTickEntity(Entity entity, CallbackInfo ci) {
        LivingEntity livingEntity;
        if (this.timeStopperId == -1 || this.timeStopperId == entity.getId() || entity == MinecraftClient.getInstance().player) {
            return;
        }
        ci.cancel();
        entity.resetPosition();
        if (entity instanceof LivingEntity && !(livingEntity = (LivingEntity) entity).isDead() && livingEntity.hurtTime > 0) {
            --livingEntity.hurtTime;
        }
    }
    @Inject(method={"doRandomBlockDisplayTicks"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDoRandomBlockDisplayTicks(CallbackInfo ci) {
        if (this.timeStopperId == -1) {
            return;
        }
        ci.cancel();
    }
}
