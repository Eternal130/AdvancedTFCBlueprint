package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.eternal130.advancedtfcblueprint.Tool;
import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

public class ButtonDirection<W extends ButtonDirection<W>> extends ButtonWidget<W> {

    private final Tool.Direction direction;

    public Tool.Direction getDirection() {
        return direction;
    }

    public ButtonDirection(Tool.Direction direction) {
        this.direction = direction;
    }

    @Override
    public void drawOverlay(ModularGuiContext context, WidgetTheme widgetTheme) {}

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 0) {
            super.playClickSound();
            AdvancedBlueprintUI.setDirection(this.direction);
        }
        return Result.ACCEPT;
    }

    @Override
    public @NotNull Result onMouseTapped(int mouseButton) {
        return super.onMouseTapped(mouseButton);
    }

    @Override
    public @Nullable IDrawable getBackground() {
        if (this.getDirection() == AdvancedBlueprintUI.getDirection()) {
            return super.getOverlay();
        }
        return super.getBackground();
    }

    @Override
    public @Nullable IDrawable getHoverBackground() {
        if (this.getDirection() == AdvancedBlueprintUI.getDirection()) {
            return super.getOverlay();
        }
        return super.getHoverBackground();
    }
}
