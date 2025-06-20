package party.morino.maturi;

import com.google.inject.Singleton;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
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