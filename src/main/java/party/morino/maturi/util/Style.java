package party.morino.maturi.util;

import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Style {

    private Style() {
    }

    public static Vector circle(final int step, final int maxStep, final double radius) {
        double angle;
        double x;
        double y;

        angle = 2 * Math.PI * step / maxStep;

        x = Math.cos(angle) * radius;
        y = Math.sin(angle) * radius;

        return new Vector(x, y, 0);
    }

    public static Vector rotate(Vector vector, final double pitch, final double yaw) {
        vector = VectorUtils.rotateAroundAxisX(vector, VectorUtils.xAxisCos(pitch), VectorUtils.xAxisSin(pitch));
        vector = VectorUtils.rotateAroundAxisY(vector, VectorUtils.yAxisCos(yaw), VectorUtils.yAxisSin(yaw));

        return vector;
    }
}
