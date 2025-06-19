package party.morino.maturi.game.syateki.data;

import party.morino.maturi.game.Area;
import party.morino.maturi.game.syateki.Syateki;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * Represents the areas used in the {@link Syateki} game.
 *
 * @param targetSpawnArea the targetSpawnArea
 * @param restrictedArea  the restrictedArea
 * @param spawnPoint      the spawnPoint
 */
@DefaultQualifier(NonNull.class)
@ConfigSerializable
public record SyatekiArea(
        Area targetSpawnArea,
        Area restrictedArea,
        Location spawnPoint
) {
}
