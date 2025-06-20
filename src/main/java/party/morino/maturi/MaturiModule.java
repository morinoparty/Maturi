package party.morino.maturi;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.listener.MaturiListener;
import party.morino.maturi.listener.SyatekiListener;

@NullMarked
public final class MaturiModule extends AbstractModule {

    private final Maturi maturi;

    MaturiModule(
            final Maturi maturi
    ) {
        this.maturi = maturi;
    }

    @Override
    public void configure() {
        this.bind(Maturi.class).toInstance(this.maturi);
        this.bind(Server.class).toInstance(this.maturi.getServer());

        this.configureListener();
    }

    private void configureListener() {
        final Multibinder<Listener> listeners = Multibinder.newSetBinder(this.binder(), Listener.class);
        listeners.addBinding().to(SyatekiListener.class).in(Scopes.SINGLETON);
        listeners.addBinding().to(MaturiListener.class).in(Scopes.SINGLETON);
    }
}
