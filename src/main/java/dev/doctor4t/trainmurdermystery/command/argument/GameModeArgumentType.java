package dev.doctor4t.trainmurdermystery.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMGameModes;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModeArgumentType implements ArgumentType<Identifier> {
    private static final Collection<String> EXAMPLES = Stream.of(TMMGameModes.MURDER, TMMGameModes.DISCOVERY, TMMGameModes.LOOSE_ENDS)
            .map(key -> key.identifier.toString())
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType INVALID_GAME_MODE_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("argument.game_mode.invalid", id)
    );

    public Identifier parse(StringReader stringReader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof CommandSource
                ? CommandSource.suggestIdentifiers(TMMGameModes.GAME_MODES.keySet().stream().toList(), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static GameModeArgumentType gameMode() {
        return new GameModeArgumentType();
    }

    public static GameMode getGameModeArgument(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        Identifier identifier = context.getArgument(name, Identifier.class);
        GameMode gameMode = TMMGameModes.GAME_MODES.get(identifier);
        if (gameMode == null) {
            throw INVALID_GAME_MODE_EXCEPTION.create(identifier);
        } else {
            return gameMode;
        }
    }
}
