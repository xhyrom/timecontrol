package dev.xhyrom.timecontrol.items;

import dev.xhyrom.timecontrol.accessor.MinecraftServerAccessor;
import dev.xhyrom.timecontrol.accessor.ServerItemCooldownManagerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TimeManipulationItem extends Item {
    public final Type type;

    public TimeManipulationItem(Settings settings, Type type) {
        super(settings);
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient) {
            MinecraftServerAccessor minecraftServerAccessor = (MinecraftServerAccessor)world.getServer();
            if (this.type == Type.STOPPER) {
                minecraftServerAccessor.setTimeStopper(minecraftServerAccessor.getTimeStopper() == null ? (ServerPlayerEntity) playerEntity : null);
            } else {
                double timeRate = minecraftServerAccessor.getTimeRate();
                timeRate = timeRate >= 1.0 ? timeRate + (this.type == Type.ACCELERATOR ? 0.5 : -0.5) : timeRate * (this.type == Type.ACCELERATOR ? 2.0 : 0.5);
                minecraftServerAccessor.setTimeRate(MathHelper.clamp(timeRate, 0.03125, 8.0));
            }
            ((ServerItemCooldownManagerAccessor)playerEntity.getItemCooldownManager()).setTimeManipulationTime(System.currentTimeMillis());
        }
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}

enum Type {
    ACCELERATOR,
    DECELERATOR,
    STOPPER;
}
