package party.morino.maturi.message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslationStore;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"UnstableApiUsage", "PatternValidation"})
@NullMarked
@Singleton
public final class Messages {

    private final ComponentLogger logger;
    private final Path messagesDir;

    private final Map<Locale, ResourceBundle> locales = new HashMap<>();
    private final Map<Locale, String> supportedLocales = Map.of(
            Locale.JAPAN, "messages_ja_JP",
            Locale.US, "messages_en_US"
    );
    private final Pattern pattern = Pattern.compile("messages_(.+)\\.properties");
    private static final String PREFIX = "<white>[<gradient:#005aa7:#fffde4>Maturi</gradient>]</white>";

    private final Key registryKey;
    private MiniMessageTranslationStore translationRegistry;

    @Inject
    public Messages(
            final Path dataDirectory,
            final ComponentLogger logger,
            final PluginMeta pluginMeta
    ) {
        this.logger = logger;
        this.messagesDir = dataDirectory.resolve("locale");
        this.registryKey = Key.key(pluginMeta.getName().toLowerCase(), "translation");
        this.translationRegistry = MiniMessageTranslationStore.create(this.registryKey);

        this.reloadMessage();
    }

    public void reloadMessage() {
        this.locales.clear();
        this.logger.info("Reloading locales...");
        this.loadMessageFile();
    }

    private void loadMessageFile() {
        // Create registry
        GlobalTranslator.translator().removeSource(this.translationRegistry);
        this.translationRegistry = MiniMessageTranslationStore.create(this.registryKey);
        this.translationRegistry.defaultLocale(Locale.US);

        if (!Files.exists(this.messagesDir)) {
            try {
                Files.createDirectories(this.messagesDir);
            } catch (final IOException e) {
                this.logger.error(String.format("Failed to create directory %s", this.messagesDir), e);
            }
        }

        // Create supported locales
        this.createSupportedLocales(this.messagesDir);

        // Load messages_*.properties locale
        try (final Stream<Path> paths = Files.list(this.messagesDir)) {
            paths.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".properties"))
                    .forEach(this::loadMatchFile);
        } catch (final IOException e) {
            this.logger.error("Failed to load locales.", e);
        }

        // Register translator
        GlobalTranslator.translator().addSource(this.translationRegistry);

        this.logger.info("Successfully {} locales loaded! {}", this.locales.keySet().size(), this.locales.keySet());
    }

    private void createSupportedLocales(final Path path) {
        for (final Map.Entry<Locale, String> localesEntry : this.supportedLocales.entrySet()) {
            final var locale = localesEntry.getKey();
            final var fileName = localesEntry.getValue();
            final var localePath = path.resolve(fileName + ".properties");

            if (!Files.exists(localePath)) {
                final var bundle = ResourceBundle.getBundle("locale." + fileName, locale, UTF8ResourceBundleControl.get());
                this.createProperties(localePath, bundle);
            }
        }
    }

    private void createProperties(final Path path, final ResourceBundle bundle) {
        final var properties = new Properties() {
            @Override
            public synchronized @NotNull Set<Map.Entry<Object, Object>> entrySet() {
                return Collections.unmodifiableSet(
                        (Set<? extends Map.Entry<Object, Object>>) super.entrySet()
                                .stream()
                                .sorted(Comparator.comparing(entry -> entry.getKey().toString()))
                                .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
        };

        try (final Writer writer = Files.newBufferedWriter(path)) {
            properties.putAll(bundle.keySet().stream()
                    .collect(Collectors.toMap(key1 -> key1, bundle::getString)));
            properties.store(writer, null);
            this.logger.info("Successfully '{}' created!", path.getFileName());
        } catch (final IOException e) {
            this.logger.error("Failed to create '{}'", path.getFileName(), e);
        }
    }

    private void loadMatchFile(final Path path) {
        final var matcher = this.pattern.matcher(path.getFileName().toString());
        if (matcher.matches()) {
            final @Nullable Locale locale = Translator.parseLocale(matcher.group(1));

            if (locale == null) {
                this.logger.warn("Invalid locale file: {}", path.getFileName());
            } else {
                this.load(locale, path);
            }
        }
    }

    private void load(final Locale locale, final Path path) {
        try (final BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            final var bundle = new PropertyResourceBundle(reader);
            this.locales.put(locale, bundle);
            this.translationRegistry.registerAll(locale, bundle, false);
        } catch (final Exception e) {
            this.logger.error(String.format("Failed to load %s", path.getFileName()), e);
        }
    }
}
