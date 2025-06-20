package party.morino.maturi.util;

import org.jspecify.annotations.NullMarked;
import party.morino.maturi.compat.EconomyProvider;
import party.morino.maturi.game.Gamer;

@NullMarked
public final class MoneyUtils {

    private MoneyUtils() {
    }

    public static boolean hasEnoughMoney(final Gamer gamer, final double amount) {
        final var playerOpt = gamer.player();

        if (playerOpt.isEmpty()) {
            return false;
        }

        final var player = playerOpt.get();
        if (player.hasPermission("maturi.bypass.money")) {
            return true;
        }

        return EconomyProvider.economy().getBalance(player) >= amount;
    }

    public static void takeMoney(final Gamer gamer, final double amount) {
        gamer.player()
                .filter(player -> !player.hasPermission("maturi.bypass.money"))
                .ifPresent(player -> EconomyProvider.economy().withdrawPlayer(player, amount));
    }
}
