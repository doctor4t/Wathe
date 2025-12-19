package dev.doctor4t.wathe.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public class SetMoneyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("wathe:setMoney")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(context -> execute(context.getSource(), ImmutableList.of(context.getSource().getEntityOrThrow()), IntegerArgumentType.getInteger(context, "amount")))
                                        .then(
                                                CommandManager.argument("targets", EntityArgumentType.entities())
                                                        .executes(context -> execute(context.getSource(), EntityArgumentType.getEntities(context, "targets"), IntegerArgumentType.getInteger(context, "amount")))
                                        )
                        )
        );
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets, int amount) {

        return Wathe.executeSupporterCommand(source,
                () -> {
                    for (Entity target : targets) {
                        PlayerShopComponent.KEY.get(target).setBalance(amount);
                    }
                }
        );
    }

}
