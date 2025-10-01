package dev.doctor4t.trainmurdermystery.datagen;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.trainmurdermystery.index.TMMBlocks;
import dev.doctor4t.trainmurdermystery.index.TMMEntities;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TrainMurderMysteryLangGen extends FabricLanguageProvider {

    public TrainMurderMysteryLangGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, @NotNull TranslationBuilder builder) {
        TMMBlocks.registrar.generateLang(wrapperLookup, builder);
        TMMItems.registrar.generateLang(wrapperLookup, builder);
        TMMEntities.registrar.generateLang(wrapperLookup, builder);

//        builder.add(TMMItems.LETTER.getTranslationKey() + ".instructions", "Instructions");
//        builder.add("tip.letter.killer.tooltip1", "Thank you for taking this job. Please eliminate the following targets:");
//        builder.add("tip.letter.killer.tooltip.target", "- %s");
//        builder.add("tip.letter.killer.tooltip2", "Please do so with the utmost discretion and do not get caught. Good luck.");
//        builder.add("tip.letter.killer.tooltip3", "");
//        builder.add("tip.letter.killer.tooltip4", "P.S.: Don't forget to use your instinct [Left Alt] and use the train's exterior to relocate.");
//
//        builder.add(TMMItems.LETTER.getTranslationKey() + ".notes", "Notes");
//        builder.add("tip.letter.detective.tooltip1", "Multiple homicides, several wealthy victims.");
//        builder.add("tip.letter.detective.tooltip2", "Have to be linked... Serial killer? Assassin? Killer?");
//        builder.add("tip.letter.detective.tooltip3", "Potential next victims frequent travelers of the Harpy Express.");
//        builder.add("tip.letter.detective.tooltip4", "Perfect situation to corner but need to keep targets safe.");

        builder.add("tip.letter.name", "Dear %s, welcome aboard the Harpy Express!");
        builder.add("tip.letter.room", "Please find attached your ticket as well as the key for accessing");
        builder.add("tip.letter.room.grand_suite", "the Grand Suite");
        builder.add("tip.letter.room.cabin_suite", "your Cabin Suite");
        builder.add("tip.letter.room.twin_cabin", "your Twin Cabin");
        builder.add("tip.letter.tooltip1", "%s for your trip on the 1st of January 1923.");
        builder.add("tip.letter.tooltip2", "La Sirène wishes you a pleasant and safe voyage.");

        builder.add("itemGroup.trainmurdermystery.building", "TrainMurderMystery: Building Blocks");
        builder.add("itemGroup.trainmurdermystery.decoration", "TrainMurderMystery: Decoration & Functional");
        builder.add("itemGroup.trainmurdermystery.equipment", "TrainMurderMystery: Equipment");

        builder.add("container.cargo_box", "Cargo Box");
        builder.add("container.cabinet", "Cabinet");
        builder.add("subtitles.block.cargo_box.close", "Cargo Box closes");
        builder.add("subtitles.block.cargo_box.open", "Cargo Box opens");
        builder.add("subtitles.block.door.toggle", "Door operates");
        builder.add("subtitles.item.crowbar.pry", "Crowbar pries door");

        builder.add("tip.door.locked", "This door is locked and cannot be opened.");
        builder.add("tip.door.requires_key", "This door is locked and requires a key to be opened.");
        builder.add("tip.door.requires_different_key", "This door is locked and requires a different key to be opened.");
        builder.add("tip.door.jammed", "This door is jammed and cannot be opened at the moment!");

        builder.add("tip.cooldown", "On cooldown: %s");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.KNIFE) + ".tooltip", "Right-click, hold for a second and get close to your victim\nAfter a kill, cannot be used for 1 minute\nAttack to knock back / push a player (no cooldown)");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.REVOLVER) + ".tooltip", "Point, right-click and shoot\nDrops if you kill an innocent");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.GRENADE) + ".tooltip", "Right-click to throw, explodes on impact\nDoes not affect killers, good to clear groups of people\nSingle use, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.PSYCHO_MODE) + ".tooltip", "\"Do you like hurting other people?\"\nHides your identity and allows you to go crazy with a bat for 30 seconds\nBat kills on full swing and cannot be unselected for the duration of the ability\nActivated instantly upon purchase, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.POISON_VIAL) + ".tooltip", "Slip in food or drinks to poison the next pickup");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.FIRECRACKER) + ".tooltip", "Detonates 15 seconds after being placed on ground\nGood to simulate gunshots and lure people");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.SCORPION) + ".tooltip", "Slip in a bed to poison the next person looking for a rest");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.LOCKPICK) + ".tooltip", "Use on any locked door to open it (no cooldown)\nSneak-use on a door to jam it for 1 minute (3 minute cooldown)");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.CROWBAR) + ".tooltip", "Use on any door to open it permanently (3 minute cooldown)");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.BODY_BAG) + ".tooltip", "Use on a dead body to bag it up and remove it\nSingle use, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.BLACKOUT) + ".tooltip", "Turn off all lights aboard for up to 12 seconds\nActivated instantly on purchase, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.NOTE) + ".tooltip", "Write a message and pin it for others to see\nSneak-use to write a message, then use on a wall or floor to place\nInvisible in hand");

        builder.add("game.win.killers", "The killers reached their kill count, they win!");
        builder.add("game.win.passengers", "All killers were eliminated: the passengers win!");
        builder.add("game.win.time", "The killers ran out of time: the passengers win!");

        builder.add("key.trainmurdermystery.instinct", "Instinct");
        builder.add("category.trainmurdermystery.keybinds", "Train Murder Mystery");

        builder.add("task.feel", "You feel like ");
        builder.add("task.fake", "You could fake ");
        builder.add("task.sleep", "getting some sleep.");
        builder.add("task.outside", "getting some fresh air.");
        builder.add("task.drink", "getting a drink.");
        builder.add("task.eat", "getting a snack.");
        builder.add("game.player.stung", "You feel something stinging you in your sleep.");
        builder.add("game.psycho_mode.time", "Psycho Mode: %s");
        builder.add("game.psycho_mode.text", "Kill them all!");
        builder.add("game.psycho_mode.over", "Psycho Mode Over!");
        builder.add("game.tip.cohort", "Killer Cohort");
        builder.add("game.not_enough_players", "Game cannot start: 6 players minimum are required.");
    }
}
