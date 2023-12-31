package dev.xhyrom.timecontrol.mixin;

import dev.xhyrom.timecontrol.accessor.ServerItemCooldownManagerAccessor;
import net.minecraft.server.network.ServerItemCooldownManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={ServerItemCooldownManager.class})
public abstract class ServerItemCooldownManagerMixin
        implements ServerItemCooldownManagerAccessor {
    @Unique
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
