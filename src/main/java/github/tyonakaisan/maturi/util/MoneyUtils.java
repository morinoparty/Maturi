package github.tyonakaisan.maturi.util;

import github.tyonakaisan.maturi.game.Gamer;
import github.tyonakaisan.maturi.integration.EconomyProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
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
