package dev.xhyrom.timecontrol.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.xhyrom.timecontrol.accessor.MinecraftServerAccessor;
import dev.xhyrom.timecontrol.items.TimeManipulationItem;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static net.minecraft.server.command.CommandManager.*;

public class ModCommands {
    private static final LiteralCommandNode<ServerCommandSource> SET = literal("set")
            .then(
                    argument("time_rate", doubleArg(0.03125, 8.0))
                            .executes(context -> {
                                MinecraftServerAccessor minecraftServerAccessor = getAccessor(context.getSource());
                                double newTimeRate = context.getArgument("time_rate", Double.class);

                                minecraftServerAccessor.setTimeRate(MathHelper.clamp(newTimeRate, 0.03125, 8.0));

                                context.getSource().sendFeedback(() ->
                                                Text.translatable(
                                                        "command.timecontrol.change" + (isStopped(minecraftServerAccessor) ? ".stopped" : ""),
                                                        new DecimalFormat("#.######").format(minecraftServerAccessor.getTimeRate() * 100.0) + "%"
                                                ),
                                        false
                                );

                                return 1;
                            })
            )
            .build();

    private static final LiteralCommandNode<ServerCommandSource> STOP = literal("stop")
            .executes(context -> {
                MinecraftServerAccessor minecraftServerAccessor = getAccessor(context.getSource());
                ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                
                minecraftServerAccessor.setTimeStopper(minecraftServerAccessor.getTimeStopper() == null ? playerEntity : null);

                double timeRate = minecraftServerAccessor.getTimeRate();
                context.getSource().sendFeedback(() ->
                                Text.translatable(
                                        "command.timecontrol.change" + (isStopped(minecraftServerAccessor) ? ".stopped" : ""),
                                        new DecimalFormat("#.######").format(timeRate * 100.0) + "%"
                                ),
                        false
                );

                return 1;
            })
            .build();

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("timecontrol")
                    .then(build(TimeManipulationItem.Type.ACCELERATOR))
                    .then(build(TimeManipulationItem.Type.DECELERATOR))
                    .then(SET)
                    .then(STOP)
            );
        });
    }
    
    private static LiteralCommandNode<ServerCommandSource> build(TimeManipulationItem.Type type) {
        return literal(type == TimeManipulationItem.Type.ACCELERATOR ? "accelerate" : "decelerate")
                .executes(context -> {
                    MinecraftServerAccessor minecraftServerAccessor = getAccessor(context.getSource());
                    double timeRate = minecraftServerAccessor.getTimeRate();

                    timeRate = timeRate >= 1.0 ? timeRate + (type == TimeManipulationItem.Type.ACCELERATOR ? 0.5 : -0.5) : timeRate * (type == TimeManipulationItem.Type.ACCELERATOR ? 2.0 : 0.5);
                    minecraftServerAccessor.setTimeRate(MathHelper.clamp(timeRate, 0.03125, 8.0));

                    double finalTimeRate = timeRate;
                    context.getSource().sendFeedback(() ->
                                    Text.translatable(
                                            "command.timecontrol.change",
                                            new DecimalFormat("#.######").format(finalTimeRate * 100.0) + "%"
                                    ),
                            false
                    );

                    return 1;
                })
                .build();
    }

    private static MinecraftServerAccessor getAccessor(ServerCommandSource source) {
        return (MinecraftServerAccessor) source.getServer();
    }

    private static boolean isStopped(MinecraftServerAccessor accessor) {
        return accessor.getTimeStopper() != null;
    }
}
