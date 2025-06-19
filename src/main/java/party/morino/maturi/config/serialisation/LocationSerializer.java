package party.morino.maturi.config.serialisation;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

@DefaultQualifier(NonNull.class)
public final class LocationSerializer implements TypeSerializer<Location> {

    private static final String WORLD = "world";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String PITCH = "pitch";
    private static final String YAW = "yaw";

    @Inject
    public LocationSerializer() {}

    @Override
    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        final var worldName = node.node(WORLD).getString("");
        final @Nullable World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new SerializationException("Unknown world: %s".formatted(worldName));
        }

        final var x = node.node(X).getDouble();
        final var y = node.node(Y).getDouble();
        final var z = node.node(Z).getDouble();

        return new Location(world, x, y, z);
    }

    @Override
    public void serialize(Type type, @Nullable Location obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            final var world = obj.getWorld().getName();
            final var x = obj.getX();
            final var y = obj.getY();
            final var z = obj.getZ();

            node.node(WORLD).set(world);
            node.node(X).set(x);
            node.node(Y).set(y);
            node.node(Z).set(z);
        }
    }
}
