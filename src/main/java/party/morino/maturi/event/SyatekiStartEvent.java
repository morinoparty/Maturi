package party.morino.maturi.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.game.syateki.Syateki;

/**
 * Event called when {@link Syateki} start.
 */
@NullMarked
public final class SyatekiStartEvent extends Event {

    private final Syateki syateki;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public SyatekiStartEvent(final Syateki syateki) {
        this.syateki = syateki;
    }

    /**
     * Get {@link Syateki}.
     * @return the syateki
     */
    public Syateki getSyateki() {
        return this.syateki;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }
}
