package github.tyonakaisan.maturi.game;

import github.tyonakaisan.maturi.Maturi;
import github.tyonakaisan.maturi.MaturiProvider;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.time.Duration;

@DefaultQualifier(NonNull.class)
public final class TimeBar {

    private final Maturi maturi;

    private final GameData gameData;
    private @Nullable BukkitTask task;
    private @Nullable BossBar bossBar;

    public TimeBar(final GameData gameData) {
        this.gameData = gameData;
        this.maturi = MaturiProvider.instance();
    }

    public void enable() {
        if (this.bossBar != null) {
            return;
        }

        if (!this.gameData.duration().isPositive()) {
            throw new IllegalArgumentException("Must be more than 0");
        }

        this.bossBar = BossBar.bossBar(
                Component.empty(),
                1f,
                BossBar.Color.BLUE,
                BossBar.Overlay.NOTCHED_10
        );
        this.task = new TimerBarUpdater(this.gameData.duration()).runTaskTimer(this.maturi, 0, 20);
    }

    public void disable() {
        if (this.bossBar != null) {
            this.gameData.gamers().forEach(gamer -> gamer.hideBossBar(this.bossBar));
        }

        if (this.task != null) {
            this.task.cancel();
        }

        this.task = null;
        this.bossBar = null;
    }

    private final class TimerBarUpdater extends BukkitRunnable {

        private final Duration duration;
        private Duration remaining;

        public TimerBarUpdater(final Duration duration) {
            this.duration = duration;
            this.remaining = duration;
        }

        @Override
        public void run() {
            if (bossBar == null || !this.duration.isPositive()) {
                disable();
                return;
            }

            bossBar.name(Component.text("%01d:%02d".formatted(this.remaining.toMinutesPart(), this.remaining.toSecondsPart())));
            bossBar.progress((float) this.remaining.toSeconds() / this.duration.toSeconds());
            gameData.gamers().forEach(gamer -> gamer.showBossBar(bossBar));
            this.remaining = this.remaining.minusSeconds(1);
        }
    }
}
