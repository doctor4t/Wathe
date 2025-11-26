package dev.doctor4t.trainmurdermystery.client.util;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TMMItemTooltips {
    public static final int COOLDOWN_COLOR = 0xC90000;
    public static final int LETTER_COLOR = 0xC5AE8B;
    public static final int REGULAR_TOOLTIP_COLOR = 0x808080;

    public static void addTooltips() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, tooltipList) -> {
            addCooldownText(TMMItems.KNIFE, tooltipList, itemStack);
            addCooldownText(TMMItems.REVOLVER, tooltipList, itemStack);
            addCooldownText(TMMItems.DERRINGER, tooltipList, itemStack);
            addCooldownText(TMMItems.GRENADE, tooltipList, itemStack);
            addCooldownText(TMMItems.LOCKPICK, tooltipList, itemStack);
            addCooldownText(TMMItems.CROWBAR, tooltipList, itemStack);
            addCooldownText(TMMItems.BODY_BAG, tooltipList, itemStack);
            addCooldownText(TMMItems.PSYCHO_MODE, tooltipList, itemStack);
            addCooldownText(TMMItems.BLACKOUT, tooltipList, itemStack);

            addTooltipForItem(TMMItems.KNIFE, itemStack, tooltipList);
            addTooltipForItem(TMMItems.REVOLVER, itemStack, tooltipList);
            addTooltipForItem(TMMItems.DERRINGER, itemStack, tooltipList);
            addTooltipForItem(TMMItems.GRENADE, itemStack, tooltipList);
            addTooltipForItem(TMMItems.PSYCHO_MODE, itemStack, tooltipList);
            addTooltipForItem(TMMItems.POISON_VIAL, itemStack, tooltipList);
            addTooltipForItem(TMMItems.SCORPION, itemStack, tooltipList);
            addTooltipForItem(TMMItems.FIRECRACKER, itemStack, tooltipList);
            addTooltipForItem(TMMItems.LOCKPICK, itemStack, tooltipList);
            addTooltipForItem(TMMItems.CROWBAR, itemStack, tooltipList);
            addTooltipForItem(TMMItems.BODY_BAG, itemStack, tooltipList);
            addTooltipForItem(TMMItems.BLACKOUT, itemStack, tooltipList);
            addTooltipForItem(TMMItems.NOTE, itemStack, tooltipList);
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
