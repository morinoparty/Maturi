package github.tyonakaisan.example;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import github.tyonakaisan.example.listener.ExampleListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public final class ExampleModule extends AbstractModule {

    private final ComponentLogger logger;
    private final Example example;
    private final Path dataDirectory;

    ExampleModule(
            final Example example,
            final Path dataDirectory,
            final ComponentLogger logger
    ) {
        this.example = example;
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    @Override
    public void configure() {
        this.bind(ComponentLogger.class).toInstance(this.logger);
        this.bind(Example.class).toInstance(this.example);
        this.bind(Server.class).toInstance(this.example.getServer());
        this.bind(Path.class).toInstance(this.dataDirectory);

        this.configureListener();
    }

    private void configureListener() {
        final Multibinder<Listener> listeners = Multibinder.newSetBinder(this.binder(), Listener.class);
        listeners.addBinding().to(ExampleListener.class).in(Scopes.SINGLETON);
    }
}
