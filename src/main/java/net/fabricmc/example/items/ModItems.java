package net.fabricmc.example.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final TimeManipulationItem ACCELERATOR = new TimeManipulationItem(new FabricItemSettings());
    public static final TimeManipulationItem DECELERATOR = new TimeManipulationItem(new FabricItemSettings());
    public static final TimeManipulationItem STOPPER = new TimeManipulationItem(new FabricItemSettings());

    public static void init() {
        Registry.register(Registries.ITEM, new Identifier("examplemod", "accelerator"), ACCELERATOR);
        Registry.register(Registries.ITEM, new Identifier("examplemod", "decelerator"), DECELERATOR);
        Registry.register(Registries.ITEM, new Identifier("examplemod", "stopper"), STOPPER);
    }
}
