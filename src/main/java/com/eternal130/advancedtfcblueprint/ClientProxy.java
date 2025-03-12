package com.eternal130.advancedtfcblueprint;

import net.minecraftforge.common.MinecraftForge;

import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(new AdvancedBlueprintUI());
    }

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

}
