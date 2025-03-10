# Advanced Blueprint
[中文版](README_CN.md)

The Advanced Blueprint aims to enhance the functionality of TFC blueprints. This mod is a reconstruction of the [Advanced Blueprint](https://terrafirmacraft.com/f/topic/7639-tfc-07914-advanced-blueprint-v03/). Since the original mod's files or code were unavailable, a similar mod was developed independently.

## Item Usage

- **Import Schematic**: Use the new blueprint, hold `Shift` and right-click on a detailed block to import the block's schematic into the blueprint. You can also right-click in the air to edit the blueprint directly.
- **Apply Schematic**: Once the blueprint stores a schematic, right-click on a detailed block to apply the blueprint's schematic. Similar to TFC blueprints, a hammer and chisel are required in the hotbar to apply the schematic.
- **Edit Schematic**: Right-click in the air to open the Advanced Blueprint GUI for schematic editing.

## GUI Usage

- **GUI Title**: The top section is the GUI title, which can be ignored.
- **Blueprint Naming**: Below the title, you can input any string as the blueprint's name. After saving, this name will be used as the item's name.
- **Blueprint Controls**:
  - Left-click to toggle whether the current position is carved. The default state is uncarved, displayed as a slashed grid.
- **Operation Buttons**:
  - Below the blueprint controls, there are 8 buttons that allow you to perform the following operations on the carved schematic:
    - Move left
    - Move right
    - Move up
    - Move down
    - Rotate 90° clockwise
    - Rotate 90° counterclockwise
    - Mirror left-right
    - Mirror up-down
- **View Direction**:
  - On the left side of the blueprint controls, there are 6 buttons to select the viewing direction. The default is `TOP` (viewing the blueprint from above).
- **View Layer**:
  - On the right side of the blueprint controls, buttons allow you to select the current viewing layer. The layer closest to you is `0`, and the farthest is `7`.
- **Save and Cancel**:
  - The two buttons at the bottom right are used to either cancel the current changes or save the blueprint:
    - Cancel: Reads data from NBT and overwrites the current changes.
    - Save: Stores the current data into NBT.

## Notes

- The blueprint can be edited and used infinitely without being consumed, making its recipe relatively expensive.
