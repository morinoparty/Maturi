package party.morino.maturi.config.json.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.time.Instant;

@NullMarked
public final class InstantAdapter extends TypeAdapter<Instant> {

    @Override
    public void write(final JsonWriter out, final Instant value) throws IOException {
        out.value(value.toEpochMilli());
    }

    @Override
    public Instant read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NUMBER) {
            return Instant.ofEpochMilli(in.nextLong());
        }

        throw new UnsupportedOperationException("Unsupported format");
    }
}
