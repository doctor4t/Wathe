package dev.doctor4t.wathe.api;

import dev.doctor4t.trainmurdermystery.game.GameConstants;
import net.minecraft.util.Identifier;

public final class Role {
    private final Identifier identifier;
    private final int color;
    private final boolean isInnocent;
    private final boolean canUseKiller;
    private final MoodType moodType;
    private final int maxSprintTime;
    private final boolean canSeeTime;

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
    public Role(Identifier identifier, int color, boolean isInnocent, boolean canUseKiller, MoodType moodType, int maxSprintTime, boolean canSeeTime) {
        this.identifier = identifier;
        this.color = color;
        this.isInnocent = isInnocent;
        this.canUseKiller = canUseKiller;
        this.moodType = moodType;
        this.maxSprintTime = maxSprintTime;
        this.canSeeTime = canSeeTime;
    }

    public Identifier identifier() {
        return identifier;
    }

    public int color() {
        return color;
    }

    public boolean isInnocent() {
        return isInnocent;
    }

    public boolean canUseKiller() {
        return canUseKiller;
    }

    public MoodType getMoodType() {
        return moodType;
    }

    public int getMaxSprintTime() {
        return maxSprintTime;
    }

    public boolean canSeeTime() {
        return canSeeTime;
    }

    public static class Builder {
        protected int color = -1;
        protected boolean isInnocent = false;
        protected boolean canUseKiller = false;
        protected MoodType moodType = MoodType.NONE;
        protected int maxSprintTime = -1;
        protected boolean canSeeTime = false;

        public Builder() {
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        /**
         * Populates the builder with the innocents' default values
         * (innocent, doesn't see time, max 10-second sprint, real mood)
         * @return The {@linkplain Builder}, for chaining
         */
        public Builder innocent() {
            return this.isInnocent(true)
                    .canUseKiller(false)
                    .canSeeTime(false)
                    .maxSprintTime(GameConstants.getInTicks(0, 10))
                    .moodType(MoodType.REAL);
        }

        public Builder isInnocent(boolean innocent) {
            this.isInnocent = innocent;
            return this;
        }

        /**
         * Populates the builder with the killers' default values
         * (killer, sees time, infinite sprint, fake mood)
         * @return The {@linkplain Builder}, for chaining
         */
        public Builder killer() {
            return this.canUseKiller(true)
                    .isInnocent(false)
                    .canSeeTime(true)
                    .infiniteSprint()
                    .moodType(MoodType.FAKE);
        }

        public Builder canUseKiller(boolean canUseKiller) {
            this.canUseKiller = canUseKiller;
            return this;
        }

        public Builder moodType(MoodType moodType) {
            this.moodType = moodType;
            return this;
        }

        public Builder infiniteSprint() {
            return this.maxSprintTime(-1);
        }

        public Builder maxSprintTime(int maxSprintTime) {
            this.maxSprintTime = maxSprintTime;
            return this;
        }

        public Builder canSeeTime(boolean canSeeTime) {
            this.canSeeTime = canSeeTime;
            return this;
        }

        public Role build(Identifier id) {
            return new Role(id, this.color, this.isInnocent, this.canUseKiller, this.moodType, this.maxSprintTime, this.canSeeTime);
        }

        public Role buildAndRegister(Identifier id) {
            return TMMRoles.registerRole(this.build(id));
        }
    }
}