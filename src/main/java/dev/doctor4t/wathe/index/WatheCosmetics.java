package dev.doctor4t.wathe.index;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.util.WeaponSkinsSupporterData;
import dev.upcraft.datasync.api.DataSyncAPI;
import dev.upcraft.datasync.api.SyncToken;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public interface WatheCosmetics {
    Identifier WEAPON_SKINS_DATA_ID = Wathe.id("weapon_skins");
    SyncToken<WeaponSkinsSupporterData> WEAPON_SKINS_DATA = DataSyncAPI.register(WeaponSkinsSupporterData.class, WEAPON_SKINS_DATA_ID, WeaponSkinsSupporterData.CODEC);

    static String getSkin(ItemStack itemStack) {
        UUID owner = UUID.fromString(itemStack.getOrDefault(WatheDataComponentTypes.OWNER, "98eaa37f-7712-4809-b709-504d3be0b6ef")); // random uuid
        String itemName = itemStack.getItem().getName().getString().toLowerCase(Locale.ROOT);
        Optional<WeaponSkinsSupporterData> optional = WEAPON_SKINS_DATA.get(owner);
        if (optional.isPresent()) {
            String serialized = optional.get().serialized();
            String[] namesAndSkins = serialized.split(";");
            for (String nameAndSkin : namesAndSkins) {
                if (nameAndSkin.matches(itemName + ":.+")) {
                    String[] split = nameAndSkin.split(":");
                    return split[1];
                }
            }
        }

        return "default";
    }

    static void setSkin(PlayerEntity player, ItemStack itemStack, String skinName) {
        // only upload data on the client, servers can't datasync
        if (player.getWorld().isClient()) {
            StringBuilder serializedBuilder = new StringBuilder();
            Optional<WeaponSkinsSupporterData> optional = WEAPON_SKINS_DATA.get(player.getUuid());
            String itemName = itemStack.getItem().getName().getString().toLowerCase(Locale.ROOT);

            String[] namesAndSkins = new String[]{};
            if (optional.isPresent()) {
                namesAndSkins = optional.get().serialized().split(";");
            }

            for (String nameAndSkin : namesAndSkins) {
                if (!nameAndSkin.matches(itemName + ":.+")) {
                    serializedBuilder.append(nameAndSkin).append(";");
                }
            }

            serializedBuilder.append(itemName).append(":").append(skinName);
            String string = serializedBuilder.toString();
            WeaponSkinsSupporterData newData = new WeaponSkinsSupporterData(string);
            WEAPON_SKINS_DATA.setData(newData); // upload to server
        }
    }
}
