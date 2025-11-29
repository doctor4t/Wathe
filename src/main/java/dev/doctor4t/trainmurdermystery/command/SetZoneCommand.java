package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.Arrays;
import java.util.Optional;

public class SetZoneCommand {

    private static final SimpleCommandExceptionType INVALID_ZONE_TYPE = new SimpleCommandExceptionType(Text.literal("Invalid zone type"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:setzone")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("type", StringArgumentType.word())
                        .suggests((context, builder) -> CommandSource.suggestMatching(
                                Arrays.stream(ZoneType.values()).map(ZoneType::getName), builder
                        ))
                        .executes(ctx -> {
                            String inputName = StringArgumentType.getString(ctx, "type");
                            ZoneType type = ZoneType.byName(inputName)
                                    .orElseThrow(INVALID_ZONE_TYPE::create);
                            return setZone(ctx, type);
                        }))
        );
    }

    private enum ZoneType {
        PLAY_AREA("playArea"),
        READY_AREA("readyArea"),
        RESET_TEMPLATE("resetTemplateArea"),
        RESET_PASTE("resetPasteArea");

        private final String name;

        ZoneType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Optional<ZoneType> byName(String name) {
            return Arrays.stream(values())
                    .filter(z -> z.name.equals(name))
                    .findFirst();
        }
    }

    private static int setZone(CommandContext<ServerCommandSource> context, ZoneType type) {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getPlayer();

        if (player == null) return 0;

        ItemStack stack = player.getMainHandStack();
        if (!stack.isOf(TMMItems.ZONE_WAND)) {
            source.sendError(Text.translatable("commands.trainmurdermystery.setzone.error.no_wand"));
            return 0;
        }

        BlockPos posA = stack.get(TMMDataComponentTypes.POS_A);
        BlockPos posB = stack.get(TMMDataComponentTypes.POS_B);

        if (posA == null || posB == null) {
            source.sendError(Text.translatable("commands.trainmurdermystery.setzone.error.no_positions"));
            return 0;
        }

        double minX = Math.min(posA.getX(), posB.getX());
        double minY = Math.min(posA.getY(), posB.getY());
        double minZ = Math.min(posA.getZ(), posB.getZ());
        double maxX = Math.max(posA.getX(), posB.getX()) + 1;
        double maxY = Math.max(posA.getY(), posB.getY()) + 1;
        double maxZ = Math.max(posA.getZ(), posB.getZ()) + 1;

        // Lock floor logic (Template Area only)
        if (type == ZoneType.RESET_TEMPLATE) {
            if (minY != 64.0) {
                minY = 64.0;
                source.sendFeedback(() -> Text.translatable("commands.trainmurdermystery.setzone.warning.y_locked"), false);
            }
        }

        Box newZone = new Box(minX, minY, minZ, maxX, maxY, maxZ);
        AreasWorldComponent areas = AreasWorldComponent.KEY.get(source.getWorld());

        switch (type) {
            case PLAY_AREA -> areas.setPlayArea(newZone);
            case READY_AREA -> areas.setReadyArea(newZone);
            case RESET_TEMPLATE -> areas.setResetTemplateArea(newZone);
            case RESET_PASTE -> areas.setResetPasteArea(newZone);
        }

        areas.sync();
        source.sendFeedback(() -> Text.translatable("commands.trainmurdermystery.setzone.success", type.getName()), true);
        return 1;
    }
}