package party.morino.maturi.game;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import party.morino.maturi.Maturi;
import party.morino.maturi.MaturiProvider;

import java.time.Duration;

@NullMarked
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
