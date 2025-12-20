package dev.doctor4t.trainmurdermystery.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoleArgumentType implements ArgumentType<Identifier> {
    private static final Collection<String> EXAMPLES = Stream.of(
            TMMRoles.CIVILIAN,
                    TMMRoles.KILLER,
                    TMMRoles.VIGILANTE,
                    TMMRoles.LOOSE_END,
                    TMMRoles.DISCOVERY_CIVILIAN
            )
            .map(key -> key.identifier().toString())
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType INVALID_ROLE_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("argument.roles.invalid", id)
    );

    public Identifier parse(StringReader stringReader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof CommandSource
                ? CommandSource.suggestIdentifiers(TMMRoles.ROLES.stream().map(Role::identifier).toList(), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static RoleArgumentType role() {
        return new RoleArgumentType();
    }

    public static Role getRoleArgument(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        Identifier identifier = context.getArgument(name, Identifier.class);
        Optional<Role> role = TMMRoles.ROLES.stream().filter(it -> it.identifier().equals(identifier)).findFirst();
        if (role.isEmpty()) {
            throw INVALID_ROLE_EXCEPTION.create(identifier);
        } else {
            return role.get();
        }
    }
}
