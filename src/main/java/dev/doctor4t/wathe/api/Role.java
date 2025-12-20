package dev.doctor4t.wathe.api;

import net.minecraft.util.Identifier;

public record Role(Identifier identifier, int color, boolean isInnocent, boolean canUseKiller, MoodType moodType,
                   int maxSprintTime, boolean canSeeTime) {
    public enum MoodType {
        NONE, REAL, FAKE
    }

    /**
     * @param identifier    the mod id and name of the role
     * @param color         the role announcement color
     * @param isInnocent    whether the gun drops when a person with this role is shot and is considered a civilian to the win conditions
     * @param canUseKiller  can see and use the killer features
     * @param moodType      the mood type a role has
     * @param maxSprintTime the maximum sprint time in ticks
     * @param canSeeTime    if the role can see the game timer
     */
    public Role {
    }
}