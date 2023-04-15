package net.fabricmc.example.mixin.client;

import net.fabricmc.example.accessor.ClientWorldAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public abstract class EntityMixin {
    @Shadow
    public World world;
    @Shadow
    public abstract int getId();
    @Inject(method={"changeLookDirection"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChangeLookDirection(CallbackInfo ci) {
        int timeStopperId = ((ClientWorldAccessor)this.world).getTimeStopperId();
        if (timeStopperId == -1 || timeStopperId == this.getId()) {
            return;
        }
        ci.cancel();
    }
}
