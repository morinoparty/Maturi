package github.tyonakaisan.maturi.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@DefaultQualifier(NonNull.class)
@ConfigSerializable
public record Pair<A, B>(
        A first,
        B second
) {
    public static <A, B> Pair<A, B> of(final A first, final B second) {
        return new Pair<>(first, second);
    }
}