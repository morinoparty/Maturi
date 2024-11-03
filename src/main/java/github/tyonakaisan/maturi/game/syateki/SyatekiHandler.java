package github.tyonakaisan.maturi.game.syateki;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import github.tyonakaisan.maturi.config.ConfigFactory;
import github.tyonakaisan.maturi.game.Gamer;
import github.tyonakaisan.maturi.game.syateki.data.SyatekiData;
import github.tyonakaisan.maturi.message.Messages;
import github.tyonakaisan.maturi.util.MoneyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@DefaultQualifier(NonNull.class)
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
            gamers.forEach(gamer -> gamer.sendMessage(Messages.translate("syateki.start.alredy_start", gamer)));
            return;
        }

        // インベントリが空でなければリターン
        if (gamers.stream().anyMatch(gamer -> !gamer.isInventoryEmpty())) {
            gamers.forEach(gamer -> gamer.sendMessage(Messages.translate("syateki.start.inventory_not_empty", gamer)));
            return;
        }

        // 設定されているエリア数よりもアクティブ数の方が多ければリターン
        final var areas = settings.areas();
        if (areas.size() <= this.syatekiManager.actives(difficulty)) {
            gamers.forEach(gamer -> gamer.sendMessage(Messages.translate("syateki.start.not_found_area", gamer)));
            return;
        }

        // 開始するための必要なお金を持っていなければリターン
        final var fee = settings.entranceFee();
        if (gamers.stream().anyMatch(gamer -> !MoneyUtils.hasEnoughMoney(gamer, fee))) {
            gamers.forEach(gamer -> gamer.sendMessage(Messages.translate("maturi.not_enough_money", gamer)));
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
            gamer.sendMessage(Messages.translate("syateki.game.in_restricted_area", gamer));
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

        final var list = List.of(Messages.translate("syateki.game.point", gamer, builder -> builder.tag("point", Tag.selfClosingInserting(Component.text(scoreHolder.score())))));
        syateki.sideBar().update(list);
        gamer.showTitle(Title.title(Component.empty(), Messages.translate("syateki.game.hit_announce", gamer, builder -> builder.tag("point", Tag.selfClosingInserting(Component.text(score))))));
    }
}
