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

public class TMMLangGen extends FabricLanguageProvider {

    public TMMLangGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
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

        builder.add("lobby.players.count", "Players boarded: %s / %s");
        builder.add("lobby.autostart.active", "Game will start once 6+ players are boarded");
        builder.add("lobby.autostart.time", "Game starting in %ss");
        builder.add("lobby.autostart.starting", "Game starting");

        builder.add("announcement.role.civilian", "Civilian!");
        builder.add("announcement.role.vigilante", "Vigilante!");
        builder.add("announcement.role.killer", "Killer!");
        builder.add("announcement.role.loose_end", "Loose End!");
        builder.add("announcement.title.civilian", "Civilians");
        builder.add("announcement.title.vigilante", "Vigilantes");
        builder.add("announcement.title.killer", "Killers");
        builder.add("announcement.title.loose_end", "Loose Ends");

        builder.add("announcement.welcome", "Welcome aboard %s");
        builder.add("announcement.premise", "There is a killer aboard the train.");
        builder.add("announcement.premises", "There are %s killers aboard the train.");
        builder.add("announcement.goal.civilian", "Stay safe and survive till the end of the ride.");
        builder.add("announcement.goal.vigilante", "Eliminate any murderers and protect the civilians.");
        builder.add("announcement.goal.killer", "Eliminate a passenger to succeed, before time runs out.");
        builder.add("announcement.goals.civilian", "Stay safe and survive till the end of the ride.");
        builder.add("announcement.goals.vigilante", "Eliminate any murderers and protect the civilians.");
        builder.add("announcement.goals.killer", "Eliminate all civilians before time runs out.");
        builder.add("announcement.win.civilian", "Passengers Win!");
        builder.add("announcement.win.vigilante", "Passengers Win!");
        builder.add("announcement.win.killer", "Killers Win!");
        builder.add("announcement.win.loose_end", "%s Wins!");
        builder.add("announcement.loose_ends.welcome", "Welcome aboard... Loose End.");
        builder.add("announcement.loose_ends.premise", "Everybody on the train has a derringer and a knife.");
        builder.add("announcement.loose_ends.goal", "Tie all loose ends before they tie you. Good luck.");
        builder.add("announcement.loose_ends.winner", "%s Wins!");

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
        builder.add("tip.derringer.used", "Used: cannot be shot anymore, get a kill for another chance!");

        builder.add("tip.cooldown", "On cooldown: %s");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.KNIFE) + ".tooltip", "Right-click, hold for a second and get close to your victim\nAfter a kill, cannot be used for 1 minute\nAttack to knock back / push a player (no cooldown)");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.REVOLVER) + ".tooltip", "Point, right-click and shoot\nDrops if you kill an innocent");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.DERRINGER) + ".tooltip", "Point, right-click and shoot\nCan only be shot once, so make it count!\nShot is replenished after a kill");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.GRENADE) + ".tooltip", "Right-click to throw, explodes on impact\nGood to clear groups of people, but be wary of the blast radius!\nSingle use, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.PSYCHO_MODE) + ".tooltip", "\"Do you like hurting other people?\"\nHides your identity and allows you to go crazy with a bat for 30 seconds\nBat kills on full swing and cannot be unselected for the duration of the ability\nActivated instantly upon purchase, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.POISON_VIAL) + ".tooltip", "Slip in food or drinks to poison the next pickup");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.FIRECRACKER) + ".tooltip", "Detonates 15 seconds after being placed on ground\nGood to simulate gunshots and lure people");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.SCORPION) + ".tooltip", "Slip in a bed to poison the next person looking for a rest");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.LOCKPICK) + ".tooltip", "Use on any locked door to open it (no cooldown)\nSneak-use on a door to jam it for 1 minute (3 minute cooldown)");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.CROWBAR) + ".tooltip", "Use on any door to open it permanently");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.BODY_BAG) + ".tooltip", "Use on a dead body to bag it up and remove it\nSingle use, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.BLACKOUT) + ".tooltip", "Turn off all lights aboard for 15 to 20 seconds\nUse your instinct [left-alt] to see your targets in the dark\nActivated instantly on purchase, 5 minute cooldown");
        builder.add(TextUtils.getItemTranslationKey(TMMItems.NOTE) + ".tooltip", "Write a message and pin it for others to see\nSneak-use to write a message, then use on a wall or floor to place\nInvisible in hand");

        builder.add("game.win.killers", "The killers reached their kill count, they win!");
        builder.add("game.win.passengers", "All killers were eliminated: the passengers win!");
        builder.add("game.win.time", "The killers ran out of time: the passengers win!");
        builder.add("game.win.loose_end", "They tied all of their loose ends!");

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
        builder.add("game.start_error.not_enough_players", "Game cannot start: 6 players minimum are required.");
        builder.add("game.start_error.game_running", "Game cannot start: a game is already running. Please try again from the lobby.");

        builder.add("tmm.gui.reset", "Clear");

        builder.add("commands.supporter_only", "Super silly supporter commands are reserved for Ko-Fi and YouTube members; if you wanna try them out, please consider supporting! <3");

        builder.add("trainmurdermystery.midnightconfig.title", "The Last Voyage of the Harpy Express - Config");
        builder.add("trainmurdermystery.midnightconfig.ultraPerfMode", "Ultra Performance Mode");
        builder.add("trainmurdermystery.midnightconfig.ultraPerfMode.tooltip", "Disables scenery for a worse visual experience but maximum performance. Lowers render distance to 2.");
        builder.add("trainmurdermystery.midnightconfig.disableScreenShake", "Disable Screen Shake");

        builder.add("credits.trainmurdermystery.thank_you", "Thank you for playing The Last Voyage of the Harpy Express!\nMe and my team spent a lot of time working\non this mod and we hope you enjoy it.\nIf you do and wish to make a video or stream\nplease make sure to credit my channel,\nvideo and the mod page!\n - RAT / doctor4t");
    }
}
