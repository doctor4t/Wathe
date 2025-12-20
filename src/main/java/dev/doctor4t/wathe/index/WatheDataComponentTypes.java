package dev.doctor4t.wathe.index;

import com.mojang.serialization.Codec;
import dev.doctor4t.wathe.Wathe;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public interface WatheDataComponentTypes {
    ComponentType<String> OWNER = register("owner", stringBuilder -> stringBuilder.codec(Codec.STRING).packetCodec(PacketCodecs.STRING));
    ComponentType<String> POISONER = register("poisoner", stringBuilder -> stringBuilder.codec(Codec.STRING).packetCodec(PacketCodecs.STRING));
    ComponentType<Boolean> USED = register("used", stringBuilder -> stringBuilder.codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL));

    private static <T> ComponentType<T> register(String name, @NotNull UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Wathe.id(name), builderOperator.apply(ComponentType.builder()).build());
    }
}
