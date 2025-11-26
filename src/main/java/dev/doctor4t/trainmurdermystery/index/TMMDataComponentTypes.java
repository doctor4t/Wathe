package dev.doctor4t.trainmurdermystery.index;

import com.mojang.serialization.Codec;
import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos; // Import BlockPos
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public interface TMMDataComponentTypes {
    ComponentType<String> POISONER = register("poisoner", stringBuilder -> stringBuilder.codec(Codec.STRING).packetCodec(PacketCodecs.STRING));
    ComponentType<Boolean> USED = register("used", stringBuilder -> stringBuilder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL));

    // Position data
    ComponentType<BlockPos> POS_A = register("pos_a", builder -> builder.codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC));
    ComponentType<BlockPos> POS_B = register("pos_b", builder -> builder.codec(BlockPos.CODEC).packetCodec(BlockPos.PACKET_CODEC));

    private static <T> ComponentType<T> register(String name, @NotNull UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, TMM.id(name), builderOperator.apply(ComponentType.builder()).build());
    }
}