package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.Nullable;

import com.eternal130.advancedtfcblueprint.item.AdvancedBlueprint;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

public class ButtonWidgetLayer extends ButtonWidget {

    public AdvancedBlueprint getBlueprint() {
        return blueprint;
    }

    @Override
    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    private int layer;
    private final AdvancedBlueprint blueprint;

    public ButtonWidgetLayer(AdvancedBlueprint blueprint, int layer) {
        this.layer = layer;
        this.blueprint = blueprint;
    }

    @Override
    public @Nullable IDrawable[] getBackground() {
        if (layer == blueprint.getCurrentLayer()) {
            return new IDrawable[] { ModularUITextures.VANILLA_BUTTON_DISABLED,
                new Text(String.valueOf(layer)).color(0xFFFFFF) };
        } else if (super.getBackground() != null && isHovering()) {
            return super.getBackground();
        }
        return super.getBackground();
    }

}
