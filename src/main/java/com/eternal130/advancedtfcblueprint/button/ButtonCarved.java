package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.eternal130.advancedtfcblueprint.Tool;
import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

public class ButtonCarved<W extends ButtonCarved<W>> extends ButtonWidget<W> {

    private final int x;
    private final int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ButtonCarved(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void drawOverlay(ModularGuiContext context, WidgetTheme widgetTheme) {}

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 1) {
            super.playClickSound();
            int currentLayer = AdvancedBlueprintUI.getCurrentLayer();
            for (int i = currentLayer; i < 8; i++) {
                Tool.setModifiedDataByDirectionAndLayer(
                    x,
                    y,
                    AdvancedBlueprintUI.getDirection(),
                    i,
                    AdvancedBlueprintUI.getModifiedData());
            }
        }
        return Result.ACCEPT;
    }

    public void clicked(int mouseButton) {
        if (mouseButton == 0) {
            super.playClickSound();
            Tool.setModifiedDataByDirectionAndLayer(
                x,
                y,
                AdvancedBlueprintUI.getDirection(),
                AdvancedBlueprintUI.getCurrentLayer(),
                AdvancedBlueprintUI.getModifiedData());
        }
    }

    @Override
    public @Nullable IDrawable getBackground() {
        if (Tool.getModifiedDataByDirectionAndLayer(
            x,
            y,
            AdvancedBlueprintUI.getDirection(),
            AdvancedBlueprintUI.getCurrentLayer(),
            AdvancedBlueprintUI.getModifiedData())) {
            return super.getOverlay();
        }
        return super.getBackground();
    }

    @Override
    public @Nullable IDrawable getHoverBackground() {
        return getBackground();
    }
}
