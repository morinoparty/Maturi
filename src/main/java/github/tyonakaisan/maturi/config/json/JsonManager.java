package github.tyonakaisan.maturi.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import github.tyonakaisan.maturi.config.json.adapter.DurationAdapter;
import github.tyonakaisan.maturi.config.json.adapter.InstantAdapter;
import github.tyonakaisan.maturi.config.json.adapter.LocationAdapter;
import github.tyonakaisan.maturi.config.json.adapter.UUIDAdapter;
import github.tyonakaisan.maturi.game.syateki.data.SyatekiPersonalData;
import github.tyonakaisan.maturi.game.syateki.data.SyatekiResult;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DefaultQualifier(NonNull.class)
public final class JsonManager {

    private final Path resultDirectory;
    private final Path personalDirectory;
    private final ComponentLogger logger;
    private final Gson gson;

    @Inject
    public JsonManager(
            final Path dataDirectory,
            final ComponentLogger logger
    ) throws IOException {
        this.resultDirectory = dataDirectory.resolve("syateki-result");
        this.personalDirectory = dataDirectory.resolve("syateki-personal");
        this.logger = logger;

        Files.createDirectories(this.resultDirectory);
        Files.createDirectories(this.personalDirectory);

        this.gson = GsonComponentSerializer.gson()
                .populator()
                .apply(new GsonBuilder())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(UUID.class, new UUIDAdapter())
                .setPrettyPrinting()
                .create();
    }

    private Path syatekiResultFile(final SyatekiResult syatekiResult) {
        return this.resultDirectory.resolve(syatekiResult.syatekiData().uuid() + ".json");
    }

    private Path syatekiPersonalDataFile(final SyatekiPersonalData personalData) {
        return this.personalDirectory.resolve(personalData.gamer().uuid() + ".json");
    }

    public Set<SyatekiResult> loadResult() {
        return this.load(this.resultDirectory, SyatekiResult.class);
    }

    public void saveResult(final SyatekiResult syatekiResult) {
        final var resultFile = this.syatekiResultFile(syatekiResult);
        this.save(resultFile, syatekiResult);
    }

    public Set<SyatekiPersonalData> loadPersonalData() {
        return this.load(this.personalDirectory, SyatekiPersonalData.class);
    }

    public void savePersonalData(final SyatekiPersonalData personalData) {
        final var personalBestFile = this.syatekiPersonalDataFile(personalData);
        this.save(personalBestFile, personalData);
    }

    private <T> Set<T> load(final Path directory, final Class<T> clazz) {
        try (final Stream<Path> paths = Files.walk(directory)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .map(file -> {
                        try (final Reader reader = Files.newBufferedReader(file)) {
                            return this.gson.fromJson(reader, clazz);
                        } catch (IOException e) {
                            this.logger.error("Failed to load json.", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(final Path file, final Object object) {
        try {
            final var json = this.gson.toJson(object);

            if (json == null || json.isBlank()) {
                throw new IllegalStateException("No data to save - toJson returned null or blank.");
            }

            Files.writeString(this.makeDirectories(file), json);
        } catch (final IOException e) {
            this.logger.error("Exception while saving data: [%s]".formatted(object), e);
        }
    }

    private Path makeDirectories(final Path path) {
        final Path parent = path.getParent();
        if (parent != null && !Files.isDirectory(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (final IOException e) {
                this.logger.error("Failed to create parent directories for '%s'".formatted(parent.getFileName()), e);
            }
        }
        return path;
    }
}
