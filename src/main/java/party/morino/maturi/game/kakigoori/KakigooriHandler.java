package party.morino.maturi.game.kakigoori;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import party.morino.maturi.config.ConfigFactory;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.kakigoori.data.KakigooriData;
import party.morino.maturi.util.MoneyUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@NullMarked
@Singleton
public final class KakigooriHandler {

    private final ConfigFactory configFactory;

    private @Nullable Kakigoori kakigoori = null;

    @Inject
    public KakigooriHandler(
            final ConfigFactory configFactory
    ) {
        this.configFactory = configFactory;
    }

    public void start(final Player player, final ShavedIce.Type type) {
        if (this.kakigoori != null && this.kakigoori.enabled()) {
            return;
        }

        final var purchaser = new Gamer(player.getUniqueId(), player.displayName());
        final var settings = this.configFactory.primaryConfig().kakigoori();
        final var prize = settings.prize();
        if (!MoneyUtils.hasEnoughMoney(purchaser, 100)) {
            purchaser.sendMessage(Component.translatable("maturi.not_enough_money"));
            return;
        }

        MoneyUtils.takeMoney(purchaser, prize);

        final var clerkLoc = settings.clerkLocation().clone();
        final var handleLoc = settings.handleLocation().clone();
        final var iceLoc = settings.iceLocation().clone();
        final var cupLoc = settings.cupLocation().clone();
        final var dropLoc = settings.dropLocation().clone();

        final var clerk = this.clerk(clerkLoc);
        final var ice = this.itemDisplay(iceLoc, ItemStack.of(Material.ICE), new Matrix4f().scale(0.65f, 0.5f, 0.65f));
        final var handle = this.itemDisplay(handleLoc, ItemStack.of(Material.IRON_TRAPDOOR), new Matrix4f().scale(0.65f, 0.5f, 0.65f));
        final var cup = this.itemDisplay(cupLoc, ItemStack.empty(), new Matrix4f().scale(0.5f, 0.5f, 0.5f));
        final var drop = this.itemDisplay(dropLoc, ItemStack.empty(), new Matrix4f().scale(0.5f, 0.5f, 0.5f));

        final var a = new KakigooriData.ShavedIceData(type, ShavedIce.mainMaterial(type), ShavedIce.subMaterial(type), settings.amount());
        final var b = new KakigooriData.Display(handle, handleLoc, ice, iceLoc, cup, cupLoc, drop, dropLoc, 5);
        final var c = new KakigooriData(a, b, clerk, clerkLoc, purchaser);

        this.kakigoori = new Kakigoori(c);
        this.kakigoori.start();
    }

    private ItemDisplay itemDisplay(final Location location, final ItemStack itemStack, final Matrix4f matrix4f) {
        final var itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);

        itemDisplay.setItemStack(itemStack);
        itemDisplay.setTransformationMatrix(matrix4f);

        return itemDisplay;
    }

    private Evoker clerk(final Location location) {
        final var clerk = (Evoker) location.getWorld().spawnEntity(location, EntityType.EVOKER);
        clerk.setRotation(180, 0);
        clerk.setAI(false);
        clerk.setSilent(true);
        clerk.setLootTable(new LootTable() {
            @Override
            public @NotNull Collection<ItemStack> populateLoot(final @Nullable Random random, final @NotNull LootContext context) {
                return Collections.emptyList();
            }

            @Override
            public void fillInventory(final @NotNull Inventory inventory, final @Nullable Random random, final @NotNull LootContext context) {
                // empty
            }

            @Override
            public @NotNull NamespacedKey getKey() {
                return new NamespacedKey("maturi", "empty_loot_table");
            }
        });
        clerk.getEquipment().setItem(EquipmentSlot.HEAD, this.hat());
        clerk.setInvulnerable(true);
        return clerk;
    }

    private ItemStack hat() {
        final var itemStack = ItemStack.of(Material.FEATHER);
        final var omen = ThreadLocalRandom.current().nextBoolean(); // hyottoko or okame
        itemStack.editMeta(itemMeta -> itemMeta.setCustomModelData(omen ? 17 : 19));
        return itemStack;
    }
}
