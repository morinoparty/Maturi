package party.morino.maturi;

import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
@Singleton
public final class MaturiProvider {

    private static @Nullable Maturi instance;

    private MaturiProvider() {
    }

    static void register(final Maturi instance) {
        MaturiProvider.instance = instance;
    }

    public static Maturi instance() {
        if (instance == null) {
            throw new IllegalStateException("Maturi not initialized!");
        }

        return instance;
    }
}