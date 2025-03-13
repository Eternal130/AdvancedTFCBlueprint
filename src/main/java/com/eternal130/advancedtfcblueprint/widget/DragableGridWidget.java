package com.eternal130.advancedtfcblueprint.widget;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.eternal130.advancedtfcblueprint.button.ButtonCarved;

public class DragableGridWidget extends Grid {

    private boolean dragging = false;
    public Map<ButtonCarved<?>, Boolean> clicked = new HashMap<>();

    public DragableGridWidget() {
        listenGuiAction((IGuiAction.MouseReleased) button -> {
            boolean val = this.dragging;
            this.dragging = false;
            clicked.clear();
            return val;
        });
    }

    /**
     * Called when this widget is pressed.
     *
     * @param mouseButton mouse button that was pressed.
     * @return result that determines what happens to other widgets
     *         {@link #onMouseTapped(int)} is only called if this returns {@link Result#ACCEPT} or
     *         {@link Result#SUCCESS}
     */
    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (mouseButton == 0) {
            ModularGuiContext context = this.getContext();
            ButtonCarved<?> child = null;
            if (context.getHovered() instanceof ButtonCarved<?>) {
                child = (ButtonCarved<?>) context.getHovered();
            }
            if (child != null) {
                if (clicked.get(child) != null && clicked.get(child)) {
                    return Result.STOP;
                }
                child.clicked(mouseButton);
                clicked.put(child, true);
            }
        }
        return Result.SUCCESS;
    }

    /**
     * Called when this widget was clicked and mouse is now dragging.
     *
     * @param mouseButton    mouse button that drags
     * @param timeSinceClick time since drag began
     */
    @Override
    public void onMouseDrag(int mouseButton, long timeSinceClick) {
        onMousePressed(mouseButton);
    }
}
