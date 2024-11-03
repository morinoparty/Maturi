package github.tyonakaisan.maturi.game;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class ScoreHolder {

    private int score = 0;

    public int score() {
        return this.score;
    }

    public void increment(final int point) {
        this.score = this.score + point;
    }

    public void reset() {
        this.score = 0;
    }
}
