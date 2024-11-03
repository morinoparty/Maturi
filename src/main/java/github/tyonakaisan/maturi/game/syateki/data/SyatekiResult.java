package github.tyonakaisan.maturi.game.syateki.data;

import github.tyonakaisan.maturi.game.syateki.Syateki;

import java.time.Instant;

/**
 * {@link Syateki} Results
 * @param syatekiData the {@link SyatekiData}
 * @param played the played
 * @param point the point
 */
public record SyatekiResult(
        SyatekiData syatekiData,
        Instant played,
        int point
) {
}
