package dev.xhyrom.timecontrol.mixin;

import dev.xhyrom.timecontrol.items.TimeManipulationItem;
import dev.xhyrom.timecontrol.accessor.ServerItemCooldownManagerAccessor;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemCooldownManager.class)
public class ItemCooldownManagerMixin {
    @Inject(method = "getCooldownProgress", at = @At("HEAD"), cancellable = true)
    public void getCooldownProgress(Item item, float tickDelta, CallbackInfoReturnable<Float> cir) {
        if (!(item instanceof TimeManipulationItem)) return;
        if (!(this instanceof ServerItemCooldownManagerAccessor)) return;

        float progress = (float)(System.currentTimeMillis() - ((ServerItemCooldownManagerAccessor)(this)).getTimeManipulationTime()) / 250.0f;
        if (progress < 0.0f || progress >= 1.0f) {
            progress = 0.0f;
        }
        cir.setReturnValue(progress);
    }
}
