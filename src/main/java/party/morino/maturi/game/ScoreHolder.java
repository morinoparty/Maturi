package party.morino.maturi.game;

import org.jspecify.annotations.NullMarked;

@NullMarked
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
