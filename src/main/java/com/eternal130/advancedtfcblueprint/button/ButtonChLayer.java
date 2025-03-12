package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

public class ButtonChLayer<W extends ButtonChLayer<W>> extends ButtonWidget<W> {

    private final int dir;

    public ButtonChLayer(int dir) {
        this.dir = dir;
    }

    @Override
    public void drawOverlay(ModularGuiContext context, WidgetTheme widgetTheme) {}

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 0) {
            if (dir == 0) {
                if (AdvancedBlueprintUI.getCurrentLayer() == 0) {
                    return Result.STOP;
                }
            } else {
                if (AdvancedBlueprintUI.getCurrentLayer() == 7) {
                    return Result.STOP;
                }
            }
            super.playClickSound();
            if (dir == 0) {
                AdvancedBlueprintUI.setCurrentLayer(AdvancedBlueprintUI.getCurrentLayer() - 1);
            } else {
                AdvancedBlueprintUI.setCurrentLayer(AdvancedBlueprintUI.getCurrentLayer() + 1);
            }
        }
        return Result.ACCEPT;
    }

    @Override
    public @Nullable IDrawable getBackground() {
        if (dir == 0) {
            if (AdvancedBlueprintUI.getCurrentLayer() == 0) {
                return super.getOverlay();
            }
        } else {
            if (AdvancedBlueprintUI.getCurrentLayer() == 7) {
                return super.getOverlay();
            }
        }
        return super.getBackground();
    }

    @Override
    public @Nullable IDrawable getHoverBackground() {
        if (dir == 0) {
            if (AdvancedBlueprintUI.getCurrentLayer() == 0) {
                return super.getOverlay();
            }
        } else {
            if (AdvancedBlueprintUI.getCurrentLayer() == 7) {
                return super.getOverlay();
            }
        }
        return super.getHoverBackground();
    }
}
