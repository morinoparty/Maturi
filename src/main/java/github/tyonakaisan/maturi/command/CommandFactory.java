package github.tyonakaisan.maturi.command;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.tyonakaisan.maturi.command.commands.KakigooriCommand;
import github.tyonakaisan.maturi.command.commands.ReloadCommand;
import github.tyonakaisan.maturi.command.commands.SyatekiCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class CommandFactory {

    private final KakigooriCommand kakigooriCommand;
    private final ReloadCommand reloadCommand;
    private final SyatekiCommand syatekiCommand;

    private static final LiteralArgumentBuilder<CommandSourceStack> FIRST_LITERAL_ARGUMENT = Commands.literal("maturi");
    private static final List<String> ALIASES = List.of("mt");

    @Inject
    public CommandFactory(
            final KakigooriCommand kakigooriCommand,
            final ReloadCommand reloadCommand,
            final SyatekiCommand syatekiCommand
    ) {
        this.kakigooriCommand = kakigooriCommand;
        this.reloadCommand = reloadCommand;
        this.syatekiCommand = syatekiCommand;
    }

    public void registerViaBootstrap(final BootstrapContext context) {
        final LifecycleEventManager<BootstrapContext> lifecycleManager = context.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
                event.registrar().register(this.bootstrapCommands(), null, ALIASES)
        );
    }

    public void registerViaEnable(final JavaPlugin plugin) {
        final LifecycleEventManager<Plugin> lifecycleManager = plugin.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
                event.registrar().register(this.pluginCommands(), null, ALIASES)
        );
    }

    private LiteralCommandNode<CommandSourceStack> bootstrapCommands() {
        return this.literal()
                .build();
    }

    private LiteralCommandNode<CommandSourceStack> pluginCommands() {
        return this.literal()
                .then(this.reloadCommand.init())
                .then(this.syatekiCommand.init())
                .then(this.kakigooriCommand.init())
                .build();

    }

    private LiteralArgumentBuilder<CommandSourceStack> literal() {
        return FIRST_LITERAL_ARGUMENT
                .requires(source -> source.getSender().hasPermission("maturi.command"));
    }
}
