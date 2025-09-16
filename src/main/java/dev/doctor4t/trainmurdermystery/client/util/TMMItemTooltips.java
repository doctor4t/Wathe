package dev.doctor4t.trainmurdermystery.client.util;

import dev.doctor4t.trainmurdermystery.client.TrainMurderMysteryClient;
import dev.doctor4t.trainmurdermystery.game.GameLoop;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TrainMurderMysteryItems;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class TMMItemTooltips {
    private static final int COOLDOWN_COLOR = 0xC90000;
    private static final int LETTER_COLOR = 0xC5AE8B;
    private static final int REGULAR_TOOLTIP_COLOR = 0x808080;

    public static void addTooltips() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, tooltipList) -> {
            addTooltipsForItem(TrainMurderMysteryItems.KNIFE, 3, itemStack, tooltipList);
            addCooldownText(TrainMurderMysteryItems.KNIFE, tooltipList, itemStack);

            addTooltipsForItem(TrainMurderMysteryItems.LOCKPICK, 2, itemStack, tooltipList);
            addCooldownText(TrainMurderMysteryItems.LOCKPICK, tooltipList, itemStack);

            addRevolverTooltips(itemStack, tooltipList);

            addTooltipsForItem(TrainMurderMysteryItems.BODY_BAG, 1, itemStack, tooltipList);

            addLetterTooltips(itemStack, tooltipList);
        });
    }

    private static void addRevolverTooltips(ItemStack itemStack, List<Text> tooltipList) {
        Item item = TrainMurderMysteryItems.REVOLVER;
        if (itemStack.isOf(item)) {
            Integer i = itemStack.get(TMMDataComponentTypes.BULLETS);
            i = i != null ? i : 6;
            tooltipList.add(Text.translatable("tip." + item.getTranslationKey().substring(24) + ".rounds", i).withColor(i <= 0 ? COOLDOWN_COLOR : 0xC1CCD1));
            addTooltipsForItem(item, 2, itemStack, tooltipList);
        }
    }

    private static void addLetterTooltips(ItemStack itemStack, List<Text> tooltipList) {
        Item item = TrainMurderMysteryItems.LETTER;
        if (itemStack.isOf(item)) {
            if (itemStack.getName().getString().equals(Text.translatable(TrainMurderMysteryItems.LETTER.getTranslationKey()+".instructions").getString())) {
                String tooltipString = "tip." + item.getTranslationKey().substring(24) + ".hitman.tooltip";

                tooltipList.add(Text.translatable(tooltipString + "1").withColor(LETTER_COLOR));

                for (UUID target : TrainMurderMysteryClient.getTargets()) {
                    PlayerEntity player = MinecraftClient.getInstance().world.getPlayerByUuid(target);

                    UnaryOperator<Style> stylizer = style -> GameLoop.isPlayerEliminated(player) ? style.withStrikethrough(true).withColor(0x1B8943) : style.withColor(0x8A1B29);
                    PlayerListEntry playerListEntry = TrainMurderMysteryClient.PLAYER_ENTRIES_CACHE.get(target);
                    if (playerListEntry != null)
                        tooltipList.add(Text.translatable(tooltipString + ".target", playerListEntry.getProfile().getName()).styled(stylizer));
                }

                for (int i = 2; i <= 4; i++) {
                    tooltipList.add(Text.translatable(tooltipString + i).withColor(LETTER_COLOR));
                }
            } else if (itemStack.getName().getString().equals(Text.translatable(TrainMurderMysteryItems.LETTER.getTranslationKey()+".notes").getString())) {
                for (int i = 1; i <= 4; i++) {
                    tooltipList.add(Text.translatable("tip." + item.getTranslationKey().substring(24) + ".detective.tooltip" + i).withColor(LETTER_COLOR));
                }
            }
        }
    }

    private static void addTooltipsForItem(Item item, int tooltipLineCount, ItemStack itemStack, List<Text> tooltipList) {
        if (itemStack.isOf(item)) {
            for (int i = 1; i <= tooltipLineCount; i++) {
                tooltipList.add(Text.translatable("tip." + item.getTranslationKey().substring(24) + ".tooltip" + i).withColor(REGULAR_TOOLTIP_COLOR));
            }
        }
    }

    private static void addCooldownText(Item item, List<Text> tooltipList, ItemStack itemStack) {
        if (itemStack.isOf(item)) {
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
}
