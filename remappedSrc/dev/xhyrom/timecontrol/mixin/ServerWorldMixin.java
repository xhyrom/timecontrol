package dev.xhyrom.timecontrol.mixin;

import dev.xhyrom.timecontrol.accessor.MinecraftServerAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.EntityList;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(value={ServerWorld.class})
public abstract class ServerWorldMixin
        extends World {
    @Shadow
    @Final
    private ServerEntityManager<Entity> entityManager;
    @Shadow
    @Final
    private MinecraftServer server;
    @Shadow
    @Final
    private EntityList entityList;

    @Shadow
    public abstract void tickEntity(Entity var1);

    @Shadow public abstract ServerChunkManager getChunkManager();

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerPlayerEntity timeStopper = ((MinecraftServerAccessor)this.server).getTimeStopper();
        if (timeStopper == null) {
            return;
        }
        ci.cancel();
        Profiler profiler = this.getProfiler();
        profiler.push("chunkSource");
        this.getChunkManager().tick(shouldKeepTicking, true);
        profiler.pop();
        profiler.push("entities");
        profiler.push("tick");
        this.tickEntity(this::tickEntity, (Entity) timeStopper);
        this.entityList.forEach(entity -> {
            Entity livingEntity$temp;
            if (!entity.isRemoved() && (livingEntity$temp = entity) instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) livingEntity$temp;
                if (livingEntity.timeUntilRegen > 0) {
                    --livingEntity.timeUntilRegen;
                }
            }
        });
        profiler.pop();
        profiler.pop();
        profiler.push("entityManagement");
        this.entityManager.tick();
        profiler.pop();
    }
}
