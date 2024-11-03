package github.tyonakaisan.maturi.game.syateki;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import github.tyonakaisan.maturi.config.json.JsonManager;
import github.tyonakaisan.maturi.game.Gamer;
import github.tyonakaisan.maturi.game.syateki.data.SyatekiPersonalData;
import github.tyonakaisan.maturi.game.syateki.data.SyatekiResult;
import github.tyonakaisan.maturi.util.Pair;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@DefaultQualifier(NonNull.class)
@Singleton
public final class SyatekiManager {

    private final JsonManager jsonManager;
    private final ComponentLogger logger;

    private final Set<Pair<Syateki.Difficulty, Syateki>> actives = new HashSet<>();
    private final Set<SyatekiResult> results = new HashSet<>();
    private final Set<SyatekiPersonalData> personalDataSet = new HashSet<>();

    @Inject
    public SyatekiManager(
            final JsonManager jsonManager,
            final ComponentLogger logger
    ) {
        this.jsonManager = jsonManager;
        this.logger = logger;

        this.reload();
    }

    public void reload() {
        this.loadResults();
        this.loadPersonalData();

        this.logger.info("{} results, {} personals loaded!", this.results.size(), this.personalDataSet.size());
    }

    public void addActive(final Syateki syateki) {
        this.actives.add(Pair.of(syateki.syatekiData().difficulty(), syateki));
    }

    public void removeActive(final Syateki syateki) {
        this.actives.removeIf(pair -> pair.first().equals(syateki.syatekiData().difficulty()) && pair.second().syatekiData().uuid().equals(syateki.syatekiData().uuid()));
    }

    private void loadResults() {
        this.results.clear();
        this.results.addAll(this.jsonManager.loadResult());
    }

    public void saveResult(final SyatekiResult result) {
        this.results.add(result);
        this.jsonManager.saveResult(result);
        result.syatekiData().gamers().forEach(gamer -> this.updatePersonalDataIfNeeded(result, gamer));
    }

    private void updatePersonalDataIfNeeded(final SyatekiResult result, final Gamer gamer) {
        this.personalDataSet.stream()
                .filter(data -> data.gamer().equals(gamer))
                .findFirst()
                .ifPresentOrElse(data -> this.jsonManager.savePersonalData(data.update(result)), () -> {
                    final var data = new SyatekiPersonalData(gamer).update(result);
                    this.personalDataSet.add(data);
                    this.jsonManager.savePersonalData(data);
                });
    }

    private void loadPersonalData() {
        this.personalDataSet.clear();
        this.personalDataSet.addAll(this.jsonManager.loadPersonalData());
    }

    public boolean hasActiveGame(final Player player) {
        return this.hasActiveGame(new Gamer(player.getUniqueId()));
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

    @UnmodifiableView
    public Set<SyatekiResult> results() {
        return Collections.unmodifiableSet(this.results);
    }

    @UnmodifiableView
    public Set<SyatekiPersonalData> personalDataSet() {
        return Collections.unmodifiableSet(this.personalDataSet);
    }
}
