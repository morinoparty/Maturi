package party.morino.maturi.game.kakigoori;

import github.tyonakaisan.commanditem.CommandItemProvider;
import github.tyonakaisan.commanditem.item.Item;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// w
@NullMarked
public final class ShavedIce {

    private ShavedIce() {
    }

    public static ItemStack make(final Type type) {
        final var itemRegistry = CommandItemProvider.instance().itemRegistry();
        final @Nullable Item item = switch (type) {
            case CUP -> itemRegistry.item(Key.key("natumaturi:shaved_ice_cup"));
            case FLAVORLESS -> itemRegistry.item(Key.key("natumaturi:shaved_ice_flavorless"));
            case KAMIMORI -> itemRegistry.item(Key.key("natumaturi:shaved_ice_kamimori"));
            case URCHIN -> itemRegistry.item(Key.key("natumaturi:shaved_ice_urchin"));
            case STRAWBERRY -> itemRegistry.item(Key.key("natumaturi:shaved_ice_strawberry"));
            case MATCHA -> itemRegistry.item(Key.key("natumaturi:shaved_ice_matcha"));
            case SEIJU -> itemRegistry.item(Key.key("natumaturi:shaved_ice_seiju"));
        };

        if (item == null) {
            return new ItemStack(Material.STONE);
        }

        return item.asSimple();
    }

    public static Material mainMaterial(final Type type) {
        return switch (type) {
            case CUP, FLAVORLESS -> Material.AIR; // Unused
            case KAMIMORI -> Material.LIGHT_BLUE_CANDLE;
            case URCHIN -> Material.ORANGE_CANDLE;
            case STRAWBERRY -> Material.RED_CANDLE;
            case MATCHA -> Material.GREEN_CANDLE;
            case SEIJU -> Material.BLUE_CANDLE;
        };
    }

    public static Material subMaterial(final Type type) {
        return switch (type) {
            case CUP,FLAVORLESS -> Material.AIR; // Unused
            case KAMIMORI -> Material.LIGHT_BLUE_CANDLE;
            case URCHIN -> Material.BLACK_CANDLE;
            case STRAWBERRY -> Material.CANDLE;
            case MATCHA -> Material.BROWN_CANDLE;
            case SEIJU -> Material.YELLOW_CANDLE;
        };
    }

    /**
     * Shaved ice types.
     */
    public enum Type {
        /**
         * Shaved ice cup.
         */
        CUP,
        /**
         * Flavorless.
         */
        FLAVORLESS,
        /**
         * Also known as "かみもりかき氷".
         */
        KAMIMORI,
        /**
         * Also known as "ウニかき氷".
         */
        URCHIN,
        /**
         * Also known as "イチゴかき氷".
         */
        STRAWBERRY,
        /**
         * Also known as "宇治抹茶かき氷".
         */
        MATCHA,
        /**
         * Also known as "星降る森かき氷".
         */
        SEIJU,
    }
}
