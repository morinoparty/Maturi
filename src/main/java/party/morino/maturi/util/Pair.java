package party.morino.maturi.util;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@NullMarked
@ConfigSerializable
public record Pair<A, B>(
        A first,
        B second
) {
    public static <A, B> Pair<A, B> of(final A first, final B second) {
        return new Pair<>(first, second);
    }
}