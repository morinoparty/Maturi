package party.morino.maturi.game;


import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.MaturiProvider;

import java.time.Duration;
import java.util.*;

@NullMarked
public final class SideBar {

    private final GameData gameData;
    private final Map<UUID, FastBoard> boardMap = new HashMap<>();

    public SideBar(final GameData gameData) {
        this.gameData = gameData;
    }

    public void enable() {
        this.gameData.players().forEach(player -> {
            final var board = new FastBoard(player);
            board.updateTitle(Component.translatable("syateki.game.board_title"));
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

    public void update(final List<Component> lines, final Gamer gamer) {
        final List<Component> renderLines = lines.stream()
                .map(line -> GlobalTranslator.renderer().render(line, gamer.getOrDefault(Identity.LOCALE, Locale.US)))
                .toList();
        this.boardMap.values().forEach(board -> board.updateLines(renderLines));
    }
}
