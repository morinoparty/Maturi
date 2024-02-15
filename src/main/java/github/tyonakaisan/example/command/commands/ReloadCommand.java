package github.tyonakaisan.example.command.commands;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import github.tyonakaisan.example.command.ExampleCommand;
import github.tyonakaisan.example.config.ConfigFactory;
import github.tyonakaisan.example.message.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class ReloadCommand implements ExampleCommand {

    private final ConfigFactory configFactory;
    private final Messages messages;
    private final CommandManager<CommandSender> commandManager;

    @Inject
    public ReloadCommand(
            final ConfigFactory configFactory,
            final Messages messages,
            final CommandManager<CommandSender> commandManager
    ) {
        this.configFactory = configFactory;
        this.messages = messages;
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
                    sender.sendMessage(this.messages.translatable(Messages.Style.SUCCESS, sender, "command.reload.success"));
                })
                .build();

        this.commandManager.command(command);
    }
}