package party.morino.maturi.event;

import party.morino.maturi.game.syateki.Syateki;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called before the {@link Syateki} countdown begins.
 */
public class SyatekiPreCountdownEvent extends Event {

    private final Syateki syateki;
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public SyatekiPreCountdownEvent(final Syateki syateki) {
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
