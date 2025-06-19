package party.morino.maturi.config.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.time.Duration;

@DefaultQualifier(NonNull.class)
public final class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter out, final Duration value) throws IOException {
        out.value(value.getSeconds());
    }

    @Override
    public Duration read(final JsonReader in) throws IOException {
        return Duration.ofSeconds(in.nextLong());
    }
}
