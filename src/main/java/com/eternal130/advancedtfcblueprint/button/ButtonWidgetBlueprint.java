package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.Nullable;

import com.eternal130.advancedtfcblueprint.item.AdvancedBlueprint;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

public class ButtonWidgetBlueprint extends ButtonWidget {

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int x;
    private int y;
    private static final UITexture[] Carved = {
        UITexture.partly("advancedtfcblueprint", "textures/gui/advancedBlueprint.png", 208, 160, 144, 16, 160, 32) };
    private static final UITexture[] UnCarved = {
        UITexture.partly("advancedtfcblueprint", "textures/gui/advancedBlueprint.png", 208, 160, 144, 0, 160, 16) };

    public AdvancedBlueprint getBlueprint() {
        return blueprint;
    }

    private final AdvancedBlueprint blueprint;

    public ButtonWidgetBlueprint(AdvancedBlueprint blueprint, int x, int y) {
        this.blueprint = blueprint;
        this.x = x;
        this.y = y;
    }

    public ButtonWidgetBlueprint setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public @Nullable IDrawable[] getBackground() {
        if (blueprint.getModifiedDataByDirectionAndLayer(x, y, blueprint.getDirection(), blueprint.getCurrentLayer()))
            return Carved;
        else return UnCarved;

        // if (blueprint.getModifiedData()[x][y][blueprint.getCurrentLayer()]) return Carved;
        // else return UnCarved;
    }
}
