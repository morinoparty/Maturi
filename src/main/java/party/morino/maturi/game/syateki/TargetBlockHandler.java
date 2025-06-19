package party.morino.maturi.game.syateki;

import com.destroystokyo.paper.ParticleBuilder;
import party.morino.maturi.MaturiProvider;
import party.morino.maturi.game.Area;
import party.morino.maturi.util.Style;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@DefaultQualifier(NonNull.class)
public final class TargetBlockHandler {

    private TargetBlockHandler() {
    }

    public static void randomSpawn(final Location location, final Area area, final List<Player> players, final boolean bonus) {
        destroy(location, players);

        final var x = getPosition(area.firstCorner().getBlockX(), area.secondCorner().getBlockX());
        final var y = getPosition(area.firstCorner().getBlockY(), area.secondCorner().getBlockY());
        final var z = getPosition(area.firstCorner().getBlockZ(), area.secondCorner().getBlockZ());
        final var world = location.getWorld();
        final var newLocation = new Location(world, x, y, z);

        if (!newLocation.getBlock().isEmpty()) {
            randomSpawn(location, area, players, bonus);
            return;
        }
        spawn(newLocation, players, bonus);
    }

    public static void spawn(final Location location, final List<Player> players, final boolean bonus) {
        if (!location.getBlock().isEmpty()) {
            return;
        }

        location.getBlock().setType(Material.TARGET);
        spawnAnimation(location, players, bonus);
    }

    public static void destroy(final Location location, final List<Player> players) {
        if (!location.getBlock().getType().equals(Material.TARGET)) {
            return;
        }
        final var destroyLocation = location.clone();

        Bukkit.getServer().getScheduler().runTaskLater(MaturiProvider.instance(), () -> destroyLocation.getBlock().setType(Material.AIR), 1);
        destroyAnimation(location, players);
    }

    private static int getPosition(final int first, final int second) {
        final var min = Math.min(first, second);
        final var max = Math.max(first, second);
        if (max == min) {
            return max;
        } else {
            return ThreadLocalRandom.current().nextInt(min, max);
        }
    }

    private static void spawnAnimation(final Location location, final List<Player> players, final boolean bonus) {
        final var particle = bonus
                ? Particle.TRIAL_SPAWNER_DETECTION_OMINOUS
                : Particle.TRIAL_SPAWNER_DETECTION;
        new ParticleBuilder(particle)
                .location(location.add(0.5, 0, 0.5))
                .offset(0.4, 0.3, 0.4)
                .count(30)
                .extra(0)
                .spawn();
        final var sound1 = Sound.sound()
                .type(Key.key("minecraft:entity.illusioner.mirror_move"))
                .pitch(2f)
                .volume(0.35f)
                .build();
        players.forEach(player -> player.playSound(sound1));

        if (bonus) {
            final var max = 40;
            for (int i = 0; i < max; i++) {
                final var vector = Style.rotate(Style.circle(i, max, 0.5), 90, 0);
                final var particleLocation = location.clone().add(vector).add(0, 0.25, 0);
                new ParticleBuilder(Particle.ENCHANTED_HIT)
                        .location(particleLocation)
                        .offset(vector.getX(), vector.getY(), vector.getZ())
                        .extra(2)
                        .count(0)
                        .spawn();
            }

            final var sound2 = Sound.sound()
                    .type(Key.key("minecraft:block.beacon.power_select"))
                    .pitch(1.5f)
                    .volume(0.3f)
                    .build();
            players.forEach(player -> player.playSound(sound2));
        }
    }

    private static void destroyAnimation(final Location location, final List<Player> players) {
        new ParticleBuilder(Particle.END_ROD)
                .location(location.add(0.5, 0.5, 0.5))
                .offset(0.3, 0.3, 0.3)
                .count(5)
                .extra(0.1)
                .spawn();
        final var sound1 = Sound.sound()
                .type(Key.key("minecraft:entity.ender_eye.death"))
                .pitch(1.5f)
                .volume(0.35f)
                .build();
        final var sound2 = Sound.sound()
                .type(Key.key("minecraft:entity.armadillo.scute_drop"))
                .pitch(1.5f)
                .volume(0.15f)
                .build();
        players.forEach(player -> {
            player.playSound(sound1);
            player.playSound(sound2);
        });
    }
}
