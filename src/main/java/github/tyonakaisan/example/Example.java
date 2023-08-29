package github.tyonakaisan.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import github.tyonakaisan.example.command.ExampleCommand;
import github.tyonakaisan.example.command.commands.ReloadCommand;
import github.tyonakaisan.example.listener.ExampleListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;
import java.util.Set;

@DefaultQualifier(NonNull.class)
public final class Example extends JavaPlugin {

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(
            ExampleListener.class
    );
    private static final Set<Class<? extends ExampleCommand>> COMMAND_CLASSES = Set.of(
            ReloadCommand.class
    );
    private final Injector injector;

    public Example(
            final Path dataDirectory,
            final ComponentLogger logger
    ) {
        this.injector = Guice.createInjector(new ExampleModule(this, dataDirectory, logger));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Listeners
        for (final Class<? extends Listener> listenerClass : LISTENER_CLASSES) {
            var listener = this.injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listener, this);
        }

        // Commands
        for (final Class<? extends ExampleCommand> commandClass : COMMAND_CLASSES) {
            var command = this.injector.getInstance(commandClass);
            command.init();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
