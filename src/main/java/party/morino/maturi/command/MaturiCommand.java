package party.morino.maturi.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public interface MaturiCommand {
    ArgumentBuilder<CommandSourceStack, ?> init();
}
