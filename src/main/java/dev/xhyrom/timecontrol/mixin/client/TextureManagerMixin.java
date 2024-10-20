package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TextureManager.class})
public abstract class TextureManagerMixin {
    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(CallbackInfo ci) {
        if (MinecraftClient.getInstance().world == null) {
            return;
        }
        if (!(MinecraftClient.getInstance().world instanceof ClientWorldAccessor)) return;

        int timeStopperId = ((ClientWorldAccessor)(MinecraftClient.getInstance().world)).getTimeStopperId();
        if (timeStopperId == -1) {
            return;
        }
        ci.cancel();
    }
}
