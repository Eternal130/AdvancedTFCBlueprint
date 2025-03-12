package com.eternal130.advancedtfcblueprint.button;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.eternal130.advancedtfcblueprint.Tool;
import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

public class ButtonEdit<W extends ButtonEdit<W>> extends ButtonWidget<W> {

    private final char type;

    public ButtonEdit(char type) {
        this.type = type;
    }

    @Override
    public void drawOverlay(ModularGuiContext context, WidgetTheme widgetTheme) {}

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 0) {
            switch (type) {
                case '←':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        2,
                        false,
                        false,
                        false)) return Result.STOP;
                    break;
                case '→':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        3,
                        false,
                        false,
                        false)) return Result.STOP;
                    break;
                case '↑':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        0,
                        false,
                        false,
                        false)) return Result.STOP;
                    break;
                case '↓':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        1,
                        false,
                        false,
                        false)) return Result.STOP;
                    break;
                case '↻':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        -1,
                        true,
                        false,
                        false)) return Result.STOP;
                    break;
                case '↺':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        -1,
                        true,
                        true,
                        false)) return Result.STOP;
                    break;
                case '↔':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        -1,
                        false,
                        false,
                        true)) return Result.STOP;
                    break;
                case '↕':
                    if (!Tool.translateLayer(
                        AdvancedBlueprintUI.getDirection(),
                        AdvancedBlueprintUI.getCurrentLayer(),
                        -1,
                        false,
                        true,
                        true)) return Result.STOP;
                    break;
            }
            super.playClickSound();
        }
        return Result.ACCEPT;
    }

}
