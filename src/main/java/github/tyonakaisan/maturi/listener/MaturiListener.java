package github.tyonakaisan.maturi.listener;

import com.google.inject.Inject;
import github.tyonakaisan.maturi.config.ConfigFactory;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class MaturiListener implements Listener {

    private final ConfigFactory configFactory;

    @Inject
    public MaturiListener(
            final ConfigFactory configFactory
    ) {
        this.configFactory = configFactory;
    }

    @EventHandler
    public void onSpawn(final CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        if (event.getEntity() instanceof Monster && !this.configFactory.primaryConfig().monsterSpawning()) {
            event.setCancelled(true);
        }
    }
}
