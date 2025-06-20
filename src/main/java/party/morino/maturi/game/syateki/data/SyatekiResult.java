package party.morino.maturi.game.syateki.data;

import org.jspecify.annotations.NullMarked;
import party.morino.maturi.game.syateki.Syateki;

import java.time.Instant;

/**
 * {@link Syateki} Results
 * @param syatekiData the {@link SyatekiData}
 * @param played the played
 * @param point the point
 */
@NullMarked
public record SyatekiResult(
        SyatekiData syatekiData,
        Instant played,
        int point
) {
}
