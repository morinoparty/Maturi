package github.tyonakaisan.example.listener;

import com.google.inject.Inject;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class ExampleListener implements Listener {

    @Inject
    public ExampleListener (

    ) {

    }

    @EventHandler
    public void onSwing(PlayerArmSwingEvent event) {
        var player = event.getPlayer();
        var loc = player.getLocation();

        loc.getWorld().getEntities().forEach(entity -> {
            if (entity instanceof LivingEntity entity1) {
                entity1.swingMainHand();
            }
        });
    }
}
