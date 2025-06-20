package party.morino.maturi.game;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.transform.PaperTransform;
import org.jspecify.annotations.NullMarked;
import party.morino.maturi.game.syateki.gui.ReChestInterface;

@NullMarked
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
