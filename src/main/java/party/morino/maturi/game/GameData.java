package party.morino.maturi.game;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface representing the data required to start a game.
 */
public interface GameData {
    /**
     * Returns the unique identifier for the game.
     *
     * @return the UUID of the game
     */
    UUID uuid();

    /**
     * Returns the list of {@link Gamer}s participating in the game.
     *
     * @return the list of {@link Gamer}
     */
    List<Gamer> gamers(); // team...???

    /**
     * Returns the {@link Duration} for which the game will run.
     *
     * @return the {@link Duration} of the game
     */
    Duration duration();

    /**
     * Returns the players.
     * @return the players
     */
    default List<Player> players() {
        return gamers().stream()
                .map(Gamer::player)
                .flatMap(Optional::stream)
                .toList();
    }
}
