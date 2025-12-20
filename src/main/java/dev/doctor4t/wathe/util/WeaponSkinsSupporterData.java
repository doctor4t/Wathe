package dev.doctor4t.wathe.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeaponSkinsSupporterData(String serialized) {
    public static final Codec<WeaponSkinsSupporterData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("serialized").forGetter(WeaponSkinsSupporterData::serialized)
    ).apply(instance, WeaponSkinsSupporterData::new));
}