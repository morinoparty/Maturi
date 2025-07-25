package party.morino.maturi.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.command.MaturiCommand;
import party.morino.maturi.command.arguments.EnumArgument;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.syateki.Syateki;
import party.morino.maturi.game.syateki.SyatekiHandler;

import java.time.Duration;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public final class SyatekiCommand implements MaturiCommand {

    private final SyatekiHandler syatekiHandler;
    @Inject
    public SyatekiCommand(
            final SyatekiHandler syatekiHandler
    ) {
        this.syatekiHandler = syatekiHandler;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("syateki")
                .requires(source -> source.getSender().hasPermission("maturi.command.syateki"))
                .then(literal("start")
                        .then(argument("difficulty", new EnumArgument<>(Syateki.Difficulty.class))
                                .then(argument("players", ArgumentTypes.players())
                                        .then(argument("duration", LongArgumentType.longArg())
                                                .executes(context -> {
                                                    final var difficulty = context.getArgument("difficulty", Syateki.Difficulty.class);
                                                    final var gamers = context.getArgument("players", PlayerSelectorArgumentResolver.class).resolve(context.getSource())
                                                            .stream()
                                                            .map(player -> new Gamer(player.getUniqueId(), player.displayName()))
                                                            .toList();
                                                    final var duration = context.getArgument("duration", long.class);
                                                    this.syatekiHandler.start(difficulty, Duration.ofSeconds(duration), gamers);
                                                    return 1;
                                                })))));
    }
}
