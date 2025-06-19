package party.morino.maturi.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import party.morino.maturi.command.MaturiCommand;
import party.morino.maturi.config.ConfigFactory;
import party.morino.maturi.game.syateki.SyatekiManager;
import party.morino.maturi.message.Messages;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class ReloadCommand implements MaturiCommand {

    private final ConfigFactory configFactory;
    private final Messages messages;
    private final SyatekiManager syatekiManager;

    @Inject
    public ReloadCommand(
            final ConfigFactory configFactory,
            final Messages messages,
            final SyatekiManager syatekiManager
    ) {
        this.configFactory = configFactory;
        this.messages = messages;
        this.syatekiManager = syatekiManager;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("reload")
                .requires(source -> source.getSender().hasPermission("maturi.command.reload"))
                .executes(context -> {
                    final var sender = context.getSource().getSender();
                    this.configFactory.reloadPrimaryConfig();
                    this.messages.reloadMessage();
                    this.syatekiManager.reload();

                    sender.sendMessage(Messages.translate("command.reload.success.reload", sender));

                    return Command.SINGLE_SUCCESS;
                });
    }
}