package party.morino.maturi.listener;

import com.google.inject.Inject;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.config.ConfigFactory;

@NullMarked
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
