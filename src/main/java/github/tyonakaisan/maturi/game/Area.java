package github.tyonakaisan.maturi.game;

import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * Area.
 * @param firstCorner the first corner
 * @param secondCorner the second corner
 */
@DefaultQualifier(NonNull.class)
@ConfigSerializable
public record Area(
        Location firstCorner,
        Location secondCorner
) {
    /**
     * Check if the Location is within the of Area.
     * @param location the location
     * @return if the Location is within the of Area
     */
    public boolean in(final Location location) {
        return this.inX(location) && this.inY(location) && this.inZ(location);
    }

    /**
     * Check if the x-coordinate of Location is within the x-coordinate of Area.
     * @param location the location
     * @return if the x-coordinate of Location is within the x-coordinate of Area
     */
    public boolean inX(final Location location) {
        final var x = location.getX();
        return this.isWithinRange(x, this.firstCorner.getX(), this.secondCorner.getX());
    }

    /**
     * Check if the y-coordinate of Location is within the y-coordinate of Area.
     * @param location the location
     * @return if the y-coordinate of Location is within the y-coordinate of Area
     */
    public boolean inY(final Location location) {
        final var y = location.getY();
        return this.isWithinRange(y, this.firstCorner.getY(), this.secondCorner.getY());
    }

    /**
     * Check if the z-coordinate of Location is within the z-coordinate of Area.
     * @param location the location
     * @return if the z-coordinate of Location is within the z-coordinate of Area
     */
    public boolean inZ(final Location location) {
        final var z = location.getZ();
        return this.isWithinRange(z, this.firstCorner.getZ(), this.secondCorner.getZ());
    }

    private boolean isWithinRange(final double value, final double first, final double second) {
        final var min = Math.min(first, second);
        final var max = Math.max(first, second);
        return value >= min && value <= max;
    }
}
