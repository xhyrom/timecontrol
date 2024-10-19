package dev.xhyrom.timecontrol.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    private static final TimeManipulationItem ACCELERATOR = new TimeManipulationItem(new Item.Settings().maxCount(1), TimeManipulationItem.Type.ACCELERATOR);
    private static final TimeManipulationItem DECELERATOR = new TimeManipulationItem(new Item.Settings().maxCount(1), TimeManipulationItem.Type.DECELERATOR);
    private static final TimeManipulationItem STOPPER = new TimeManipulationItem(new Item.Settings().maxCount(1), TimeManipulationItem.Type.STOPPER);
    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.translatable("itemGroup.timecontrol.item_group"))
            .icon(() -> new ItemStack(ACCELERATOR))
            .entries((displayContext, entries) -> {
                entries.add(ACCELERATOR);
                entries.add(DECELERATOR);
                entries.add(STOPPER);
            })
            .build();

    public static void init() {
        Registry.register(Registries.ITEM, Identifier.of("timecontrol", "accelerator"), ACCELERATOR);
        Registry.register(Registries.ITEM, Identifier.of("timecontrol", "decelerator"), DECELERATOR);
        Registry.register(Registries.ITEM, Identifier.of("timecontrol", "stopper"), STOPPER);
        Registry.register(Registries.ITEM_GROUP, Identifier.of("timecontrol", "item_group"), ITEM_GROUP);
    }
}
