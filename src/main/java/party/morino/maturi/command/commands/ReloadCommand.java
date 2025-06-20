package party.morino.maturi.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.command.MaturiCommand;
import party.morino.maturi.config.ConfigFactory;
import party.morino.maturi.game.syateki.SyatekiManager;
import party.morino.maturi.message.Messages;

import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
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

                    sender.sendMessage(Component.translatable("command.reload.success.reload"));

                    return Command.SINGLE_SUCCESS;
                });
    }
}