package github.tyonakaisan.maturi.game;

import github.tyonakaisan.maturi.game.syateki.gui.ReChestInterface;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.transform.PaperTransform;

@DefaultQualifier(NonNull.class)
public abstract class AbstractGui {

    protected abstract ReChestInterface.Builder buildInterface();

    protected static ItemStack filler() {
        final var itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        itemStack.editMeta(itemMeta -> {
            itemMeta.setHideTooltip(true);
            itemMeta.setCustomModelData(1);
        });
        return itemStack;
    }

    protected static Transform<ChestPane, PlayerViewer> chestItem(final ItemStackElement<ChestPane> element, final int x, final int y) {
        return PaperTransform.chestItem(() -> element, x, y);
    }
}
