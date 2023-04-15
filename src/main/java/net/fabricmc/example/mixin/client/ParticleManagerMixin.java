package net.fabricmc.example.mixin.client;

import net.fabricmc.example.accessor.ClientWorldAccessor;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin(value={ParticleManager.class})
public abstract class ParticleManagerMixin {
    @Shadow
    protected ClientWorld world;
    @Shadow
    @Final
    private Queue<Particle> newParticles;

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(CallbackInfo ci) {
        if (this.world == null) {
            return;
        }
        int timeStopperId = ((ClientWorldAccessor)this.world).getTimeStopperId();
        if (timeStopperId == -1) {
            return;
        }
        ci.cancel();
        this.newParticles.clear();
    }

    @ModifyVariable(method={"renderParticles"}, at=@At(value="HEAD"), ordinal=0)
    private float modifyTickDelta(float tickDelta) {
        int timeStopperId;
        if (this.world != null && (timeStopperId = ((ClientWorldAccessor)this.world).getTimeStopperId()) != -1) {
            return 0.0f;
        }
        return tickDelta;
    }
}
