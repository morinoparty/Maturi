package party.morino.maturi.game.kakigoori;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import party.morino.maturi.config.ConfigFactory;
import party.morino.maturi.game.Gamer;
import party.morino.maturi.game.kakigoori.data.KakigooriData;
import party.morino.maturi.message.Messages;
import party.morino.maturi.util.MoneyUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.joml.Matrix4f;

import java.util.concurrent.ThreadLocalRandom;

@DefaultQualifier(NonNull.class)
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

        final var purchaser = new Gamer(player.getUniqueId());
        final var settings = this.configFactory.primaryConfig().kakigoori();
        final var prize = settings.prize();
        if (!MoneyUtils.hasEnoughMoney(purchaser, 100)) {
            purchaser.sendMessage(Messages.translate("maturi.not_enough_money", purchaser));
            return;
        }

        MoneyUtils.takeMoney(purchaser, prize);

        final var clerkLoc = settings.clerkLocation().clone();
        final var handleLoc = settings.handleLocation().clone();
        final var iceLoc = settings.iceLocation().clone();
        final var cupLoc = settings.cupLocation().clone();
        final var dropLoc = settings.dropLocation().clone();

        final var clerk = this.clerk(clerkLoc);
        final var ice = this.itemDisplay(iceLoc, new ItemStack(Material.ICE), new Matrix4f().scale(0.65f, 0.5f, 0.65f));
        final var handle = this.itemDisplay(handleLoc, new ItemStack(Material.IRON_TRAPDOOR), new Matrix4f().scale(0.65f, 0.5f, 0.65f));
        final var cup = this.itemDisplay(cupLoc, ItemStack.empty(), new Matrix4f().scale(0.5f, 0.5f, 0.5f));
        final var drop = this.itemDisplay(dropLoc, ItemStack.empty(), new Matrix4f().scale(0.5f, 0.5f, 0.5f));

        final var a = new KakigooriData.Data(type, ShavedIce.mainMaterial(type), ShavedIce.subMaterial(type), settings.amount());
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
        clerk.setLootTable(LootTables.VILLAGER.getLootTable());
        clerk.getEquipment().setItem(EquipmentSlot.HEAD, this.hat());
        clerk.setInvulnerable(true);
        return clerk;
    }

    private ItemStack hat() {
        final var itemStack = new ItemStack(Material.FEATHER);
        final var omen = ThreadLocalRandom.current().nextBoolean(); // hyottoko or okame
        itemStack.editMeta(itemMeta -> itemMeta.setCustomModelData(omen ? 17 : 19));
        return itemStack;
    }
}
