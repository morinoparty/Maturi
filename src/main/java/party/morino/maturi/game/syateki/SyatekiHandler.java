package party.morino.maturi.game.syateki;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.config.ConfigFactory;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.syateki.data.SyatekiData;
import party.morino.maturi.util.MoneyUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@NullMarked
@Singleton
public final class SyatekiHandler {

    private final SyatekiManager syatekiManager;
    private final ConfigFactory configFactory;

    private final Map<Syateki, Long> called = new HashMap<>(); // w

    @Inject
    public SyatekiHandler(
            final SyatekiManager syatekiManager,
            final ConfigFactory configFactory
    ) {
        this.syatekiManager = syatekiManager;
        this.configFactory = configFactory;
    }

    public void start(final Syateki.Difficulty difficulty, final Duration duration, final List<Gamer> gamers) {
        final var settings = this.configFactory.primaryConfig().syateki(difficulty);
        // ゲーム中であればリターン
        if (gamers.stream().anyMatch(this.syatekiManager::hasActiveGame)) {
            gamers.forEach(gamer -> gamer.sendMessage(Component.translatable("syateki.start.alredy_start")));
            return;
        }

        // インベントリが空でなければリターン
        if (gamers.stream().anyMatch(gamer -> !gamer.isInventoryEmpty())) {
            gamers.forEach(gamer -> gamer.sendMessage(Component.translatable("syateki.start.inventory_not_empty")));
            return;
        }

        // 設定されているエリア数よりもアクティブ数の方が多ければリターン
        final var areas = settings.areas();
        if (areas.size() <= this.syatekiManager.actives(difficulty)) {
            gamers.forEach(gamer -> gamer.sendMessage(Component.translatable("syateki.start.not_found_area")));
            return;
        }

        // 開始するための必要なお金を持っていなければリターン
        final var fee = settings.entranceFee();
        if (gamers.stream().anyMatch(gamer -> !MoneyUtils.hasEnoughMoney(gamer, fee))) {
            gamers.forEach(gamer -> gamer.sendMessage(Component.translatable("maturi.not_enough_money")));
            return;
        }
        gamers.forEach(gamer -> MoneyUtils.takeMoney(gamer, fee));

        final var syatekiData = new SyatekiData(UUID.randomUUID(), gamers, duration, areas.get(this.syatekiManager.actives(difficulty)), difficulty);
        new Syateki(syatekiData, settings.lootTable()).countdown();
    }

    public void hit(final Syateki syateki, final Location hitLocation, final Location shooterLocation, final Gamer gamer) {
        if (syateki.phase() != Syateki.Phase.IN_GAME) {
            return;
        }

        // Checks to prevent concurrent execution
        this.called.putIfAbsent(syateki, 0L);
        if (System.currentTimeMillis() - this.called.get(syateki) < 10) {
            return;
        }

        // Return if inside the restricted area.
        final var syatekiData = syateki.syatekiData();
        final var restrictedArea = syatekiData.syatekiArea().restrictedArea();
        if (restrictedArea.in(shooterLocation)) {
            gamer.sendMessage(Component.translatable("syateki.game.in_restricted_area"));
            return;
        }

        final var targetSpawnArea = syatekiData.syatekiArea().targetSpawnArea();
        if (targetSpawnArea.in(hitLocation)) {
            this.incrementScore(syateki, gamer);
            TargetBlockHandler.randomSpawn(hitLocation, targetSpawnArea, syatekiData.players(), syateki.bonus());
        }
        this.called.put(syateki, System.currentTimeMillis());
    }

    private void incrementScore(final Syateki syateki, final Gamer gamer) {
        final var scoreHolder = syateki.scoreHolder();
        final var score = syateki.score();

        scoreHolder.increment(score);

        final List<Component> list = List.of(Component.translatable("syateki.game.point", Argument.numeric("point", scoreHolder.score())));
        syateki.sideBar().update(list, gamer);
        gamer.showTitle(Title.title(Component.empty(), Component.translatable("syateki.game.hit_announce", Argument.numeric("point", score))));
    }
}
