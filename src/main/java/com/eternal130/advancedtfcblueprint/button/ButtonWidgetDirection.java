package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.Nullable;

import com.eternal130.advancedtfcblueprint.item.AdvancedBlueprint;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

public class ButtonWidgetDirection extends ButtonWidget {

    public AdvancedBlueprint.Direction getDirection() {
        return direction;
    }

    private final AdvancedBlueprint.Direction direction;
    private final AdvancedBlueprint blueprint;

    public ButtonWidgetDirection(AdvancedBlueprint blueprint, AdvancedBlueprint.Direction direction) {
        this.blueprint = blueprint;
        this.direction = direction;
    }

    @Override
    public @Nullable IDrawable[] getBackground() {
        if (direction == blueprint.getDirection()) {
            return new IDrawable[] { ModularUITextures.VANILLA_BUTTON_DISABLED,
                new Text(direction.name()).color(0xFFFFFF) };
        } else if (super.getBackground() != null && isHovering()) {
            return super.getBackground();
        }
        return super.getBackground();
    }
}
