package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class GiveRoomKeyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:giveRoomKey")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("roomName", StringArgumentType.string())
                                        .executes(context -> giveRoomKey(context.getSource(), StringArgumentType.getString(context, "roomName")))
                        )
        );
    }

    private static int giveRoomKey(ServerCommandSource source, String roomName) {
        ItemStack itemStack = new ItemStack(TMMItems.KEY);
        itemStack.apply(DataComponentTypes.LORE, LoreComponent.DEFAULT, component -> new LoreComponent(Text.literal(roomName).getWithStyle(Style.EMPTY.withItalic(false).withColor(0xFF8C00))));
        if (source.getPlayer() != null) source.getPlayer().giveItemStack(itemStack);
        return 1;
    }
}
