package party.morino.maturi.config.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public final class LocationAdapter extends TypeAdapter<Location> {

    private static final String WORLD = "world";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";

    @Override
    public void write(final JsonWriter out, final @Nullable Location location) throws IOException {
        if (location == null || location.getWorld() == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name(WORLD).value(location.getWorld().getName());
        out.name(X).value(location.getX());
        out.name(Y).value(location.getY());
        out.name(Z).value(location.getZ());
        out.endObject();
    }

    @Override
    public @Nullable Location read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.beginObject();
        var worldName = "";
        var x = 0.0;
        var y = 0.0;
        var z = 0.0;
        while (in.hasNext()) {
            switch (in.nextName()) {
                case WORLD -> worldName = in.nextString();
                case X -> x = in.nextDouble();
                case Y -> y = in.nextDouble();
                case Z -> z = in.nextDouble();
                default -> in.skipValue();
            }
        }
        final @Nullable World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException("Unknown world: %s".formatted(worldName));
        }
        in.endObject();
        return new Location(world, x, y, z);
    }
}
