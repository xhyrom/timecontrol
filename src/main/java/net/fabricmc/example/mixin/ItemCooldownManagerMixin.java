package net.fabricmc.example.mixin;

import net.fabricmc.example.accessor.ServerItemCooldownManagerAccessor;
import net.fabricmc.example.items.TimeManipulationItem;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerItemCooldownManager;
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
        cir.setReturnValue(Float.valueOf(progress));
    }
}
