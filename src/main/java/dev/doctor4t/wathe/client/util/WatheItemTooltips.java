package dev.doctor4t.wathe.client.util;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.wathe.index.WatheItems;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WatheItemTooltips {
    public static final int COOLDOWN_COLOR = 0xC90000;
    public static final int LETTER_COLOR = 0xC5AE8B;
    public static final int REGULAR_TOOLTIP_COLOR = 0x808080;

    public static void addTooltips() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, tooltipList) -> {
            addCooldownText(WatheItems.KNIFE, tooltipList, itemStack);
            addCooldownText(WatheItems.REVOLVER, tooltipList, itemStack);
            addCooldownText(WatheItems.DERRINGER, tooltipList, itemStack);
            addCooldownText(WatheItems.GRENADE, tooltipList, itemStack);
            addCooldownText(WatheItems.LOCKPICK, tooltipList, itemStack);
            addCooldownText(WatheItems.CROWBAR, tooltipList, itemStack);
            addCooldownText(WatheItems.BODY_BAG, tooltipList, itemStack);
            addCooldownText(WatheItems.PSYCHO_MODE, tooltipList, itemStack);
            addCooldownText(WatheItems.BLACKOUT, tooltipList, itemStack);

            addTooltipForItem(WatheItems.KNIFE, itemStack, tooltipList);
            addTooltipForItem(WatheItems.REVOLVER, itemStack, tooltipList);
            addTooltipForItem(WatheItems.DERRINGER, itemStack, tooltipList);
            addTooltipForItem(WatheItems.GRENADE, itemStack, tooltipList);
            addTooltipForItem(WatheItems.PSYCHO_MODE, itemStack, tooltipList);
            addTooltipForItem(WatheItems.POISON_VIAL, itemStack, tooltipList);
            addTooltipForItem(WatheItems.SCORPION, itemStack, tooltipList);
            addTooltipForItem(WatheItems.FIRECRACKER, itemStack, tooltipList);
            addTooltipForItem(WatheItems.LOCKPICK, itemStack, tooltipList);
            addTooltipForItem(WatheItems.CROWBAR, itemStack, tooltipList);
            addTooltipForItem(WatheItems.BODY_BAG, itemStack, tooltipList);
            addTooltipForItem(WatheItems.BLACKOUT, itemStack, tooltipList);
            addTooltipForItem(WatheItems.NOTE, itemStack, tooltipList);
        });
    }

    private static void addTooltipForItem(Item item, @NotNull ItemStack itemStack, List<Text> tooltipList) {
        if (itemStack.isOf(item)) {
            tooltipList.addAll(TextUtils.getTooltipForItem(item, Style.EMPTY.withColor(REGULAR_TOOLTIP_COLOR)));
        }
    }

    private static void addCooldownText(Item item, List<Text> tooltipList, @NotNull ItemStack itemStack) {
        if (!itemStack.isOf(item)) return;
        ItemCooldownManager itemCooldownManager = MinecraftClient.getInstance().player.getItemCooldownManager();
        if (itemCooldownManager.isCoolingDown(item)) {
            ItemCooldownManager.Entry knifeEntry = itemCooldownManager.entries.get(item);
            int timeLeft = knifeEntry.endTick - itemCooldownManager.tick;
            if (timeLeft > 0) {
                int minutes = (int) Math.floor((double) timeLeft / 1200);
                int seconds = (timeLeft - (minutes * 1200)) / 20;
                String countdown = (minutes > 0 ? minutes + "m" : "") + (seconds > 0 ? seconds + "s" : "");
                tooltipList.add(Text.translatable("tip.cooldown", countdown).withColor(COOLDOWN_COLOR));
            }
        }
    }
}
