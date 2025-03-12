package com.eternal130.advancedtfcblueprint.item;

import java.util.BitSet;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Items.ItemBlueprint;
import com.bioxx.tfc.Items.Tools.ItemChisel;
import com.bioxx.tfc.Items.Tools.ItemHammer;
import com.bioxx.tfc.TileEntities.TEDetailed;
import com.bioxx.tfc.api.TFCBlocks;

public class AdvancedBlueprint extends ItemBlueprint {

    public AdvancedBlueprint() {
        this.setMaxDamage(0);
        this.setUnlocalizedName("AdvancedBlueprint");
        this.setCreativeTab(TFCTabs.TFC_TOOLS);
        this.setFolder("tools/");
        this.setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("terrafirmacraft:tools/Blueprint");
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        return is.hasTagCompound() && is.stackTagCompound.hasKey("Name") ? (is.stackTagCompound.getString("Name")
            .equals("") ? super.getItemStackDisplayName(is) : is.stackTagCompound.getString("Name"))
            : super.getItemStackDisplayName(is);
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        if (TFC_Core.showShiftInformation()) {
            arraylist.add(TFC_Core.translate("gui.Help"));
            arraylist.add(TFC_Core.translate("gui.AdvancedBlueprint.Inst0"));
            if (is.hasTagCompound() && is.stackTagCompound.getByteArray("Data").length != 0)
                arraylist.add(TFC_Core.translate("gui.AdvancedBlueprint.Inst1"));
        } else {
            arraylist.add(TFC_Core.translate("gui.ShowHelp"));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        if (world.getBlock(x, y, z) != TFCBlocks.detailed) return false;

        if ((!stack.hasTagCompound() || stack.getTagCompound()
            .getByteArray("Data").length == 0) && player.isSneaking()) {
            TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);

            byte[] data = TEDetailed.toByteArray(te.data);

            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setByteArray(TAG_DATA, data);
            stack.setTagCompound(nbt);
        } else if (stack.hasTagCompound() && stack.getTagCompound()
            .getByteArray("Data").length != 0 && !player.isSneaking()) {
                int hasChisel = -1;
                int hasHammer = -1;

                if (!player.capabilities.isCreativeMode) {
                    for (int i = 0; i < 9; i++) {
                        if (player.inventory.mainInventory[i] != null
                            && player.inventory.mainInventory[i].getItem() instanceof ItemHammer) hasHammer = i;
                        if (player.inventory.mainInventory[i] != null
                            && player.inventory.mainInventory[i].getItem() instanceof ItemChisel) hasChisel = i;
                    }

                    if (hasChisel == -1 || hasHammer == -1) {
                        if (!world.isRemote) {
                            TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.Blueprint.missingTool"));
                        }
                        return false;
                    }
                }

                TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);
                BitSet blueprintData = TEDetailed.turnCube(stack.stackTagCompound.getByteArray(TAG_DATA), 0, 0, 0);

                for (int c = 0; c < 512; c++) if (te.data.get(c) && !blueprintData.get(c)) {
                    te.data.clear(c);

                    if (!player.capabilities.isCreativeMode) {
                        if (player.inventory.mainInventory[hasChisel] != null)
                            player.inventory.mainInventory[hasChisel].damageItem(1, player);
                        else break;
                    }
                }

                if (!world.isRemote) {
                    world.markBlockForUpdate(x, y, z);
                }
            }
        return false;
    }

}
