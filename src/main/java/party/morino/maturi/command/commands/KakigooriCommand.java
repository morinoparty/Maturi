package party.morino.maturi.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.ArgumentBuilder;
import party.morino.maturi.command.MaturiCommand;
import party.morino.maturi.command.arguments.EnumArgument;
import party.morino.maturi.game.kakigoori.KakigooriHandler;
import party.morino.maturi.game.kakigoori.ShavedIce;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class KakigooriCommand implements MaturiCommand {

    private final KakigooriHandler kakigooriHandler;

    @Inject
    public KakigooriCommand(
            final KakigooriHandler kakigooriHandler
    ) {
        this.kakigooriHandler = kakigooriHandler;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("kakigoori")
                .requires(resource -> resource.getSender().hasPermission("maturi.command.kakigoori"))
                .then(argument("type", new EnumArgument<>(ShavedIce.Type.class))
                        .then(argument("player", ArgumentTypes.player())
                                .executes(context -> {
                                    final var type = context.getArgument("type", ShavedIce.Type.class);
                                    final var player = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource()).get(0);
                                    this.kakigooriHandler.start(player, type);
                                    return 1;
                                })));
    }
}
