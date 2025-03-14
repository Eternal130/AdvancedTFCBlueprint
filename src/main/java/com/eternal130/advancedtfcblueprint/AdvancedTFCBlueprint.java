package com.eternal130.advancedtfcblueprint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = AdvancedTFCBlueprint.MODID,
    version = Tags.VERSION,
    name = "AdvancedTFCBlueprint",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:terrafirmacraftplus;required-after:modularui")
public class AdvancedTFCBlueprint {

    public static final String MODID = "advancedtfcblueprint";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(
        clientSide = "com.eternal130.advancedtfcblueprint.ClientProxy",
        serverSide = "com.eternal130.advancedtfcblueprint.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
