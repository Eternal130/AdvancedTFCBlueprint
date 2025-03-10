package com.eternal130.advancedtfcblueprint.item;

import java.util.BitSet;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.dunk.tfc.Core.TFCTabs;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemBlueprint;
import com.dunk.tfc.Items.Tools.ItemChisel;
import com.dunk.tfc.Items.Tools.ItemHammer;
import com.dunk.tfc.TileEntities.TEDetailed;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Util.Helper;
import com.eternal130.advancedtfcblueprint.button.ButtonWidgetBlueprint;
import com.eternal130.advancedtfcblueprint.button.ButtonWidgetDirection;
import com.eternal130.advancedtfcblueprint.button.ButtonWidgetLayer;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.*;
import com.gtnewhorizons.modularui.api.screen.IItemWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.*;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

public class AdvancedBlueprint extends ItemBlueprint implements IItemWithModularUI {

    public AdvancedBlueprint() {
        this.setMaxDamage(0);
        this.setUnlocalizedName("AdvancedBlueprint");
        this.setCreativeTab(TFCTabs.TFC_TOOLS);
        this.setFolder("tools/");
        this.setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("terrafirmacraftplus:tools/Blueprint");
    }

    public boolean getModifiedDataByDirectionAndLayer(int x, int z, Direction dir, int y) {
        return switch (dir) {
            case TOP -> modifiedData[x][z][7 - y];
            case BOTTOM -> modifiedData[7 - x][z][y];
            case NORTH -> modifiedData[7 - x][y][7 - z];
            case SOUTH -> modifiedData[x][7 - y][7 - z];
            case WEST -> modifiedData[y][x][7 - z];
            case EAST -> modifiedData[7 - y][7 - x][7 - z];
        };
    }

    public void setModifiedDataByDirectionAndLayer(int x, int z, Direction dir, int y) {
        switch (dir) {
            case TOP -> modifiedData[x][z][7 - y] = !modifiedData[x][z][7 - y];
            case BOTTOM -> modifiedData[7 - x][z][y] = !modifiedData[7 - x][z][y];
            case NORTH -> modifiedData[7 - x][y][7 - z] = !modifiedData[7 - x][y][7 - z];
            case SOUTH -> modifiedData[x][7 - y][7 - z] = !modifiedData[x][7 - y][7 - z];
            case WEST -> modifiedData[y][x][7 - z] = !modifiedData[y][x][7 - z];
            case EAST -> modifiedData[7 - y][7 - x][7 - z] = !modifiedData[7 - y][7 - x][7 - z];
        }
    }

    public boolean[][][] getModifiedData() {
        return modifiedData;
    }

    public int getCurrentLayer() {
        return currentLayer;
    }

    private final boolean[][][] modifiedData = new boolean[8][8][8];
    private int currentLayer = 0;
    private String name = "name_it";
    private static final AdaptableUITexture DISPLAY = AdaptableUITexture
        .of("modularui:gui/background/display", 143, 75, 2);
    private static final UITexture BlueprintBackground = UITexture
        .partly("advancedtfcblueprint", "textures/gui/advancedBlueprint.png", 208, 160, 0, 0, 144, 160);

    public enum Direction {
        TOP,
        BOTTOM,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private Direction direction = Direction.TOP;

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext, ItemStack heldStack) {
        ModularWindow.Builder builder = ModularWindow.builder(new Size(256, 256));
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        loadDataFromNBT(heldStack);

        return builder.widget(
            new TabContainer().addPage(
                new MultiChildWidget().addChild(createTopPage())
                    .addChild(createBottomPage())
                    .addChild(createCarveBlock())))
            .build();
    }

    private Widget createTopPage() {
        return new Column().setSpace(5)
            .setMaxHeight(166)
            .setAlignment(MainAxisAlignment.SPACE_BETWEEN, CrossAxisAlignment.START)
            .addChild(
                new TextWidget(
                    new Text(TFC_Core.translate("gui.AdvancedBlueprint.title")).color(Color.BLACK.normal)
                        .format(EnumChatFormatting.BOLD)).setTextAlignment(Alignment.Center)
                            .setPos(80, 10)
                            .setSize(80, 20))
            .addChild(
                new TextFieldWidget().setGetter(() -> name)
                    .setSetter(val -> name = val)
                    .setTextColor(Color.WHITE.dark(1))
                    .setTextAlignment(Alignment.Center)
                    .setScrollBar()
                    .setBackground(DISPLAY.withOffset(-2, -2, 4, 4))
                    .setSize(new Size(200, 15))
                    .setPos(30, 30))
            .addChild(createMainPage());
    }

    public boolean translateLayer(Direction direction, int layer, int mov, boolean rotation, boolean reverse,
        boolean mirror) {
        boolean[][] layerData = new boolean[8][8];
        boolean[][] newLayerData = new boolean[8][8];

        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                switch (direction) {
                    case TOP -> layerData[x][z] = modifiedData[x][z][7 - layer];
                    case BOTTOM -> layerData[x][z] = modifiedData[7 - x][z][layer];
                    case NORTH -> layerData[x][z] = modifiedData[7 - x][layer][7 - z];
                    case SOUTH -> layerData[x][z] = modifiedData[x][7 - layer][7 - z];
                    case WEST -> layerData[x][z] = modifiedData[layer][x][7 - z];
                    case EAST -> layerData[x][z] = modifiedData[7 - layer][7 - x][7 - z];
                }
            }
        }

        if (mov != -1) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    newLayerData[x][z] = false;
                }
            }
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    if (layerData[x][z]) {
                        int newX = x, newZ = z;
                        switch (mov) {
                            case 0 -> newZ--; // Up
                            case 1 -> newZ++; // Down
                            case 2 -> newX--; // Left
                            case 3 -> newX++; // Right
                        }
                        if (newX >= 0 && newX < 8 && newZ >= 0 && newZ < 8) {
                            newLayerData[newX][newZ] = true;
                        } else {
                            return false; // Translation out of bounds
                        }
                    }
                }
            }
        } else if (rotation) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    if (reverse) {
                        newLayerData[x][z] = layerData[7 - z][x]; // Counter-clockwise
                    } else {
                        newLayerData[x][z] = layerData[z][7 - x]; // Clockwise
                    }
                }
            }
        } else if (mirror) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    if (reverse) {
                        newLayerData[x][z] = layerData[x][7 - z]; // Vertical mirror
                    } else {
                        newLayerData[x][z] = layerData[7 - x][z]; // Horizontal mirror
                    }
                }
            }
        }

        // Update the modifiedData array with the new layer data
        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                switch (direction) {
                    case TOP -> modifiedData[x][z][7 - layer] = newLayerData[x][z];
                    case BOTTOM -> modifiedData[7 - x][z][layer] = newLayerData[x][z];
                    case NORTH -> modifiedData[7 - x][layer][7 - z] = newLayerData[x][z];
                    case SOUTH -> modifiedData[x][7 - layer][7 - z] = newLayerData[x][z];
                    case WEST -> modifiedData[layer][x][7 - z] = newLayerData[x][z];
                    case EAST -> modifiedData[7 - layer][7 - x][7 - z] = newLayerData[x][z];
                }
            }
        }

        return true;
    }

    private Widget createMainPage() {
        return new Row().setSpace(5)
            .setAlignment(MainAxisAlignment.SPACE_BETWEEN, CrossAxisAlignment.START)
            .addChild(createDirectionButton())
            .addChild(
                new MultiChildWidget().addChild(createEditButton())
                    .setBackground(BlueprintBackground)
                    .setSize(144, 160))
            .addChild(
                new Column().setSpace(5)
                    .setAlignment(MainAxisAlignment.SPACE_BETWEEN, CrossAxisAlignment.CENTER)
                    .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                        if (clickData.mouseButton == 0 && !button.isClient()) {
                            if (currentLayer > 0) {
                                currentLayer--;
                            }
                        }
                    })
                        .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("▲").color(0xFFFFFF))
                        .setSize(17, 17)
                        .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("▲").color(0xFFFFFF)))
                    .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                        if (clickData.mouseButton == 0 && !button.isClient()) {
                            if (currentLayer < 7) {
                                currentLayer++;
                            }
                        }
                    })
                        .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("▼").color(0xFFFFFF))
                        .setSize(17, 17)
                        .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("▼").color(0xFFFFFF)))
                    .setPos(3, 50))
            .addChild(createLayerButton())
            .setPos(10, 50);
    }

    private Widget createCarveBlock() {
        MultiChildWidget widget = new MultiChildWidget();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                widget.addChild(new ButtonWidgetBlueprint(this, i, j).setOnClick((clickData, button) -> {
                    ButtonWidgetBlueprint button1 = (ButtonWidgetBlueprint) button;
                    if (clickData.mouseButton == 0 && !button1.isClient()) {
                        button1.getBlueprint()
                            .setModifiedDataByDirectionAndLayer(
                                button1.getX(),
                                button1.getY(),
                                direction,
                                currentLayer);
                    }
                })
                    .setSize(16, 16)
                    .setPos(61 + i * 16, 54 + j * 16));
            }
        }

        return widget;
    }

    private Widget createBottomPage() {
        return new MultiChildWidget().addChild(new ButtonWidget().setOnClick((clickData, button) -> {
            if (clickData.mouseButton == 0 && !button.isClient()) {
                loadDataFromNBT(
                    button.getContext()
                        .getPlayer()
                        .getHeldItem());
            }
        })
            .setHoveredBackground(
                ModularUITextures.VANILLA_BUTTON_HOVERED,
                new Text(TFC_Core.translate("gui.AdvancedBlueprint.cancel")).color(0xFFFFFF))
            .setSize(37, 17)
            .setBackground(
                ModularUITextures.VANILLA_BUTTON_NORMAL,
                new Text(TFC_Core.translate("gui.AdvancedBlueprint.cancel")).color(0xFFFFFF))
            .setPos(162, 230))
            .addChild(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    saveDataToNBT(
                        button.getContext()
                            .getPlayer()
                            .getHeldItem());
                }
            })
                .setHoveredBackground(
                    ModularUITextures.VANILLA_BUTTON_HOVERED,
                    new Text(TFC_Core.translate("gui.AdvancedBlueprint.done")).color(0xFFFFFF))
                .setSize(37, 17)
                .setBackground(
                    ModularUITextures.VANILLA_BUTTON_NORMAL,
                    new Text(TFC_Core.translate("gui.AdvancedBlueprint.done")).color(0xFFFFFF))
                .setPos(201, 230));
    }

    private Widget createEditButton() {
        return new Row().widget(new ButtonWidget().setOnClick((clickData, button) -> {
            if (clickData.mouseButton == 0 && !button.isClient()) {
                translateLayer(direction, currentLayer, 2, false, false, false);
            }
        })
            .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("←").color(0xFFFFFF))
            .setSize(17, 17)
            .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("←").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, 3, false, false, false);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("→").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("→").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, 0, false, false, false);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("↑").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("↑").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, 1, false, false, false);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("↓").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("↓").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, -1, true, false, false);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("↻").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("↻").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, -1, true, true, false);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("↺").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("↺").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, -1, false, false, true);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("↔").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("↔").color(0xFFFFFF)))
            .widget(new ButtonWidget().setOnClick((clickData, button) -> {
                if (clickData.mouseButton == 0 && !button.isClient()) {
                    translateLayer(direction, currentLayer, -1, false, true, true);
                }
            })
                .setHoveredBackground(ModularUITextures.VANILLA_BUTTON_HOVERED, new Text("↕").color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text("↕").color(0xFFFFFF)))
            .setPos(3, 163);
    }

    private Widget createDirectionButton() {
        Column column = new Column().setSpace(5)
            .setAlignment(MainAxisAlignment.SPACE_BETWEEN, CrossAxisAlignment.START);
        for (int i = 0; i < 6; i++) {
            column.addChild(new ButtonWidgetDirection(this, Direction.values()[i]).setOnClick((clickData, button) -> {
                ButtonWidgetDirection button1 = (ButtonWidgetDirection) button;
                if (clickData.mouseButton == 0 && !button1.isClient()) {
                    direction = button1.getDirection();
                }
            })
                .setHoveredBackground(
                    ModularUITextures.VANILLA_BUTTON_HOVERED,
                    new Text(Direction.values()[i].name()).color(0xFFFFFF))
                .setSize(37, 17)
                .setBackground(
                    ModularUITextures.VANILLA_BUTTON_NORMAL,
                    new Text(Direction.values()[i].name()).color(0xFFFFFF)));
        }
        return column;
    }

    private Widget createLayerButton() {
        Column column = new Column().setSpace(3)
            .setAlignment(MainAxisAlignment.SPACE_BETWEEN, CrossAxisAlignment.START);
        for (int i = 0; i < 8; i++) {
            column.addChild(new ButtonWidgetLayer(this, i).setOnClick((clickData, button) -> {
                ButtonWidgetLayer button1 = (ButtonWidgetLayer) button;
                if (clickData.mouseButton == 0 && !button1.isClient()) {
                    currentLayer = button1.getLayer();

                }
            })
                .setHoveredBackground(
                    ModularUITextures.VANILLA_BUTTON_HOVERED,
                    new Text(String.valueOf(i)).color(0xFFFFFF))
                .setSize(17, 17)
                .setBackground(ModularUITextures.VANILLA_BUTTON_NORMAL, new Text(String.valueOf(i)).color(0xFFFFFF)));
        }
        return column;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        MovingObjectPosition mo = Helper.getMouseOverObject(player, world);

        if (mo == null || world.getBlock(mo.blockX, mo.blockY, mo.blockZ) != TFCBlocks.detailed) {
            ItemStack heldItem = player.getHeldItem();
            if (heldItem.hasTagCompound() && !heldItem.stackTagCompound.getString("Name")
                .isEmpty()) {
                this.name = heldItem.stackTagCompound.getString("Name");
            }
            UIInfos.PLAYER_HELD_ITEM_UI.open(player, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
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
        } else if (!player.isSneaking()) {
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

    public void loadDataFromNBT(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            byte[] data = nbt.getByteArray("Data");
            if (data.length == 0) {
                return;
            }
            BitSet blueprintData = TEDetailed.turnCube(data, 0, 0, 0);
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    for (int y = 0; y < 8; y++) {
                        modifiedData[x][z][y] = !blueprintData.get((x * 8 + z) * 8 + y);
                    }
                }
            }
        }
    }

    public void saveDataToNBT(ItemStack stack) {
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
        nbt.setString("Name", name);
        stack.setTagCompound(nbt);
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        return is.hasTagCompound() && is.stackTagCompound.hasKey("Name") ? is.stackTagCompound.getString("Name")
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
}
