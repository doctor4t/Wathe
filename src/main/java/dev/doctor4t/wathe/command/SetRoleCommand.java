package dev.doctor4t.wathe.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.command.argument.RoleArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public class SetRoleCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("wathe:setRole")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("role", RoleArgumentType.role())
                                        .executes(context -> execute(context.getSource(), ImmutableList.of(context.getSource().getEntityOrThrow()), RoleArgumentType.getRoleArgument(context, "role")))
                                        .then(
                                                CommandManager.argument("targets", EntityArgumentType.entities())
                                                        .executes(context -> execute(context.getSource(), EntityArgumentType.getEntities(context, "targets"), RoleArgumentType.getRoleArgument(context, "role")))
                                        )
                        )
        );
    }

    private static int execute(ServerCommandSource source, Collection<? extends Entity> targets, Role role) {
        for (Entity target : targets) {
            GameWorldComponent.KEY.get(source.getWorld()).addRole(target.getUuid(), role);
        }
        return 1;
    }
}
