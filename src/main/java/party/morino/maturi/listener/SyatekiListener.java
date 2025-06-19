package party.morino.maturi.listener;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.inject.Inject;
import party.morino.maturi.event.SyatekiEndEvent;
import party.morino.maturi.event.SyatekiPreCountdownEvent;
import party.morino.maturi.event.SyatekiTargetHitEvent;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.syateki.SyatekiHandler;
import party.morino.maturi.game.syateki.SyatekiManager;
import party.morino.maturi.message.Messages;
import party.morino.maturi.util.Style;
import io.papermc.paper.event.block.TargetHitEvent;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class SyatekiListener implements Listener {

    private final SyatekiManager syatekiManager;
    private final SyatekiHandler syatekiHandler;


    @Inject
    public SyatekiListener(
            final SyatekiManager syatekiManager,
            final SyatekiHandler syatekiHandler
    ) {
        this.syatekiManager = syatekiManager;
        this.syatekiHandler = syatekiHandler;
    }

    @EventHandler
    public void onEvent(final TargetHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player player) {
            this.syatekiManager.matchActiveGame(player)
                    .ifPresent(syateki -> {
                        final var shooter = new Gamer(player.getUniqueId());
                        final var syatekiEvent = new SyatekiTargetHitEvent(event.getEntity(), Objects.requireNonNull(event.getHitBlock()), Objects.requireNonNull(event.getHitBlockFace()), event.getSignalStrength(), syateki, shooter);
                        syatekiEvent.callEvent();
                    });
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final var player = event.getPlayer();
        if (!this.syatekiManager.hasActiveGame(player) || player.hasPermission("maturi.bypass.command")) {
            return;
        }

        player.sendMessage(Messages.translate("syateki.game.command_cancelled", player));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPick(final EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && this.syatekiManager.hasActiveGame(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onStart(final SyatekiPreCountdownEvent event) {
        this.syatekiManager.addActive(event.getSyateki());
    }

    @EventHandler
    public void onEnd(final SyatekiEndEvent event) {
        this.syatekiManager.removeActive(event.getSyateki());
        this.syatekiManager.saveResult(event.getSyatekiResult());
    }

    @EventHandler
    public void onHit(final SyatekiTargetHitEvent event) {
        if (event.getEntity().getShooter() instanceof Entity entity) {
            final var syateki = event.getSyateki();
            final var hitLocation = Objects.requireNonNull(event.getHitBlock()).getLocation().clone();
            final var shooterLocation = entity.getLocation();
            final var gamer = event.getShooter();
            this.syatekiHandler.hit(syateki, hitLocation, shooterLocation, gamer);
        }

        if (event.getHitBlockFace() != null) {
            final var direction = Direction.fromVector(event.getHitBlockFace().getDirection());
            this.animation(event.getEntity().getLocation(), direction, 40);
        }
    }

    private void animation(final Location location, final Direction direction, final int max) {
        for (int i = 0; i < max; i++) {
            final var vector = Style.rotate(Style.circle(i, max, 0.25), direction.pitch(), direction.yaw());
            final var particleLocation = location.clone().add(vector);
            new ParticleBuilder(Particle.CRIT)
                    .location(particleLocation)
                    .offset(vector.getX(), vector.getY(), vector.getZ())
                    .extra(2)
                    .count(0)
                    .spawn();
        }
    }

    public record Direction(float pitch, float yaw) {
        // From Location#setDirection
        public static Direction fromVector(final Vector vector) {
            final var _2PI = 2 * Math.PI;
            final var x = vector.getX();
            final var z = vector.getZ();

            if (x == 0 && z == 0) {
                return new Direction(vector.getY() > 0 ? -90.0f : 90.0f, 0.0f);
            }


            final var theta = Math.atan2(-x, z);

            final var x2 = NumberConversions.square(x);
            final var z2 = NumberConversions.square(z);
            final var xz = Math.sqrt(x2 + z2);

            final var pitch = Math.toDegrees(Math.atan(-vector.getY() / xz));
            final var yaw = Math.toDegrees((theta + _2PI) % _2PI);

            return new Direction((float) pitch, (float) yaw);
        }
    }
}
