package dev.xhyrom.timecontrol.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final TimeManipulationItem ACCELERATOR = new TimeManipulationItem(new FabricItemSettings().maxCount(1), Type.ACCELERATOR);
    public static final TimeManipulationItem DECELERATOR = new TimeManipulationItem(new FabricItemSettings().maxCount(1), Type.DECELERATOR);
    public static final TimeManipulationItem STOPPER = new TimeManipulationItem(new FabricItemSettings().maxCount(1), Type.STOPPER);
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier("timecontrol", "item_group"))
            .icon(() -> new ItemStack(ACCELERATOR))
            .build();

    public static void init() {
        Registry.register(Registries.ITEM, new Identifier("timecontrol", "accelerator"), ACCELERATOR);
        Registry.register(Registries.ITEM, new Identifier("timecontrol", "decelerator"), DECELERATOR);
        Registry.register(Registries.ITEM, new Identifier("timecontrol", "stopper"), STOPPER);

        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
            content.add(ACCELERATOR);
            content.add(DECELERATOR);
            content.add(STOPPER);
        });
    }
}
