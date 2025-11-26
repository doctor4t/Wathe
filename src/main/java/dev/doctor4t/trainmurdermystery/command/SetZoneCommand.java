package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class SetZoneCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:setzone")
                .requires(source -> source.hasPermissionLevel(2))

                .then(CommandManager.literal("playArea")
                        .executes(ctx -> setZone(ctx, ZoneType.PLAY_AREA)))
                .then(CommandManager.literal("readyArea")
                        .executes(ctx -> setZone(ctx, ZoneType.READY_AREA)))
                .then(CommandManager.literal("resetTemplateArea")
                        .executes(ctx -> setZone(ctx, ZoneType.RESET_TEMPLATE)))
                .then(CommandManager.literal("resetPasteArea")
                        .executes(ctx -> setZone(ctx, ZoneType.RESET_PASTE)))
        );
    }

    private enum ZoneType {
        PLAY_AREA, READY_AREA, RESET_TEMPLATE, RESET_PASTE
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
        source.sendFeedback(() -> Text.translatable("commands.trainmurdermystery.setzone.success", type.name()), true);
        return 1;
    }
}