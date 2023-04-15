package net.fabricmc.example.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final TimeManipulationItem ACCELERATOR = new TimeManipulationItem(new FabricItemSettings());
    public static final TimeManipulationItem DECELERATOR = new TimeManipulationItem(new FabricItemSettings());
    public static final TimeManipulationItem STOPPER = new TimeManipulationItem(new FabricItemSettings());

    public static void init() {
        Registry.register(Registry.ITEM, new Identifier("examplemod", "accelerator"), ACCELERATOR);
        Registry.register(Registry.ITEM, new Identifier("examplemod", "decelerator"), DECELERATOR);
        Registry.register(Registry.ITEM, new Identifier("examplemod", "stopper"), STOPPER);
    }
}
