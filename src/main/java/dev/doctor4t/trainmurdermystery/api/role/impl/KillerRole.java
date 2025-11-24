package dev.doctor4t.trainmurdermystery.api.role.impl;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.CustomRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.api.team.TMMTeams;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.util.Identifier;

public class KillerRole implements CustomRole {
    @Override
    public boolean forcesDroppedGun() {
        return false;
    }

    @Override
    public boolean canPickupGun() {
        return false;
    }

    @Override
    public Identifier id() {
        return TMM.id("killer");
    }

    @Override
    public int color() {
        return 0xC13838;
    }

    @Override
    public CustomTeam team() {
        return TMMTeams.KILLER;
    }

    @Override
    public RoleAnnouncementTexts.RoleAnnouncementText announcementText() {
        return RoleAnnouncementTexts.KILLER;
    }
    @Override
    public boolean hasMood() {
        return false;
    }
}
