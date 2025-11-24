package dev.doctor4t.trainmurdermystery.api.role.impl;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.CustomRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.api.team.TMMTeams;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.util.Identifier;

public class LooseEndRole implements CustomRole {
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
        return TMM.id("loose_end");
    }

    @Override
    public int color() {
        return 0x9F0000;
    }

    @Override
    public CustomTeam team() {
        return TMMTeams.INNOCENT;
    }

    @Override
    public RoleAnnouncementTexts.RoleAnnouncementText announcementText() {
        return RoleAnnouncementTexts.LOOSE_END;
    }
    @Override
    public boolean hasMood() {
        return true;
    }
}
