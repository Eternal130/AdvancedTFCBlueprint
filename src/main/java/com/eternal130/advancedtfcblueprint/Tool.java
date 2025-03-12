package com.eternal130.advancedtfcblueprint;

import com.eternal130.advancedtfcblueprint.ui.AdvancedBlueprintUI;

public class Tool {

    public enum Direction {
        TOP,
        BOTTOM,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    public static boolean getModifiedDataByDirectionAndLayer(int x, int z, Tool.Direction dir, int y,
        boolean[][][] modifiedData) {
        return switch (dir) {
            case TOP -> modifiedData[x][z][7 - y];
            case BOTTOM -> modifiedData[7 - x][z][y];
            case NORTH -> modifiedData[7 - x][y][7 - z];
            case SOUTH -> modifiedData[x][7 - y][7 - z];
            case WEST -> modifiedData[y][x][7 - z];
            case EAST -> modifiedData[7 - y][7 - x][7 - z];
        };
    }

    public static void setModifiedDataByDirectionAndLayer(int x, int z, Direction dir, int y,
        boolean[][][] modifiedData) {
        switch (dir) {
            case TOP -> modifiedData[x][z][7 - y] = !modifiedData[x][z][7 - y];
            case BOTTOM -> modifiedData[7 - x][z][y] = !modifiedData[7 - x][z][y];
            case NORTH -> modifiedData[7 - x][y][7 - z] = !modifiedData[7 - x][y][7 - z];
            case SOUTH -> modifiedData[x][7 - y][7 - z] = !modifiedData[x][7 - y][7 - z];
            case WEST -> modifiedData[y][x][7 - z] = !modifiedData[y][x][7 - z];
            case EAST -> modifiedData[7 - y][7 - x][7 - z] = !modifiedData[7 - y][7 - x][7 - z];
        }
    }

    public static boolean translateLayer(Tool.Direction direction, int layer, int mov, boolean rotation,
        boolean reverse, boolean mirror) {
        boolean[][] layerData = new boolean[8][8];
        boolean[][] newLayerData = new boolean[8][8];

        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                switch (direction) {
                    case TOP -> layerData[x][z] = AdvancedBlueprintUI.getModifiedData()[x][z][7 - layer];
                    case BOTTOM -> layerData[x][z] = AdvancedBlueprintUI.getModifiedData()[7 - x][z][layer];
                    case NORTH -> layerData[x][z] = AdvancedBlueprintUI.getModifiedData()[7 - x][layer][7 - z];
                    case SOUTH -> layerData[x][z] = AdvancedBlueprintUI.getModifiedData()[x][7 - layer][7 - z];
                    case WEST -> layerData[x][z] = AdvancedBlueprintUI.getModifiedData()[layer][x][7 - z];
                    case EAST -> layerData[x][z] = AdvancedBlueprintUI.getModifiedData()[7 - layer][7 - x][7 - z];
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
                    case TOP -> AdvancedBlueprintUI.getModifiedData()[x][z][7 - layer] = newLayerData[x][z];
                    case BOTTOM -> AdvancedBlueprintUI.getModifiedData()[7 - x][z][layer] = newLayerData[x][z];
                    case NORTH -> AdvancedBlueprintUI.getModifiedData()[7 - x][layer][7 - z] = newLayerData[x][z];
                    case SOUTH -> AdvancedBlueprintUI.getModifiedData()[x][7 - layer][7 - z] = newLayerData[x][z];
                    case WEST -> AdvancedBlueprintUI.getModifiedData()[layer][x][7 - z] = newLayerData[x][z];
                    case EAST -> AdvancedBlueprintUI.getModifiedData()[7 - layer][7 - x][7 - z] = newLayerData[x][z];
                }
            }
        }
        return true;
    }
}
