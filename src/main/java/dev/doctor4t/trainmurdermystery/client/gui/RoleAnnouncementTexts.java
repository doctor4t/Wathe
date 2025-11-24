package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.team.TMMTeams;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

public class RoleAnnouncementTexts {
    public static final ArrayList<RoleAnnouncementTexts.RoleAnnouncementText> ROLE_ANNOUNCEMENT_TEXTS = new ArrayList<>();

    public static RoleAnnouncementTexts.RoleAnnouncementText registerRoleAnnouncementText(RoleAnnouncementTexts.RoleAnnouncementText role) {
        ROLE_ANNOUNCEMENT_TEXTS.add(role);
        return role;
    }
    public static final Text INNOCENT_TEAM = Text.translatable("team.innocents").withColor(TMMTeams.INNOCENT.endScreenColor());
    public static final Text KILLER_TEAM = Text.translatable("team.killers").withColor(TMMTeams.KILLER.endScreenColor());
    public static final Text NEUTRAL_TEAM = Text.translatable("team.neutrals").withColor(TMMTeams.NEUTRAL_KILLER.endScreenColor());
    public static final Text EXTERNAL_TEAM = Text.translatable("team.externals").withColor(0x3F3DA0);

    public static final RoleAnnouncementText BLANK = registerRoleAnnouncementText(new RoleAnnouncementText("", "", 0xFFFFFF, 0xFFFFFF, ""));
    public static final RoleAnnouncementText CIVILIAN = registerRoleAnnouncementText(new RoleAnnouncementText("civilian", "civilian", 0x36E51B, TMMTeams.INNOCENT.endScreenColor(), TMM.id("civilian").toString()));
    public static final RoleAnnouncementText VIGILANTE = registerRoleAnnouncementText(new RoleAnnouncementText("vigilante", "vigilante", 0x1B8AE5, 0x1B8AE5, TMM.id("vigilante").toString()));
    public static final RoleAnnouncementText KILLER = registerRoleAnnouncementText(new RoleAnnouncementText("killer", "killer", 0xC13838, TMMTeams.KILLER.endScreenColor(), TMM.id("killer").toString()));
    public static final RoleAnnouncementText LOOSE_END = registerRoleAnnouncementText(new RoleAnnouncementText("loose_end", "loose_end", 0x9F0000, TMMTeams.NEUTRAL_BENIGN.endScreenColor(), TMM.id("loose_end").toString()));

    public static class RoleAnnouncementText {
        private final String name;
        public final int colour;
        public final Text roleText;
        public final Text titleText;
        public final Text welcomeText;
        public final Function<Integer, Text> premiseText;
        public final Function<Integer, Text> goalText;
        public final Text winText;

        public final String roleId;
        public RoleAnnouncementText(String name, String team, int colour, int teamColor, String roleId) {
            this.name = name;
            this.roleId = roleId;
            this.colour = colour;
            this.roleText = Text.translatable("announcement.role." + this.name.toLowerCase()).withColor(this.colour);
            this.titleText = Text.translatable("announcement.title." + team.toLowerCase()).withColor(teamColor);
            this.welcomeText = Text.translatable("announcement.welcome", this.roleText).withColor(0xF0F0F0);
            this.premiseText = (count) -> Text.translatable(count == 1 ? "announcement.premise" : "announcement.premises", count);
            this.goalText = (count) -> Text.translatable((count == 1 ? "announcement.goal." : "announcement.goals.") + this.name.toLowerCase(), count).withColor(this.colour);
            this.winText = Text.translatable("announcement.win." + team.toLowerCase()).withColor(teamColor);
        }

        public Text getLoseText() {
            return this == KILLER ? CIVILIAN.winText : KILLER.winText;
        }

        public @Nullable Text getEndText(GameFunctions.@NotNull WinStatus status, Text winner) {
            return switch (status) {
                case NONE -> null;
                case PASSENGERS, TIME -> this == KILLER ? this.getLoseText() : this.winText;
                case KILLERS -> this == KILLER ? this.winText : this.getLoseText();
                case LOOSE_END ->
                        Text.translatable("announcement.win." + LOOSE_END.name.toLowerCase(), winner).withColor(LOOSE_END.colour);
            };
        }
    }
}
