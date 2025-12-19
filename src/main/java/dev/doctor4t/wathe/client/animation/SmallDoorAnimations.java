package dev.doctor4t.wathe.client.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class SmallDoorAnimations {
    public static final Animation OPEN = Animation.Builder.create(1f)
            .addBoneAnimation("Door",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Interpolations.EASE_OUT_EXPO),
                            new Keyframe(0.7f, AnimationHelper.createTranslationalVector(14f, 0f, 0f),
                                    Interpolations.EASE_OUT_EXPO))).build();
    public static final Animation CLOSE = Animation.Builder.create(1f)
            .addBoneAnimation("Door",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.1f, AnimationHelper.createTranslationalVector(14f, 0f, 0f),
                                    Interpolations.EASE_OUT_EXPO),
                            new Keyframe(0.7f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Interpolations.EASE_OUT_EXPO))).build();
}
