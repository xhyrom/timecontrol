package dev.xhyrom.timecontrol.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientPlayerEntity.class})
public abstract class ClientPlayerEntityMixin
        extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(CallbackInfo ci) {
        if (!(this.method_48926() instanceof ClientWorldAccessor)) return;

        int timeStopperId = ((ClientWorldAccessor)(this.method_48926())).getTimeStopperId();
        if (timeStopperId == -1 || timeStopperId == this.getId()) {
            return;
        }
        ci.cancel();
        this.resetPosition();
        this.strideDistance = 0.0f;
        this.prevStrideDistance = 0.0f;
        this.stepBobbingAmount = 0.0f;
        this.prevStepBobbingAmount = 0.0f;
        this.horizontalSpeed = 0.0f;
        this.prevHorizontalSpeed = 0.0f;
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
    }
}
