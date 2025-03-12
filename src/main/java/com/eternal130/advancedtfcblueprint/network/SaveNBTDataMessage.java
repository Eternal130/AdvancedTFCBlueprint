package com.eternal130.advancedtfcblueprint.network;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class SaveNBTDataMessage implements IMessage {

    private NBTTagCompound nbtData;

    public SaveNBTDataMessage() {}

    public SaveNBTDataMessage(NBTTagCompound nbtData) {
        this.nbtData = nbtData;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        nbtData = ByteBufUtils.readTag(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, nbtData);
    }

    public NBTTagCompound getNbtData() {
        return nbtData;
    }
}
