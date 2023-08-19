package github.tyonakaisan.example.command.commands;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import github.tyonakaisan.example.command.ExampleCommand;
import github.tyonakaisan.example.config.ConfigFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class ReloadCommand implements ExampleCommand {

    private final ConfigFactory configFactory;
    private final CommandManager<CommandSender> commandManager;

    @Inject
    public ReloadCommand(
            final ConfigFactory configFactory,
            final CommandManager<CommandSender> commandManager
    ) {
        this.configFactory = configFactory;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("example", "ex")
                .literal("reload")
                .permission("example.command.reload")
                .senderType(CommandSender.class)
                .handler(handler -> {
                    this.configFactory.reloadPrimaryConfig();
                    final var sender = (Player) handler.getSender();
                    sender.sendRichMessage("config reloaded!");
                })
                .build();

        this.commandManager.command(command);
    }
}