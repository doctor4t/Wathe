package dev.doctor4t.wathe.client.animation;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

/**
 * @author EightSidedSquare
 * @author Geckolib authors
 */
public class Interpolations {

    public static final Transformation.Interpolation LINEAR = Transformation.Interpolations.LINEAR;
    public static final Transformation.Interpolation SPLINE = Transformation.Interpolations.CUBIC;
    public static final InterpolationCreator STEP = steps -> easing(Interpolations.step((double) steps));
    public static final Transformation.Interpolation EASE_IN_QUADRATIC = easing(easeIn(Interpolations::quadratic));
    public static final Transformation.Interpolation EASE_OUT_QUADRATIC = easing(easeOut(Interpolations::quadratic));
    public static final Transformation.Interpolation EASE_IN_OUT_QUADRATIC = easing(easeInOut(Interpolations::quadratic));
    public static final Transformation.Interpolation EASE_IN_CUBIC = easing(easeIn(Interpolations::cubic));
    public static final Transformation.Interpolation EASE_OUT_CUBIC = easing(easeOut(Interpolations::cubic));
    public static final Transformation.Interpolation EASE_IN_OUT_CUBIC = easing(easeInOut(Interpolations::cubic));
    public static final Transformation.Interpolation EASE_IN_QUARTIC = easing(easeIn(pow(4)));
    public static final Transformation.Interpolation EASE_OUT_QUARTIC = easing(easeOut(pow(4)));
    public static final Transformation.Interpolation EASE_IN_OUT_QUARTIC = easing(easeInOut(pow(4)));
    public static final Transformation.Interpolation EASE_IN_QUINTIC = easing(easeIn(pow(5)));
    public static final Transformation.Interpolation EASE_OUT_QUINTIC = easing(easeOut(pow(5)));
    public static final Transformation.Interpolation EASE_IN_OUT_QUINTIC = easing(easeInOut(pow(5)));
    public static final Transformation.Interpolation EASE_IN_EXPO = easing(easeIn(Interpolations::exp));
    public static final Transformation.Interpolation EASE_OUT_EXPO = easing(easeOut(Interpolations::exp));
    public static final Transformation.Interpolation EASE_IN_OUT_EXPO = easing(easeInOut(Interpolations::exp));
    public static final Transformation.Interpolation EASE_IN_CIRCLE = easing(easeIn(Interpolations::circle));
    public static final Transformation.Interpolation EASE_OUT_CIRCLE = easing(easeOut(Interpolations::circle));
    public static final Transformation.Interpolation EASE_IN_OUT_CIRCLE = easing(easeInOut(Interpolations::circle));
    public static final InterpolationCreator EASE_IN_BACK = overshoot -> easing(easeIn(Interpolations.back((double) overshoot)));
    public static final InterpolationCreator EASE_OUT_BACK = overshoot -> easing(easeOut(Interpolations.back((double) overshoot)));
    public static final InterpolationCreator EASE_IN_OUT_BACK = overshoot -> easing(easeInOut(Interpolations.back((double) overshoot)));
    public static final InterpolationCreator EASE_IN_BOUNCE = bounciness -> easing(easeIn(Interpolations.bounce((double) bounciness)));
    public static final InterpolationCreator EASE_OUT_BOUNCE = bounciness -> easing(easeOut(Interpolations.bounce((double) bounciness)));
    public static final InterpolationCreator EASE_IN_OUT_BOUNCE = bounciness -> easing(easeInOut(Interpolations.bounce((double) bounciness)));
    public static final Transformation.Interpolation EASE_IN_SINE = easing(easeIn(Interpolations::sine));
    public static final Transformation.Interpolation EASE_OUT_SINE = easing(easeOut(Interpolations::sine));
    public static final Transformation.Interpolation EASE_IN_OUT_SINE = easing(easeInOut(Interpolations::sine));
    public static final InterpolationCreator EASE_IN_ELASTIC = bounciness -> easing(easeIn(Interpolations.elastic((double) bounciness)));
    public static final InterpolationCreator EASE_OUT_ELASTIC = bounciness -> easing(easeOut(Interpolations.elastic((double) bounciness)));
    public static final InterpolationCreator EASE_IN_OUT_ELASTIC = bounciness -> easing(easeInOut(Interpolations.elastic((double) bounciness)));

    private static Transformation.Interpolation easing(Double2DoubleFunction easing) {
        return (output, delta, keyframes, currentFrame, targetFrame, strength) -> {
            Vector3f vector3f = keyframes[currentFrame].target();
            Vector3f vector3f2 = keyframes[targetFrame].target();

            double eased = delta <= 0 ? 0 : delta >= 1 ? 1 : easing.apply((double) delta);
            output.set(MathHelper.lerp(eased, vector3f.x(), vector3f2.x()), MathHelper.lerp(eased, vector3f.y(), vector3f2.y()), MathHelper.lerp(eased, vector3f.z(), vector3f2.z()));
            return output;
        };
    }

    /**
     * Returns an easing function running forward in time
     */
    static Double2DoubleFunction easeIn(Double2DoubleFunction function) {
        return function;
    }

    /**
     * Returns an easing function running backwards in time
     */
    static Double2DoubleFunction easeOut(Double2DoubleFunction function) {
        return time -> 1 - function.apply(1 - time);
    }

    /**
     * Returns an easing function that runs equally both forwards and backwards in time based on the halfway point, generating a symmetrical curve
     */
    static Double2DoubleFunction easeInOut(Double2DoubleFunction function) {
        return time -> {
            if (time < 0.5d)
                return function.apply(time * 2d) / 2d;

            return 1 - function.apply((1 - time) * 2d) / 2d;
        };
    }

    /**
     * A quadratic function, equivalent to the square (<i>n</i>^2) of elapsed time
     * <p>
     * {@code f(n) = n^2}
     * <p>
     * <a href="http://easings.net/#easeInQuad">Easings.net#easeInQuad</a>
     */
    static double quadratic(double n) {
        return n * n;
    }

    /**
     * A cubic function, equivalent to cube (<i>n</i>^3) of elapsed time
     * <p>
     * {@code f(n) = n^3}
     * <p>
     * <a href="http://easings.net/#easeInCubic">Easings.net#easeInCubic</a>
     */
    static double cubic(double n) {
        return n * n * n;
    }

    /**
     * A sinusoidal function, equivalent to a sine curve output
     * <p>
     * {@code f(n) = 1 - cos(n * π / 2)}
     * <p>
     * <a href="http://easings.net/#easeInSine">Easings.net#easeInSine</a>
     */
    static double sine(double n) {
        return 1 - Math.cos(n * Math.PI / 2f);
    }

    /**
     * A circular function, equivalent to a normally symmetrical curve
     * <p>
     * {@code f(n) = 1 - sqrt(1 - n^2)}
     * <p>
     * <a href="http://easings.net/#easeInCirc">Easings.net#easeInCirc</a>
     */
    static double circle(double n) {
        return 1 - Math.sqrt(1 - n * n);
    }

    /**
     * An exponential function, equivalent to an exponential curve
     * <p>
     * {@code f(n) = 2^(10 * (n - 1))}
     * <p>
     * <a href="http://easings.net/#easeInExpo">Easings.net#easeInExpo</a>
     */
    static double exp(double n) {
        return Math.pow(2, 10 * (n - 1));
    }

    // ---> Easing Curve Functions <--- //

    /**
     * An elastic function, equivalent to an oscillating curve
     * <p>
     * <i>n</i> defines the elasticity of the output
     * <p>
     * {@code f(t) = 1 - (cos(t * π) / 2))^3 * cos(t * n * π)}
     * <p>
     * <a href="http://easings.net/#easeInElastic">Easings.net#easeInElastic</a>
     */
    static Double2DoubleFunction elastic(Double n) {
        double n2 = n == null ? 1 : n;

        return t -> 1 - Math.pow(Math.cos(t * Math.PI / 2f), 3) * Math.cos(t * n2 * Math.PI);
    }

    /**
     * A bouncing function, equivalent to a bouncing ball curve
     * <p>
     * <i>n</i> defines the bounciness of the output
     * <p>
     * Thanks to <b>Waterded#6455</b> for making the bounce adjustable, and <b>GiantLuigi4#6616</b> for additional cleanup
     * <p>
     * <a href="http://easings.net/#easeInBounce">Easings.net#easeInBounce</a>
     */
    static Double2DoubleFunction bounce(Double n) {
        final double n2 = n == null ? 0.5d : n;

        Double2DoubleFunction one = x -> 121f / 16f * x * x;
        Double2DoubleFunction two = x -> 121f / 4f * n2 * Math.pow(x - 6f / 11f, 2) + 1 - n2;
        Double2DoubleFunction three = x -> 121 * n2 * n2 * Math.pow(x - 9f / 11f, 2) + 1 - n2 * n2;
        Double2DoubleFunction four = x -> 484 * n2 * n2 * n2 * Math.pow(x - 10.5f / 11f, 2) + 1 - n2 * n2 * n2;

        return t -> Math.min(Math.min(one.apply(t), two.apply(t)), Math.min(three.apply(t), four.apply(t)));
    }

    /**
     * A negative elastic function, equivalent to inverting briefly before increasing
     * <p>
     * <code>f(t) = t^2 * ((n * 1.70158 + 1) * t - n * 1.70158)</code>
     * <p>
     * <a href="https://easings.net/#easeInBack">Easings.net#easeInBack</a>
     */
    static Double2DoubleFunction back(Double n) {
        final double n2 = n == null ? 1.70158d : n * 1.70158d;

        return t -> t * t * ((n2 + 1) * t - n2);
    }

    /**
     * An exponential function, equivalent to an exponential curve to the {@code n} root
     * <p>
     * <code>f(t) = t^n</code>
     *
     * @param n The exponent
     */
    static Double2DoubleFunction pow(double n) {
        return t -> Math.pow(t, n);
    }

    // The MIT license notice below applies to the function step

    /**
     * The MIT License (MIT)
     * <br><br>
     * Copyright (c) 2015 Boris Chumichev
     * <br><br>
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     * <br><br>
     * The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software.
     * <br><br>
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     * SOFTWARE.
     * <br><br>
     * Returns a stepped value based on the nearest step to the input value.<br>
     * The size (grade) of the steps depends on the provided value of {@code n}
     **/
    static Double2DoubleFunction step(Double n) {
        double n2 = n == null ? 2 : n;

        if (n2 < 2)
            throw new IllegalArgumentException("Steps must be >= 2, got: " + n2);

        final int steps = (int) n2;

        return t -> {
            double result = 0;

            if (t < 0)
                return result;

            double stepLength = (1 / (double) steps);

            if (t > (result = (steps - 1) * stepLength))
                return result;

            int testIndex;
            int leftBorderIndex = 0;
            int rightBorderIndex = steps - 1;

            while (rightBorderIndex - leftBorderIndex != 1) {
                testIndex = leftBorderIndex + (rightBorderIndex - leftBorderIndex) / 2;

                if (t >= testIndex * stepLength) {
                    leftBorderIndex = testIndex;
                } else {
                    rightBorderIndex = testIndex;
                }
            }

            return leftBorderIndex * stepLength;
        };
    }

    public interface InterpolationCreator {
        Transformation.Interpolation configure(float value);
    }

}
