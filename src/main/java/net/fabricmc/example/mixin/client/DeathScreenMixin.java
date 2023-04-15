package net.fabricmc.example.mixin.client;

import net.fabricmc.example.accessor.ClientWorldAccessor;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={DeathScreen.class})
public abstract class DeathScreenMixin
        extends Screen {
    protected DeathScreenMixin(Text title) {
        super(title);
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(CallbackInfo ci) {
        int timeStoppedId = ((ClientWorldAccessor)((Object)this.client.world)).getTimeStopperId();
        if (timeStoppedId == -1 || timeStoppedId == this.client.player.getId()) {
            return;
        }
        ci.cancel();
    }
}
