package party.morino.maturi.game.syateki.data;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import party.morino.maturi.game.Area;
import party.morino.maturi.game.syateki.Syateki;

/**
 * Represents the areas used in the {@link Syateki} game.
 *
 * @param targetSpawnArea the targetSpawnArea
 * @param restrictedArea  the restrictedArea
 * @param spawnPoint      the spawnPoint
 */
@NullMarked
@ConfigSerializable
public record SyatekiArea(
        Area targetSpawnArea,
        Area restrictedArea,
        Location spawnPoint
) {
}
