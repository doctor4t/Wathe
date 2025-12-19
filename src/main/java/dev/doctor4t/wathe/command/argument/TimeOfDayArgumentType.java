package dev.doctor4t.wathe.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import dev.doctor4t.wathe.cca.TrainWorldComponent;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

import java.util.Arrays;
import java.util.Locale;

public class TimeOfDayArgumentType extends EnumArgumentType<TrainWorldComponent.TimeOfDay> {
    private static final Codec<TrainWorldComponent.TimeOfDay> CODEC = StringIdentifiable.createCodec(
            TimeOfDayArgumentType::getValues, name -> name.toLowerCase(Locale.ROOT)
    );

    private static TrainWorldComponent.TimeOfDay[] getValues() {
        return Arrays.stream(TrainWorldComponent.TimeOfDay.values()).toArray(TrainWorldComponent.TimeOfDay[]::new);
    }

    private TimeOfDayArgumentType() {
        super(CODEC, TimeOfDayArgumentType::getValues);
    }

    public static TimeOfDayArgumentType timeofday() {
        return new TimeOfDayArgumentType();
    }

    public static TrainWorldComponent.TimeOfDay getTimeofday(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, TrainWorldComponent.TimeOfDay.class);
    }

    @Override
    protected String transformValueName(String name) {
        return name.toLowerCase(Locale.ROOT);
    }
}
