package dev.xhyrom.timecontrol.mixin.client;

import dev.xhyrom.timecontrol.accessor.ClientWorldAccessor;
import dev.xhyrom.timecontrol.items.TimeManipulationItem;
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
        if (this.world == null) return;
        if (!(this.world instanceof ClientWorldAccessor)) return;

        ClientWorldAccessor clientWorldAccessor = (ClientWorldAccessor)(this.world);

        if (!(player.getInventory().getMainHandStack().getItem() instanceof TimeManipulationItem)) return;

        if (clientWorldAccessor.getTimeStopperId() != -1) {
            this.inGameHud.setOverlayMessage(
                    Text.translatable(
                            "overlay.timecontrol.rate.stopped",
                            new DecimalFormat("#.######")
                                    .format(clientWorldAccessor.getTimeRate() * 100.0) + "%"
                    ),
                    false
            );
        } else {
            this.inGameHud.setOverlayMessage(
                    Text.translatable(
                            "overlay.timecontrol.rate",
                            new DecimalFormat("#.######")
                                    .format(clientWorldAccessor.getTimeRate() * 100.0) + "%"
                    ),
                    false
            );
        }
    }

    @Inject(method={"handleInputEvents"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleInputEvents(CallbackInfo ci) {
        if (this.world == null || this.player == null) {
            return;
        }
        if (!(this.world instanceof ClientWorldAccessor)) return;

        int timeStopperId = ((ClientWorldAccessor) this.world).getTimeStopperId();
        if (timeStopperId == -1 || timeStopperId == this.player.getId()) {
            return;
        }
        ci.cancel();
    }
}
