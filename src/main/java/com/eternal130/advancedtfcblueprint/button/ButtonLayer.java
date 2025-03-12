package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

public class ButtonLayer<W extends ButtonLayer<W>> extends ButtonWidget<W> {

    private final int layer;

    public ButtonLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public void drawOverlay(ModularGuiContext context, WidgetTheme widgetTheme) {}

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 0) {
            super.playClickSound();
            AdvancedBlueprintUI.setCurrentLayer(this.layer);
        }
        return Result.ACCEPT;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        if (this.getLayer() == AdvancedBlueprintUI.getCurrentLayer()) {
            return super.getOverlay();
        }
        return super.getBackground();
    }

    @Override
    public @Nullable IDrawable getHoverBackground() {
        if (this.getLayer() == AdvancedBlueprintUI.getCurrentLayer()) {
            return super.getOverlay();
        }
        return super.getHoverBackground();
    }
}
