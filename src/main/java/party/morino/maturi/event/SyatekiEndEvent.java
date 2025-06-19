package party.morino.maturi.event;

import party.morino.maturi.game.syateki.Syateki;
import party.morino.maturi.game.syateki.data.SyatekiResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when {@link Syateki} end.
 */
@DefaultQualifier(NonNull.class)
public final class SyatekiEndEvent extends Event {

    private final Syateki syateki;
    private final SyatekiResult syatekiResult;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public SyatekiEndEvent(final Syateki syateki, final SyatekiResult syatekiResult) {
        this.syateki = syateki;
        this.syatekiResult = syatekiResult;
    }

    /**
     * Get {@link Syateki}.
     * @return the syateki
     */
    public Syateki getSyateki() {
        return this.syateki;
    }

    /**
     * Get {@link SyatekiResult}.
     * @return the syateki result
     */
    public SyatekiResult getSyatekiResult() {
        return this.syatekiResult;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }
}
