package github.tyonakaisan.maturi.config.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.util.UUID;

@DefaultQualifier(NonNull.class)
public class UUIDAdapter extends TypeAdapter<UUID> {

    public UUIDAdapter() {
    }

    @Override
    public void write(final JsonWriter out, final @Nullable UUID uuid) throws IOException {
        if (uuid != null) {
            out.value(uuid.toString());
        } else {
            out.value((String) null);
        }
    }

    @Override
    public UUID read(final JsonReader in) throws IOException {
        return UUID.fromString(in.nextString());
    }
}
