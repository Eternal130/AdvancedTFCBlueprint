package com.eternal130.advancedtfcblueprint.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import com.eternal130.advancedtfcblueprint.item.AdvancedBlueprint;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SaveNBTDataMessageHandler implements IMessageHandler<SaveNBTDataMessage, IMessage> {

    @Override
    public IMessage onMessage(SaveNBTDataMessage message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.getItem() instanceof AdvancedBlueprint) {
            heldItem.setTagCompound(message.getNbtData());
        }
        return null;
    }
}
