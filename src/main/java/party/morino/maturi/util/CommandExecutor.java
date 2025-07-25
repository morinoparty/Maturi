package party.morino.maturi.util;

import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CommandExecutor {

    private CommandExecutor() {
    }

    public static void execute(final String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }
}
