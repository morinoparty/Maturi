package party.morino.maturi.compat;

import com.google.inject.Singleton;
import party.morino.maturi.Maturi;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
@Singleton
public final class EconomyProvider {

    private static @Nullable Economy economy = null;

    private EconomyProvider() {
    }

    public static void init() {
        if (!Maturi.isVaultLoaded()) {
            return;
        }

        final @Nullable RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }

        EconomyProvider.economy = rsp.getProvider();
    }

    public static Economy economy() {
        if (economy == null) {
            throw new IllegalStateException("Economy not found.");
        }

        return economy;
    }
}
