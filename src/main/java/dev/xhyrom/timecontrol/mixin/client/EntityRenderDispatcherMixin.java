package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={EntityRenderManager.class})
public abstract class EntityRenderDispatcherMixin {
    @ModifyVariable(
            method = "getAndUpdateRenderState",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true)
    private float modifyTickDelta(float tickDelta, Entity entity) {
        if (!(entity.getEntityWorld() instanceof ClientWorldAccessor)) return 0.0f;

        int timeStopperId = ((ClientWorldAccessor)(entity.getEntityWorld())).getTimeStopperId();
        if (timeStopperId == -1 || timeStopperId == entity.getId()) {
            return tickDelta;
        }
        return 0.0f;
    }
}
