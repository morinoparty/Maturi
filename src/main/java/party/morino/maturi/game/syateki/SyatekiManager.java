package party.morino.maturi.game.syateki;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.util.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NullMarked
@Singleton
public final class SyatekiManager {

    private final ComponentLogger logger;

    private final Set<Pair<Syateki.Difficulty, Syateki>> actives = new HashSet<>();

    @Inject
    public SyatekiManager(
            final ComponentLogger logger
    ) {
        this.logger = logger;

        this.reload();
    }

    public void reload() {
        // empty
    }

    public void addActive(final Syateki syateki) {
        this.actives.add(Pair.of(syateki.syatekiData().difficulty(), syateki));
    }

    public void removeActive(final Syateki syateki) {
        this.actives.removeIf(pair -> pair.first().equals(syateki.syatekiData().difficulty()) && pair.second().syatekiData().uuid().equals(syateki.syatekiData().uuid()));
    }

    public boolean hasActiveGame(final Player player) {
        return this.hasActiveGame(new Gamer(player.getUniqueId(), player.displayName()));
    }

    public boolean hasActiveGame(final Gamer gamer) {
        if (this.actives.isEmpty()) {
            return false;
        }

        return this.actives.stream()
                .anyMatch(pair -> pair.second().containsPlayer(gamer));
    }

    public Optional<Syateki> matchActiveGame(final Player player) {
        if (this.actives.isEmpty()) {
            return Optional.empty();
        }

        return this.actives.stream()
                .filter(pair -> pair.second().containsPlayer(player))
                .map(Pair::second)
                .findFirst();
    }

    // w
    @UnmodifiableView
    public Set<Pair<Syateki.Difficulty, Syateki>> actives() {
        return Collections.unmodifiableSet(this.actives);
    }

    public int actives(final Syateki.Difficulty difficulty) {
        return (int) this.actives.stream()
                .filter(pair -> pair.first().equals(difficulty))
                .count();
    }
}
