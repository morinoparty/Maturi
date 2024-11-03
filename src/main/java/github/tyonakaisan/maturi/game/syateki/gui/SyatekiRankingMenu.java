package github.tyonakaisan.maturi.game.syateki.gui;

import com.google.inject.Inject;
import github.tyonakaisan.maturi.game.AbstractGui;
import github.tyonakaisan.maturi.game.syateki.Syateki;
import github.tyonakaisan.maturi.game.syateki.SyatekiManager;
import github.tyonakaisan.maturi.game.syateki.data.SyatekiResult;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.transform.PaperTransform;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class SyatekiRankingMenu extends AbstractGui {

    private final SyatekiManager syatekiManager;

    private final List<SyatekiResult> results = new ArrayList<>();

    @Inject
    public SyatekiRankingMenu(
            final SyatekiManager syatekiManager
    ) {
        this.syatekiManager = syatekiManager;
    }

    @Override
    protected ReChestInterface.Builder buildInterface() {
        return ReChestInterface.builder()
                .rows(4)
                .updates(true, 20)
                .clickHandler(ClickHandler.cancel())
                .addTransform(PaperTransform.chestFill(ItemStackElement.of(filler())));
    }

    public void open(final Player player, final Syateki.Difficulty difficulty) {
        this.results.clear();
        this.results.addAll(this.syatekiManager.personalDataSet().stream()
                .map(syatekiPersonalData -> syatekiPersonalData.bests(difficulty))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(SyatekiResult::point).reversed())
                .limit(10)
                .toList());

        final var interfaceBuilder = this.buildInterface()
                .title(Component.text("Ranking"));

        final int[] position = {0};
        final int[] column = {1};
        this.results.stream()
                .map(result -> {
                    position[0]++;
                    if (position[0] > 5) {
                        position[0] = 1;
                        column[0]++;
                    }
                    return chestItem(ItemStackElement.of(this.playerHead(result, position[0], column[0] - 1)), position[0], column[0]);
                })
                .forEach(interfaceBuilder::addTransform);

        interfaceBuilder.build()
                .open(PlayerViewer.of(player));
    }

    private ItemStack playerHead(final @Nullable SyatekiResult result, final int position, final int column) {
        if (result == null) {
            return new ItemStack(Material.BARRIER);
        }

        final var opt = result.syatekiData().gamers().get(0);
        final var player = opt.online() ? opt.player().get() : opt.offlinePlayer();
        final var itemStack = new ItemStack(Material.PLAYER_HEAD);

        final var profile = player.getPlayerProfile();
        itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final SkullMeta skullMeta) {
                skullMeta.setPlayerProfile(profile);
            }
            itemMeta.displayName(MiniMessage.miniMessage().deserialize("<!italic><gold>#<rank> <point>pt",
                    TagResolver.builder()
                            .tag("rank", Tag.inserting(Component.text(position + column * 5)))
                            .tag("point", Tag.inserting(Component.text(result.point())))
                            .build()));
        });
        return itemStack;
    }
}
