package com.eternal130.advancedtfcblueprint;

import net.minecraft.item.Item;

import com.eternal130.advancedtfcblueprint.item.AdvancedBlueprint;

import cpw.mods.fml.common.registry.GameRegistry;

public class Items {

    public static Item advancedBlueprint;

    public static void setup() {
        advancedBlueprint = new AdvancedBlueprint();
    }

    public static void register() {
        GameRegistry.registerItem(advancedBlueprint, advancedBlueprint.getUnlocalizedName());
    }
}
