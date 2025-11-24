package dev.doctor4t.trainmurdermystery.api.role.impl;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.CustomRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.api.team.TMMTeams;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.util.Identifier;

public class VigilanteRole implements CustomRole {
    @Override
    public boolean forcesDroppedGun() {
        return true;
    }

    @Override
    public boolean canPickupGun() {
        return true;
    }

    @Override
    public Identifier id() {
        return TMM.id("vigilante");
    }

    @Override
    public int color() {
        return 0x1B8AE5;
    }

    @Override
    public CustomTeam team() {
        return TMMTeams.INNOCENT;
    }

    @Override
    public RoleAnnouncementTexts.RoleAnnouncementText announcementText() {
        return RoleAnnouncementTexts.VIGILANTE;
    }
    @Override
    public boolean hasMood() {
        return true;
    }
}
