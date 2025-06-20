package party.morino.maturi.config.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.time.Duration;

@NullMarked
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
