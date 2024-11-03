package github.tyonakaisan.maturi.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("UnstableApiUsage")
public class EnumArgument<E extends Enum<E>> implements CustomArgumentType.Converted<E, String> {

    private final Class<E> enumClass;

    public EnumArgument(final Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public @NotNull E convert(final @NotNull String nativeType) throws CommandSyntaxException {
        try {
            return Enum.valueOf(this.enumClass, nativeType.toUpperCase());
        } catch (final Exception e) {
            final var message = MessageComponentSerializer.message().serialize(Component.text("Invalid species %s!".formatted(nativeType), NamedTextColor.RED));

            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder) {
        for (final var species : this.enumClass.getEnumConstants()) {
            builder.suggest(species.name().toLowerCase());
        }

        return CompletableFuture.completedFuture(
                builder.build()
        );
    }
}
