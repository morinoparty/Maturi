package party.morino.maturi.util;

import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class VectorUtils {

    private VectorUtils() {
    }

    public static double xAxisCos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public static double xAxisSin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public static double yAxisCos(double angle) {
        return Math.cos(-Math.toRadians(angle));
    }

    public static double yAxisSin(double angle) {
        return Math.sin(-Math.toRadians(angle));
    }

    public static double zAxisCos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public static double zAxisSin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public static Vector rotateAroundAxisX(Vector v, double cos, double sin) {
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static Vector rotateAroundAxisY(Vector v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector rotateAroundAxisZ(Vector v, double cos, double sin) {
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
}
