package github.tyonakaisan.maturi.config.primary;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class KakigooriSettings {

    private int prize = 100;
    private int amount = 10;
    private Location clerkLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
    private Location handleLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
    private Location iceLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
    private Location cupLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);
    private Location dropLocation = new Location(Bukkit.getWorld("world"), 0, 0, 0);

    public int prize() {
        return this.prize;
    }

    public int amount() {
        return this.amount;
    }

    public Location clerkLocation() {
        return this.clerkLocation;
    }

    public Location handleLocation() {
        return this.handleLocation;
    }

    public Location iceLocation() {
        return this.iceLocation;
    }

    public Location cupLocation() {
        return this.cupLocation;
    }

    public Location dropLocation() {
        return this.dropLocation;
    }
}
