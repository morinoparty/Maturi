package github.tyonakaisan.maturi;

import com.google.inject.*;
import github.tyonakaisan.maturi.command.CommandFactory;
import github.tyonakaisan.maturi.integration.EconomyProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.paper.PaperInterfaceListeners;

import java.util.Set;

@DefaultQualifier(NonNull.class)
@Singleton
public final class Maturi extends JavaPlugin {

    private final Injector injector;

    @Inject
    public Maturi(
            final Injector bootstrapInjector
    ) {
        this.injector = bootstrapInjector.createChildInjector(new MaturiModule(this));

        MaturiProvider.register(this);
    }

    @Override
    public void onEnable() {
        PaperInterfaceListeners.install(this);
        EconomyProvider.init();

        final Set<Listener> listeners = this.injector.getInstance(Key.get(new TypeLiteral<>() {}));
        listeners.forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        this.injector.getInstance(CommandFactory.class).registerViaEnable(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static boolean isVaultLoaded() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Vault");
    }
}
