package github.tyonakaisan.example;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import github.tyonakaisan.example.listener.ExampleListener;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class ExampleModule extends AbstractModule {

    private final Example example;

    ExampleModule(
            final Example example
    ) {
        this.example = example;
    }

    @Override
    public void configure() {
        this.bind(Example.class).toInstance(this.example);
        this.bind(Server.class).toInstance(this.example.getServer());

        this.configureListener();
    }

    private void configureListener() {
        final Multibinder<Listener> listeners = Multibinder.newSetBinder(this.binder(), Listener.class);
        listeners.addBinding().to(ExampleListener.class).in(Scopes.SINGLETON);
    }
}
