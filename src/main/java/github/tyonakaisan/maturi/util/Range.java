package github.tyonakaisan.maturi.util;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record Range<N extends Comparable<N>>(N min, N max) {

    public boolean isWithinRange(final N score) {
        return score.compareTo(this.min) >= 0 && score.compareTo(this.max) <= 0;
    }
}
