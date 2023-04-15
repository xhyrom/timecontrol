package net.fabricmc.example.mixin.client;

import net.fabricmc.example.accessor.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderTickCounter.class})
public abstract class RenderTickCounterMixin {
    @Shadow
    @Final
    private float tickTime;

    @Redirect(method={"beginRenderTick"}, at=@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter;tickTime:F", opcode=180, ordinal=0))
    private float redirectTickTime(RenderTickCounter renderTickCounter) {
        ClientWorldAccessor clientWorldAccessor = (ClientWorldAccessor) MinecraftClient.getInstance().world;
        if (clientWorldAccessor == null) {
            return this.tickTime;
        }
        return (float)((double)this.tickTime / clientWorldAccessor.getTimeRate());
    }
}
