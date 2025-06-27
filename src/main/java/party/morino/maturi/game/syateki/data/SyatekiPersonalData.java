package party.morino.maturi.game.syateki.data;

import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.syateki.Syateki;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Represents personal results for a {@link Gamer} in the {@link Syateki} game.
 * Stores the number of times played and the best results per difficulty level.
 */
@NullMarked
public final class SyatekiPersonalData {

    private final Gamer gamer;
    private final Map<Syateki.Difficulty, Integer> playCounts = new EnumMap<>(Syateki.Difficulty.class);
    private final Map<Syateki.Difficulty, SyatekiResult> bests = new EnumMap<>(Syateki.Difficulty.class);

    /**
     * Constructs a new {@link SyatekiPersonalData} instance for the given gamer.
     *
     * @param gamer the {@link Gamer} associated with this personal data
     */
    public SyatekiPersonalData(final Gamer gamer) {
        this.gamer = gamer;
    }

    /**
     * Returns the gamer associated with this personal data.
     *
     * @return the {@link Gamer}
     */
    public Gamer gamer() {
        return this.gamer;
    }

    /**
     * Returns the play times per difficulty.
     *
     * @return the play times map
     */
    @UnmodifiableView
    public Map<Syateki.Difficulty, Integer> playCounts() {
        return Collections.unmodifiableMap(this.playCounts);
    }

    /**
     * Returns the best results per difficulty.
     *
     * @return the best results map
     */
    @UnmodifiableView
    public Map<Syateki.Difficulty, SyatekiResult> bests() {
        return Collections.unmodifiableMap(this.bests);
    }

    /**
     * Get the best result for the specified difficulty level.
     * @param difficulty the difficulty
     * @return the syatekiResult or null
     */
    @UnmodifiableView
    public @Nullable SyatekiResult bests(final Syateki.Difficulty difficulty) {
        return this.bests.get(difficulty);
    }

    /**
     * Updates the personal data with a new result.
     * This includes updating the best result and play times.
     *
     * @param result the new {@link SyatekiResult}
     * @return the updated {@link SyatekiPersonalData} instance
     */
    public SyatekiPersonalData update(final SyatekiResult result) {
        this.updateBestResult(result);
        this.updatePlayTimes(result);
        return this;
    }

    /**
     * Updates the best result if the new result is better.
     *
     * @param result the new {@link SyatekiResult}
     * @return the updated {@link SyatekiPersonalData} instance
     */
    private SyatekiPersonalData updateBestResult(final SyatekiResult result) {
        final var difficulty = result.syatekiData().difficulty();
        this.bests.merge(difficulty, result, (oldResult, newResult) ->
                newResult.point() > oldResult.point() ? newResult : oldResult);
        return this;
    }

    /**
     * Increments the play times for the given difficulty.
     *
     * @param result the new {@link SyatekiResult}
     * @return the updated {@link SyatekiPersonalData} instance
     */
    private SyatekiPersonalData updatePlayTimes(final SyatekiResult result) {
        final var difficulty = result.syatekiData().difficulty();
        this.playCounts.merge(difficulty, 1, Integer::sum);
        return this;
    }
}
