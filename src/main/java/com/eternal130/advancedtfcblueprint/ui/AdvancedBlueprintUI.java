package com.eternal130.advancedtfcblueprint.ui;

import java.util.BitSet;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.ClientGUI;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.TEDetailed;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Util.Helper;
import com.eternal130.advancedtfcblueprint.Items;
import com.eternal130.advancedtfcblueprint.Tool;
import com.eternal130.advancedtfcblueprint.button.ButtonCarved;
import com.eternal130.advancedtfcblueprint.button.ButtonChLayer;
import com.eternal130.advancedtfcblueprint.button.ButtonDirection;
import com.eternal130.advancedtfcblueprint.button.ButtonEdit;
import com.eternal130.advancedtfcblueprint.button.ButtonLayer;
import com.eternal130.advancedtfcblueprint.network.NetworkHandler;
import com.eternal130.advancedtfcblueprint.network.SaveNBTDataMessage;
import com.eternal130.advancedtfcblueprint.widget.DragableGridWidget;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AdvancedBlueprintUI {

    private static boolean[][][] modifiedData = new boolean[8][8][8];
    private static int currentLayer = 0;
    private static String name = "name_it";
    private static Tool.Direction direction = Tool.Direction.TOP;
    private static final UITexture BlueprintBackground = UITexture
        .fullImage("advancedtfcblueprint", "textures/gui/advancedBlueprint.png")
        .getSubArea(0, 0, (float) 144 / 208, 1);
    private static final IDrawable Carved = UITexture
        .fullImage("advancedtfcblueprint", "textures/gui/advancedBlueprint.png")
        .getSubArea((float) 144 / 208, (float) 16 / 160, (float) 160 / 208, (float) 32 / 160);
    private static final UITexture UnCarved = UITexture
        .fullImage("advancedtfcblueprint", "textures/gui/advancedBlueprint.png")
        .getSubArea((float) 144 / 208, 0, (float) 160 / 208, (float) 16 / 160);
    private static final ButtonDirection[] DirectionButtons = new ButtonDirection[6];
    private static final ButtonLayer[] LayerButtons = new ButtonLayer[8];
    private static final ButtonChLayer[] ChLayerButtons = new ButtonChLayer[2];
    private static final ButtonWidget[] EditButtons = new ButtonWidget[8];
    private static final ButtonCarved[][] CarvedButtons = new ButtonCarved[8][8];
    private static final ButtonWidget[] SaveButtons = new ButtonWidget[2];
    private static final TextFieldWidget nameField = new TextFieldWidget().size(200, 15)
        .onUpdateListener(s -> setName(s.getText()));
    private static final char[] EditType = { '←', '→', '↑', '↓', '↻', '↺', '↔', '↕' };

    public static boolean[][][] getModifiedData() {
        return modifiedData;
    }

    public static void setModifiedData(boolean[][][] modifiedData) {
        AdvancedBlueprintUI.modifiedData = modifiedData;
    }

    public static int getCurrentLayer() {
        return currentLayer;
    }

    public static void setCurrentLayer(int currentLayer) {
        if (currentLayer < 0) currentLayer = 0;
        if (currentLayer > 7) currentLayer = 7;
        AdvancedBlueprintUI.currentLayer = currentLayer;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        AdvancedBlueprintUI.name = name;
    }

    public static Tool.Direction getDirection() {
        return direction;
    }

    public static void setDirection(Tool.Direction direction) {
        AdvancedBlueprintUI.direction = direction;
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent event) {
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null) return;
        if (event.entityPlayer.getEntityWorld().isRemote && !event.entityPlayer.isSneaking()
            && heldItem.getItem() == Items.advancedBlueprint) {
            MovingObjectPosition mo = Helper.getMouseOverObject(event.entityPlayer, event.world);
            if (mo == null || event.world.getBlock(mo.blockX, mo.blockY, mo.blockZ) != TFCBlocks.detailed) {
                if (heldItem.hasTagCompound()) {
                    loadDataFromNBT(heldItem);
                } else {
                    resetData();
                }
                ClientGUI.open(createGUI());
            }
        }
    }

    private void resetData() {
        modifiedData = new boolean[8][8][8];
        name = "";
        nameField.setText(name);
        currentLayer = 0;
        direction = Tool.Direction.TOP;
    }

    public static ModularScreen createGUI() {
        ModularPanel panel = ModularPanel.defaultPanel("advancedblueprint", 246, 256);
        panel.child(
            new Column().margin(7)
                .child(
                    new TextWidget(TFC_Core.translate("gui.AdvancedBlueprint.title")).alignment(Alignment.Center)
                        .color(0x000000)
                        .style(EnumChatFormatting.BOLD)
                        .size(90, 15))
                .child(
                    new Row().size(232, 15)
                        .margin(0)
                        .child(
                            new TextWidget(TFC_Core.translate("gui.AdvancedBlueprint.name")).size(25, 15)
                                .marginLeft(5))
                        .child(nameField))
                .child(
                    new Row().marginLeft(2)
                        .marginRight(2)
                        .marginTop(6)
                        .marginBottom(1)
                        .size(232, 160)
                        .child(regDirButtons())
                        .child(regCarvedButtons())
                        .child(regChLayerButtons())
                        .child(regLayerButtons()))
                .child(regEditButtons())
                .child(regSaveButtons()));
        return new ModularScreen(panel);
    }

    private static Column regDirButtons() {
        Column dirColumn = new Column();
        dirColumn.marginTop(2)
            .marginBottom(2)
            .size(37, 160);
        for (int i = 0; i < 6; i++) {
            DirectionButtons[i] = new ButtonDirection<>(Tool.Direction.values()[i]).size(37, 17)
                .overlay(GuiTextures.MC_BUTTON_DISABLED, IKey.str(Tool.Direction.values()[i].name()))
                .background(GuiTextures.MC_BUTTON, IKey.str(Tool.Direction.values()[i].name()))
                .hoverBackground(GuiTextures.MC_BUTTON_HOVERED, IKey.str(Tool.Direction.values()[i].name()))
                .marginTop(9);
            dirColumn.child(DirectionButtons[i]);
        }
        return dirColumn;
    }

    private static Widget regCarvedButtons() {
        Grid carvedGrid = new DragableGridWidget();
        carvedGrid.marginTop(9)
            .marginLeft(9)
            .size(128, 128);
        for (int i = 0; i < 8; i++) {
            carvedGrid.nextRow();
            for (int j = 0; j < 8; j++) {
                CarvedButtons[j][i] = new ButtonCarved<>(j, i).size(16, 16)
                    .background(UnCarved)
                    .overlay(Carved);
                carvedGrid.child(CarvedButtons[j][i]);
            }
        }
        ParentWidget parent = new ParentWidget<>();
        parent.background(BlueprintBackground)
            .size(144, 160)
            .margin(5);
        parent.child(carvedGrid);
        return parent;
    }

    private static Widget regLayerButtons() {
        Column layerColumn = new Column();
        layerColumn.marginTop(2)
            .marginBottom(2)
            .size(17, 160);
        for (int i = 0; i < 8; i++) {
            LayerButtons[i] = new ButtonLayer<>(i).size(17, 17)
                .overlay(GuiTextures.MC_BUTTON_DISABLED, IKey.str(String.valueOf(i)))
                .background(GuiTextures.MC_BUTTON, IKey.str(String.valueOf(i)))
                .hoverBackground(GuiTextures.MC_BUTTON_HOVERED, IKey.str(String.valueOf(i)))
                .marginTop(2);
            layerColumn.child(LayerButtons[i]);
        }
        return layerColumn;
    }

    private static Widget regChLayerButtons() {
        Column chLayerColumn = new Column();
        chLayerColumn.marginTop(2)
            .marginBottom(2)
            .marginRight(5)
            .size(17, 40);
        ChLayerButtons[0] = new ButtonChLayer<>(0).size(17, 17)
            .overlay(GuiTextures.MC_BUTTON_DISABLED, IKey.str("▲"))
            .background(GuiTextures.MC_BUTTON, IKey.str("▲"))
            .hoverBackground(GuiTextures.MC_BUTTON_HOVERED, IKey.str("▲"))
            .marginTop(2);
        ChLayerButtons[1] = new ButtonChLayer<>(1).size(17, 17)
            .overlay(GuiTextures.MC_BUTTON_DISABLED, IKey.str("▼"))
            .background(GuiTextures.MC_BUTTON, IKey.str("▼"))
            .hoverBackground(GuiTextures.MC_BUTTON_HOVERED, IKey.str("▼"))
            .marginTop(2);
        chLayerColumn.child(ChLayerButtons[0]);
        chLayerColumn.child(ChLayerButtons[1]);
        return chLayerColumn;
    }

    private static Widget regEditButtons() {
        Row editRow = new Row();
        editRow.marginTop(4)
            .marginBottom(4)
            .size(150, 17);
        for (int i = 0; i < 8; i++) {
            EditButtons[i] = new ButtonEdit<>(EditType[i]).size(17, 17)
                .background(GuiTextures.MC_BUTTON, IKey.str(String.valueOf(EditType[i])))
                .hoverBackground(GuiTextures.MC_BUTTON_HOVERED, IKey.str(String.valueOf(EditType[i])))
                .marginLeft(1);
            editRow.child(EditButtons[i]);
        }
        return editRow;
    }

    private static Widget regSaveButtons() {
        Row saveRow = new Row();
        saveRow.marginTop(4)
            .marginBottom(4)
            .size(78, 20);
        saveRow.align(Alignment.BottomRight);
        SaveButtons[0] = new ButtonWidget<>().size(37, 17)
            .background(GuiTextures.MC_BUTTON, IKey.str(TFC_Core.translate("gui.AdvancedBlueprint.cancel")))
            .hoverBackground(
                GuiTextures.MC_BUTTON_HOVERED,
                IKey.str(TFC_Core.translate("gui.AdvancedBlueprint.cancel")))
            .onMousePressed(mouseButton -> {
                if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null
                    && Minecraft.getMinecraft().thePlayer.getHeldItem()
                        .hasTagCompound())
                    loadDataFromNBT(Minecraft.getMinecraft().thePlayer.getHeldItem());
                return true;
            })
            .marginRight(2)
            .marginBottom(6);
        SaveButtons[1] = new ButtonWidget<>().size(37, 17)
            .background(GuiTextures.MC_BUTTON, IKey.str(TFC_Core.translate("gui.AdvancedBlueprint.save")))
            .hoverBackground(GuiTextures.MC_BUTTON_HOVERED, IKey.str(TFC_Core.translate("gui.AdvancedBlueprint.save")))
            .onMousePressed(mouseButton -> {
                saveDataToNBT();
                return true;
            })
            .marginRight(2)
            .marginBottom(6);

        saveRow.child(SaveButtons[0]);
        saveRow.child(SaveButtons[1]);
        return saveRow;
    }

    public static void loadDataFromNBT(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        byte[] data = nbt.getByteArray("Data");
        if (data.length == 0) {
            modifiedData = new boolean[8][8][8];
        } else {
            BitSet blueprintData = TEDetailed.turnCube(data, 0, 0, 0);
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    for (int y = 0; y < 8; y++) {
                        modifiedData[x][z][y] = !blueprintData.get((x * 8 + z) * 8 + y);
                    }
                }
            }
        }
        name = nbt.getString("Name");
        if (Objects.equals(name, "")) name = "name_it";
        nameField.setText(name);
        currentLayer = nbt.getInteger("Layer");
        direction = Tool.Direction.values()[nbt.getInteger("Direction")];
    }

    public static void saveDataToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        byte[] data;
        BitSet blueprintData = new BitSet(512);
        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                for (int y = 0; y < 8; y++) {
                    blueprintData.set((x * 8 + z) * 8 + y, !modifiedData[x][z][y]);
                }
            }
        }
        data = TEDetailed.toByteArray(blueprintData);
        nbt.setByteArray("Data", data);
        if (name != null) nbt.setString("Name", name);
        nbt.setInteger("Layer", currentLayer);
        nbt.setInteger("Direction", direction.ordinal());
        NetworkHandler.INSTANCE.sendToServer(new SaveNBTDataMessage(nbt));
    }
}
