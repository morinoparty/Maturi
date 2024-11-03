package github.tyonakaisan.maturi.util;

import org.bukkit.Bukkit;

public final class CommandExecutor {

    private CommandExecutor() {
    }

    public static void execute(final String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }
}
