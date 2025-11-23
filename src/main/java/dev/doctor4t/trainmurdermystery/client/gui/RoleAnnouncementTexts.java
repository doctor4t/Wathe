package dev.doctor4t.trainmurdermystery.client.gui;

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

    public static final RoleAnnouncementText BLANK = registerRoleAnnouncementText(new RoleAnnouncementText("", 0xFFFFFF));
    public static final RoleAnnouncementText CIVILIAN = registerRoleAnnouncementText(new RoleAnnouncementText("civilian", 0x36E51B));
    public static final RoleAnnouncementText VIGILANTE = registerRoleAnnouncementText(new RoleAnnouncementText("vigilante", 0x1B8AE5));
    public static final RoleAnnouncementText KILLER = registerRoleAnnouncementText(new RoleAnnouncementText("killer", 0xC13838));
    public static final RoleAnnouncementText LOOSE_END = registerRoleAnnouncementText(new RoleAnnouncementText("loose_end", 0x9F0000));

    public static class RoleAnnouncementText {
        private final String name;
        public final int colour;
        public final Text roleText;
        public final Text titleText;
        public final Text welcomeText;
        public final Function<Integer, Text> premiseText;
        public final Function<Integer, Text> goalText;
        public final Text winText;

        public RoleAnnouncementText(String name, int colour) {
            this.name = name;
            this.colour = colour;
            this.roleText = Text.translatable("announcement.role." + this.name.toLowerCase()).withColor(this.colour);
            this.titleText = Text.translatable("announcement.title." + this.name.toLowerCase()).withColor(this.colour);
            this.welcomeText = Text.translatable("announcement.welcome", this.roleText).withColor(0xF0F0F0);
            this.premiseText = (count) -> Text.translatable(count == 1 ? "announcement.premise" : "announcement.premises", count);
            this.goalText = (count) -> Text.translatable((count == 1 ? "announcement.goal." : "announcement.goals.") + this.name.toLowerCase(), count).withColor(this.colour);
            this.winText = Text.translatable("announcement.win." + this.name.toLowerCase()).withColor(this.colour);
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
