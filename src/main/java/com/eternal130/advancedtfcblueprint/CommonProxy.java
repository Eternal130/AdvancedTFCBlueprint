package com.eternal130.advancedtfcblueprint;

import net.minecraft.item.ItemStack;

import com.dunk.tfc.api.TFCItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        AdvancedTFCBlueprint.LOG.info(Config.greeting);
        AdvancedTFCBlueprint.LOG.info("I am AdvancedTFCBlueprint at version " + Tags.VERSION);
        Items.setup();
        Items.register();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {

        GameRegistry.addShapelessRecipe(
            new ItemStack(Items.advancedBlueprint, 1),
            new ItemStack(TFCItems.ink),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1),
            new ItemStack(net.minecraft.init.Items.paper, 1));
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
