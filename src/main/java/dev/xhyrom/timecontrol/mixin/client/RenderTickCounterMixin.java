package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderTickCounter.Dynamic.class})
public abstract class RenderTickCounterMixin {
    @Shadow
    @Final
    private float tickTime;

    @Redirect(method="beginRenderTick(J)I", at=@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter$Dynamic;tickTime:F", opcode=180, ordinal=0))
    private float redirectTickTime(RenderTickCounter.Dynamic instance) {
        ClientWorldAccessor clientWorldAccessor = (ClientWorldAccessor) MinecraftClient.getInstance().world;
        if (clientWorldAccessor == null) {
            return this.tickTime;
        }
        return (float)((double)this.tickTime / clientWorldAccessor.getTimeRate());
    }
}
