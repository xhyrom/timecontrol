package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(value={MinecraftClient.class})
public abstract class MinecraftClientMixin {
    @Shadow
    @Final
    public InGameHud inGameHud;
    @Shadow
    @Nullable
    public ClientWorld world;
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void onTick(CallbackInfo ci) {
        if (this.world != null) {
            ClientWorldAccessor clientWorldAccessor = (ClientWorldAccessor)(this.world);
            this.inGameHud.setOverlayMessage(Text.of("Rychlost: " + new DecimalFormat("#.######").format(clientWorldAccessor.getTimeRate() * 100.0) + "%" + (clientWorldAccessor.getTimeStopperId() == -1 ? "" : " zastavený")), false);
        }
    }

    @Inject(method={"handleInputEvents"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleInputEvents(CallbackInfo ci) {
        if (this.world == null || this.player == null) {
            return;
        }
        int timeStopperId = ((ClientWorldAccessor)((Object)this.world)).getTimeStopperId();
        if (timeStopperId == -1 || timeStopperId == this.player.getId()) {
            return;
        }
        ci.cancel();
    }
}
