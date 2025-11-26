# Train Murder Mystery - Map Setup Guide

This guide explains how to set up the dynamic game areas, build the train, and configure the map using the **Zone Wand**.

## 1. The Zone Wand
The Zone Wand is the primary tool used to define game boundaries and the train structure.

* **Get the item:**
    ```
    /give @s trainmurdermystery:zone_wand
    ```

### Controls
* **Right Click:** Set **Position A** (The first corner).
* **Shift + Right Click:** Set **Position B** (The second corner).

---

## 2. Defining Zones
Once you have selected two positions with the wand, run the corresponding command to save that area.

### The Zones

| Command Sub-argument | Description |
| :--- | :--- |
| `resetTemplateArea` | **The Builder's Area.** This is where you build the train. It allows you to edit the map safely. This area is copied to the "Paste Area" when the game starts. |
| `resetPasteArea` | **The Active Train.** This is where the train actually appears during gameplay. **Do not build here directly**; it gets overwritten by the Template. |
| `playArea` | **The Bounds.** Players are limited to this box during the game. If they go outside, they may be teleported back or killed. |
| `readyArea` | **The Lobby.** Players must stand here to be counted as "Ready" to start the game. |

### Usage Example
1.  Select the bottom-left corner of your build with the Wand.
2.  Select the top-right corner of your build with the Wand.
3.  Run the command:
    ```
    /tmm:setzone resetTemplateArea
    ```

---

## 3. Important Building Rules
The mod enforces specific rules to ensure the train works correctly and resets properly.

### 🛑 Height Limit (Y-Level)
* **Rule:** You cannot select any block below **Y = 64**.

### 📐 Symmetry Requirement
* **Applies to:** `resetTemplateArea` and `resetPasteArea` (The Train).
* **Rule:** The train must be perfectly centered on the track axis (**Z = -536**).
* **Example:**
    * ✅ Left side is at Z = -530 (6 blocks away). Right side is at Z = -542 (6 blocks away). **Valid.**
    * ❌ Left side is at Z = -530. Right side is at Z = -540. **Invalid.**

---

## 4. Testing & Troubleshooting

### Force Reset Command

``/tmm:forcereset``

* **Note:** If the chunks are unloaded (too far away), the command will tell you it is loading them. simply wait 1 second and run the command again.

### Common Issues
* **"Train is not symmetric!"**: Check your Z coordinates. Calculate the distance from -536 for both A and B positions.
* **"Under Block Limit!"**: You are trying to select deep underground. Move your selection up to Y=64 or higher.