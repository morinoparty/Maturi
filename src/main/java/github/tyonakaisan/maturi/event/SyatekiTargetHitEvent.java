package github.tyonakaisan.maturi.event;

import github.tyonakaisan.maturi.game.Gamer;
import github.tyonakaisan.maturi.game.syateki.Syateki;
import io.papermc.paper.event.block.TargetHitEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a {@link Gamer} in {@link Syateki} shoots a Target block.
 */
@DefaultQualifier(NonNull.class)
public final class SyatekiTargetHitEvent extends TargetHitEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Syateki syateki;
    private final Gamer shooter;

    public SyatekiTargetHitEvent(final @NotNull Projectile projectile, final @NotNull Block block, final @NotNull BlockFace blockFace, final int signalStrength, final Syateki syateki, final Gamer shooter) {
        super(projectile, block, blockFace, signalStrength);
        this.syateki = syateki;
        this.shooter = shooter;
    }

    /**
     * Get {@link Syateki}.
     * @return the {@link Syateki}
     */
    public Syateki getSyateki() {
        return this.syateki;
    }

    /**
     * Get shooter.
     * @return the {@link Gamer}
     */
    public Gamer getShooter() {
        return this.shooter;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
