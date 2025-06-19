package party.morino.maturi.game;


import fr.mrmicky.fastboard.adventure.FastBoard;
import party.morino.maturi.MaturiProvider;
import party.morino.maturi.message.Messages;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.time.Duration;
import java.util.*;

@DefaultQualifier(NonNull.class)
public final class SideBar {

    private final GameData gameData;
    private final Map<UUID, FastBoard> boardMap = new HashMap<>();

    public SideBar(final GameData gameData) {
        this.gameData = gameData;
    }

    public void enable() {
        this.gameData.players().forEach(player -> {
            final var board = new FastBoard(player);
            board.updateTitle(Messages.translate("syateki.game.board_title", player));
            this.boardMap.put(player.getUniqueId(), board);
        });
    }

    public void disableDelay(final Duration duration) {
        Bukkit.getServer().getScheduler().runTaskLater(MaturiProvider.instance(), this::disable, duration.getSeconds() * 20);
    }

    public void disable() {
        this.boardMap.values().forEach(FastBoard::delete);
        this.boardMap.clear();
    }

    public void update(final List<Component> lines) {
        this.boardMap.values().forEach(board -> board.updateLines(lines));
    }
}
