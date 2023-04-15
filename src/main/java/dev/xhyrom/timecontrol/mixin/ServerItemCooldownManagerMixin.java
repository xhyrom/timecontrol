package dev.xhyrom.timecontrol.mixin;

import dev.xhyrom.timecontrol.accessor.ServerItemCooldownManagerAccessor;
import net.minecraft.server.network.ServerItemCooldownManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value={ServerItemCooldownManager.class})
public abstract class ServerItemCooldownManagerMixin
        implements ServerItemCooldownManagerAccessor {
    private long timeManipulationTime;

    @Override
    public long getTimeManipulationTime() {
        return this.timeManipulationTime;
    }

    @Override
    public void setTimeManipulationTime(long timeManipulationTime) {
        this.timeManipulationTime = timeManipulationTime;
    }
}
