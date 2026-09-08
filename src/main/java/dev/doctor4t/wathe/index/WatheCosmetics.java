package dev.doctor4t.wathe.index;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.util.WeaponSkinsSupporterData;
import dev.upcraft.datasync.api.DataSyncAPI;
import dev.upcraft.datasync.api.SyncToken;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface WatheCosmetics {
    Identifier WEAPON_SKINS_DATA_ID = Wathe.id("weapon_skins");
    SyncToken<WeaponSkinsSupporterData> WEAPON_SKINS_DATA = DataSyncAPI.register(WeaponSkinsSupporterData.class, WEAPON_SKINS_DATA_ID, WeaponSkinsSupporterData.CODEC);

    ItemSkinsManager KNIFE_SKINS_MANAGER = new ItemSkinsManager();
    ItemSkinsManager REVOLVER_SKINS_MANAGER = new ItemSkinsManager();

    static void initialize() {
        KNIFE_SKINS_MANAGER.registerItemSkin("default", 0xFF404040, "Kitchen Knife");
        KNIFE_SKINS_MANAGER.registerItemSkin("ceremonial", 0xFFD98C28, "Ceremonial Dagger");
        KNIFE_SKINS_MANAGER.registerItemSkin("pick", 0xFF8D4A51, "Ice Pick");

        REVOLVER_SKINS_MANAGER.registerItemSkin("default", 0xFF404040, "Revolver");
        REVOLVER_SKINS_MANAGER.registerItemSkin("broomhandle", 0xFF6E4B47, "Broomhandle");
        REVOLVER_SKINS_MANAGER.registerItemSkin("blundabust", 0xFF6E3B2A, "Blundabust");
        REVOLVER_SKINS_MANAGER.registerItemSkin("western", 0xFF8E8770, "Western");
        REVOLVER_SKINS_MANAGER.registerItemSkin("bayonet", 0xFF811A2B, "Bayonet");
    }

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
                    if (split[0].equalsIgnoreCase(Registries.ITEM.getId(itemStack.getItem()).getPath())) {
                        return split[1];
                    }
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


    class ItemSkinsManager {
        public final List<Skin> skinList;

        public ItemSkinsManager() {
            this.skinList = new ArrayList<>();
        }

        public final Skin registerItemSkin(String name, int color, @Nullable String tooltipName) {
            Skin revolverSkin = new Skin(name, color, tooltipName);
            this.skinList.add(revolverSkin);
            return revolverSkin;
        }

        public Skin fromString(String name) {
            for (Skin skin : this.skinList) if (skin.getName().equalsIgnoreCase(name)) return skin;
            return this.skinList.get(0);
        }

        public Skin getNext(Skin skin) {
            return this.skinList.get((this.skinList.indexOf(skin) + 1) % this.skinList.size());
        }

        public static class Skin {
            public final String name;
            public final int color;
            public final @Nullable String tooltipName;
            public final Random random;

            Skin(String name, int color, @Nullable String tooltipName) {
                this.name = name;
                this.color = color;
                this.tooltipName = tooltipName;
                this.random = new Random();
            }

            public String getName() {
                return name.toLowerCase(Locale.ROOT);
            }

            public int getColor() {
                return this.color;
            }

        }
    }

}
