package dev.doctor4t.wathe.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.doctor4t.wathe.api.MapEffect;
import dev.doctor4t.wathe.api.WatheMapEffects;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapEffectArgumentType implements ArgumentType<Identifier> {
    private static final Collection<String> EXAMPLES = Stream.of(WatheMapEffects.GENERIC, WatheMapEffects.HARPY_EXPRESS_NIGHT)
            .map(key -> key.identifier.toString())
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType INVALID_GAME_MODE_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("wathe.argument.map_effect.invalid", id)
    );

    public Identifier parse(StringReader stringReader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof CommandSource
                ? CommandSource.suggestIdentifiers(WatheMapEffects.MAP_EFFECTS.keySet().stream().toList(), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static MapEffectArgumentType mapEffect() {
        return new MapEffectArgumentType();
    }

    public static MapEffect getMapEffectArgument(CommandContext<ServerCommandSource> context, String name) throws CommandSyntaxException {
        Identifier identifier = context.getArgument(name, Identifier.class);
        MapEffect mapEffect = WatheMapEffects.MAP_EFFECTS.get(identifier);
        if (mapEffect == null) {
            throw INVALID_GAME_MODE_EXCEPTION.create(identifier);
        } else {
            return mapEffect;
        }
    }
}
