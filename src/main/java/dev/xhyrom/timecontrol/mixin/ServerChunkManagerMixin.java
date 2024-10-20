package dev.xhyrom.timecontrol.mixin;

import dev.xhyrom.timecontrol.accessor.MinecraftServerAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.SpawnDensityCapper;
import net.minecraft.world.SpawnHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value={ServerChunkManager.class})
public abstract class ServerChunkManagerMixin {
    @Shadow
    @Final
    private ServerWorld world;
    @Shadow
    @Nullable
    private SpawnHelper.Info spawnInfo;

    @Redirect(method={"tickChunks"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z", ordinal=0))
    private boolean redirectGetBoolean(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule) {
        if (((MinecraftServerAccessor)this.world.getServer()).getTimeStopper() != null) {
            return false;
        }
        return gameRules.getBoolean(rule);
    }

    @Redirect(method={"tickChunks"}, at=@At(value="INVOKE", target = "Lnet/minecraft/world/SpawnHelper;setupSpawn(ILjava/lang/Iterable;Lnet/minecraft/world/SpawnHelper$ChunkSource;Lnet/minecraft/world/SpawnDensityCapper;)Lnet/minecraft/world/SpawnHelper$Info;", ordinal=0))
    private SpawnHelper.Info redirectSetupSpawn(int spawningChunkCount, Iterable<Entity> entities, SpawnHelper.ChunkSource chunkSource, SpawnDensityCapper spawnDensityCapper) {
        if (((MinecraftServerAccessor)world.getServer()).getTimeStopper() != null) {
            return spawnInfo;
        }

        return SpawnHelper.setupSpawn(spawningChunkCount, entities, chunkSource, spawnDensityCapper);
    }

    @Redirect(method={"tickChunks"}, at=@At(value="INVOKE", target="Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", ordinal=0))
    private void redirectForEach(List<ChunkHolder> list, Consumer<ChunkHolder> consumer) {
        if (((MinecraftServerAccessor)this.world.getServer()).getTimeStopper() != null) {
            return;
        }
        list.forEach(consumer);
    }
}
