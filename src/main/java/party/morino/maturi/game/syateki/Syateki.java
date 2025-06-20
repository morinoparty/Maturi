package party.morino.maturi.game.syateki;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import party.morino.maturi.Maturi;
import party.morino.maturi.MaturiProvider;
import party.morino.maturi.event.SyatekiEndEvent;
import party.morino.maturi.event.SyatekiPreCountdownEvent;
import party.morino.maturi.event.SyatekiStartEvent;
import party.morino.maturi.game.*;
import party.morino.maturi.game.syateki.data.SyatekiData;
import party.morino.maturi.game.syateki.data.SyatekiResult;
import party.morino.maturi.util.CommandExecutor;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NullMarked
public final class Syateki {

    private final Maturi maturi;
    private final SyatekiData syatekiData;
    private final List<LootTable> lootTables;
    private final ScoreHolder scoreHolder;
    private final TimeBar timeBar;
    private final SideBar sideBar;
    private @Nullable BukkitTask timerTask;

    private boolean enabled = false;
    private Phase phase = Phase.EMPTY;
    private boolean bonus = false;
    private final int chance = 20;

    public Syateki(
            final SyatekiData syatekiData,
            final List<LootTable> lootTables
    ) {
        this.maturi = MaturiProvider.instance();
        this.syatekiData = syatekiData;
        this.lootTables = lootTables;
        this.scoreHolder = new ScoreHolder();
        this.timeBar = new TimeBar(syatekiData);
        this.sideBar = new SideBar(syatekiData);
    }

    public boolean enabled() {
        return this.enabled;
    }

    public Phase phase() {
        return this.phase;
    }

    public boolean bonus() {
        return this.bonus;
    }

    public SyatekiData syatekiData() {
        return this.syatekiData;
    }

    public ScoreHolder scoreHolder() {
        return this.scoreHolder;
    }

    public TimeBar timeBar() {
        return this.timeBar;
    }

    public SideBar sideBar() {
        return this.sideBar;
    }

    public boolean containsPlayer(final Player player) {
        return this.containsPlayer(new Gamer(player.getUniqueId()));
    }

    public boolean containsPlayer(final Gamer gamer) {
        return this.syatekiData.gamers().stream()
                .anyMatch(gamer1 -> gamer1.equals(gamer));
    }

    public void countdown() {
        if (this.enabled) {
            return;
        }

        if (!this.syatekiData.duration().isPositive()) {
            throw new IllegalArgumentException("Must be more than 0");
        }

        new SyatekiPreCountdownEvent(this).callEvent();

        this.syatekiData.players().forEach(player -> {
            player.teleport(this.syatekiData.syatekiArea().spawnPoint());
            this.giveBow(player);
        });

        final var delay = Duration.ofSeconds(3);
        new BukkitRunnable() {
            private int timer = (int) delay.getSeconds();
            @Override
            public void run() {
                if (this.timer > 0) {
                    syatekiData.players().forEach(player -> {
                        final var sound = Sound.sound()
                                .type(Key.key("minecraft:block.note_block.bell"))
                                .pitch(0.5f)
                                .volume(0.3f)
                                .build();
                        player.showTitle(
                                Title.title(
                                        Component.empty(),
                                        Component.translatable("syateki.start.countdown", Argument.numeric("time", this.timer))
                                ));
                        player.playSound(sound);
                    });
                } else {
                    this.cancel();
                }
                this.timer--;
            }
        }.runTaskTimer(this.maturi, 0, 20);

        Bukkit.getServer().getScheduler().runTaskLater(this.maturi, this::start, delay.getSeconds() * 20);
        this.phase = Phase.COUNTDOWN;
    }

    public void start() {
        if (this.enabled) {
            return;
        }

        if (!this.syatekiData.duration().isPositive()) {
            throw new IllegalArgumentException("Must be more than 0");
        }

        new SyatekiStartEvent(this).callEvent();

        this.syatekiData.players().forEach(player -> {
            final var sound = Sound.sound()
                    .type(Key.key("minecraft:block.trial_spawner.eject_item"))
                    .pitch(1.25f)
                    .build();
            player.showTitle(Title.title(Component.empty(), Component.translatable("syateki.start.sub_title")));
            player.playSound(sound);
        });

        this.timeBar.enable();
        this.sideBar.enable();

        this.enabled = true;
        this.timerTask = this.maturi.getServer().getScheduler().runTaskLater(this.maturi, this::end, this.syatekiData.duration().getSeconds() * 20);
        this.phase = Phase.IN_GAME;
    }

    public void end() {
        if (!this.enabled) {
            return;
        }

        final var result = new SyatekiResult(this.syatekiData, Instant.now(), this.scoreHolder.score());
        new SyatekiEndEvent(this, result).callEvent();

        if (this.timerTask != null) {
            this.timerTask.cancel();
        }

        this.syatekiData.players().forEach(player -> {
            final var sound = Sound.sound()
                    .type(Key.key("minecraft:entity.player.levelup"))
                    .pitch(1.25f)
                    .volume(0.5f)
                    .build();
            player.showTitle(Title.title(Component.empty(), Component.translatable("syateki.end.sub_title")));
            player.sendMessage(Component.translatable("syateki.end.point_announce", Argument.numeric("point", this.scoreHolder.score())));
            player.playSound(sound);
        });
        this.selectLoot();

        this.timeBar.disable();
        this.sideBar.disableDelay(Duration.ofSeconds(10));

        this.enabled = false;
        this.timerTask = null;
        this.phase = Phase.EMPTY;
    }

    public int score() {
        if (bonus) {
            this.bonus = this.tryBonus();
            return 2;
        } else {
            this.bonus = this.tryBonus();
            return 1;
        }
    }

    private boolean tryBonus() {
        return this.chance >= ThreadLocalRandom.current().nextInt(0, 100);
    }

    private void giveBow(final Player player) {
        player.getInventory().addItem(this.bow());
        player.getInventory().addItem(this.arrow());
    }

    private ItemStack arrow() {
        return new ItemStack(Material.ARROW);
    }

    // 要検討
    private ItemStack bow() {
        final var itemStack = new ItemStack(Material.BOW);
        itemStack.editMeta(itemMeta -> {
            itemMeta.setCustomModelData(1);
            itemMeta.displayName(MiniMessage.miniMessage().deserialize("<gradient:#c31432:#240b36><!italic><bold>射的の弓"));
            itemMeta.setEnchantmentGlintOverride(false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addEnchant(Enchantment.INFINITY, 1, false);
            if (itemMeta instanceof Repairable repairable) {
                repairable.setRepairCost(Integer.MAX_VALUE); // Enchantment Prevention
            }
        });
        return itemStack;
    }

    private void selectLoot() {
        final var score = this.scoreHolder.score();
        final var players = this.syatekiData.players();
        this.lootTables.stream()
                .filter(lootTable -> lootTable.scoreRange().isWithinRange(score))
                .findFirst()
                .ifPresent(lootTable -> {
                    final var commands = lootTable.commands();
                    players.forEach(player -> commands.forEach(command -> CommandExecutor.execute(command.replace("<player>", player.getName()))));
                });
    }

    public enum Difficulty {
        EASY,
        // NORMAL, // ...?
        HARD,
        // SCORE_ATTACK //...?
    }

    public enum Phase {
        EMPTY,
        COUNTDOWN,
        IN_GAME
    }
}
