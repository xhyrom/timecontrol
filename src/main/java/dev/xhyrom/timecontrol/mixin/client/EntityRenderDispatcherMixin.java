package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={EntityRenderDispatcher.class})
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    private World world;

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/EntityRenderer;)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true)
    private float modifyTickDelta(float tickDelta, Entity entity) {
        if (!(this.world instanceof ClientWorldAccessor)) return 0.0f;

        int timeStopperId = ((ClientWorldAccessor)(this.world)).getTimeStopperId();
        if (timeStopperId == -1 || timeStopperId == entity.getId()) {
            return tickDelta;
        }
        return 0.0f;
    }
}
