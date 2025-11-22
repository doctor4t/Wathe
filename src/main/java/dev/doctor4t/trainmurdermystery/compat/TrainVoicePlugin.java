package dev.doctor4t.trainmurdermystery.compat;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import dev.doctor4t.trainmurdermystery.TMM;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TrainVoicePlugin implements VoicechatPlugin {
    public static final UUID GROUP_ID = UUID.randomUUID();
    public static VoicechatServerApi serverAPI;
    public static Group group;

    public static boolean isVoiceChatMissing() {
        return serverAPI == null;
    }

    public static void addPlayer(@NotNull UUID player) {
        if (isVoiceChatMissing()) {
            return;
        }
        VoicechatConnection connection = serverAPI.getConnectionOf(player);
        if (connection == null) {
            return;
        }
        if (group == null) {
            group = serverAPI.groupBuilder().setHidden(true).setId(GROUP_ID).setName("Train Spectators").setPersistent(true).setType(Group.Type.OPEN).build();
            if (group == null) {
                return;
            }
        }
        connection.setGroup(group);
    }

    public static void resetPlayer(@NotNull UUID player) {
        if (isVoiceChatMissing()) {
            return;
        }
        VoicechatConnection connection = serverAPI.getConnectionOf(player);
        if (connection == null) {
            return;
        }
        connection.setGroup(null);
    }

    @Override
    public void registerEvents(@NotNull EventRegistration registration) {
        registration.registerEvent(VoicechatServerStartedEvent.class, event -> {
            serverAPI = event.getVoicechat();
        });
    }

    @Override
    public String getPluginId() {
        return TMM.MOD_ID;
    }
}