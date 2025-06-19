package party.morino.maturi.game.syateki.data;

import party.morino.maturi.game.GameData;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.syateki.Syateki;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Record representing the data required to start a {@link Syateki}.
 *
 * @param uuid                 the unique identifier
 * @param gamers               the list of gamers
 * @param duration             the duration
 *
 * @param difficulty           the difficulty
 */
public record SyatekiData(
        UUID uuid,
        List<Gamer> gamers,
        Duration duration,
        SyatekiArea syatekiArea,
        Syateki.Difficulty difficulty
) implements GameData {
}
