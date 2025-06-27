package party.morino.maturi.game;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a gamer with a unique UUID.
 *
 * @param uuid the unique identifier for the gamer
 */
@NullMarked
public record Gamer(UUID uuid, Component displayName) implements Audience, ForwardingAudience.Single {

    /**
     * Get the player associated with this gamer.
     *
     * @return an {@link Optional} containing the {@link Player} if they are currently online,
     * or an empty optional if the player is offline or not found
     */
    public Optional<Player> player() {
        return Optional.ofNullable(Bukkit.getPlayer(this.uuid));
    }

    public OfflinePlayer offlinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    /**
     * Check if the online.
     *
     * @return if the online
     */
    public boolean online() {
        return this.player().isPresent();
    }

    /**
     * Check if the inventory is empty.
     *
     * @return if the inventory is empty
     * Returns false if {@link Gamer#player()} is empty
     */
    public boolean isInventoryEmpty() {
        return this.player()
                .map(Player::getInventory)
                .map(Inventory::isEmpty)
                .orElse(false);
    }

    @Override
    public @NotNull Audience audience() {
        return this.player()
                .map(Audience.class::cast)
                .orElseGet(Audience::empty);
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return this.uuid.equals(((Gamer) obj).uuid);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
