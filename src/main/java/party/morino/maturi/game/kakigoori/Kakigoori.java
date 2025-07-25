package party.morino.maturi.game.kakigoori;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Spellcaster;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import party.morino.maturi.MaturiProvider;
import party.morino.maturi.game.kakigoori.data.KakigooriData;

@NullMarked
public final class Kakigoori {

    private final KakigooriData kakigooriData;
    private final Matrix4f mat;

    private boolean enabled = false;
    private @Nullable BukkitTask tickTask;
    private @Nullable BukkitTask nextPhaseTask;

    public Kakigoori(final KakigooriData kakigooriData) {
        this.kakigooriData = kakigooriData;
        this.mat = new Matrix4f().scale(0.65f, 0.5f, 0.65f);
    }

    public boolean enabled() {
        return this.enabled;
    }

    public Kakigoori start() {
        if (this.enabled) {
            return this;
        }

        this.enabled = true;
        this.create();
        return this;
    }

    // 正面180
    // 135
    private void create() {
        final var clerk = this.kakigooriData.clerk();
        final var cup = this.kakigooriData.display().cup();
        final var ice = this.kakigooriData.display().ice();
        ice.setItemStack(ItemStack.of(Material.ICE));
        clerk.setRotation(120, -10);
        cup.setItemStack(ShavedIce.make(ShavedIce.Type.CUP));
        this.playSound(Sound.sound(Key.key("minecraft:entity.evoker.ambient"), Sound.Source.MASTER, 0.2f, 1.75f), this.kakigooriData.clerkLocation());
        this.playSound(Sound.sound(Key.key("minecraft:block.iron_door.open"), Sound.Source.MASTER, 0.5f, 1.5f), this.kakigooriData.display().iceLocation());

        this.tickTask = Bukkit.getScheduler().runTaskTimer(MaturiProvider.instance(), this::rotation, 1, this.kakigooriData.display().duration());
        this.nextPhaseTask = Bukkit.getScheduler().runTaskLater(MaturiProvider.instance(), this::createDone, 140);
    }

    private void rotation() {
        final var ice = this.kakigooriData.display().ice();
        final var handle = this.kakigooriData.display().handle();
        final var iceLoc = this.kakigooriData.display().iceLocation().clone();

        final var rotate = this.mat.rotateY(((float) Math.toRadians(180)) + 0.2F);
        final var duration = this.kakigooriData.display().duration();
        ice.setTransformationMatrix(rotate);
        ice.setInterpolationDelay(0);
        ice.setInterpolationDuration(duration);
        handle.setTransformationMatrix(rotate);
        handle.setInterpolationDelay(0);
        handle.setInterpolationDuration(duration);

        new ParticleBuilder(Particle.BLOCK)
                .data(Material.ICE.createBlockData())
                .location(iceLoc)
                .offset(0.15, 0.15, 0.15)
                .count(5)
                .spawn();
        new ParticleBuilder(Particle.FALLING_DUST)
                .data(Material.POWDER_SNOW.createBlockData())
                .location(iceLoc.subtract(0.0, 0.75, 0.0))
                .offset(0.05, 0.0, 0.05)
                .count(2)
                .extra(0)
                .spawn();
        this.playSound(Sound.sound(Key.key("minecraft:block.sand.break"), Sound.Source.MASTER, 0.2f, 0.75f), iceLoc);
    }

    private void createDone() {
        final var cup = this.kakigooriData.display().cup();
        final var cupLoc = this.kakigooriData.display().cupLocation();
        cup.setItemStack(ShavedIce.make(ShavedIce.Type.FLAVORLESS));
        this.playSound(Sound.sound(Key.key("minecraft:block.fire.extinguish"), Sound.Source.MASTER, 0.1f, 1f), cupLoc);

        if (this.tickTask != null) {
            this.tickTask.cancel();
            this.tickTask = null;
        }

        this.nextPhaseTask = Bukkit.getScheduler().runTaskLater(MaturiProvider.instance(), this::take, 30);
    }

    private void take() {
        final var clerk = this.kakigooriData.clerk();
        final var cup = this.kakigooriData.display().cup();
        final var drop = this.kakigooriData.display().drop();
        drop.setItemStack(ShavedIce.make(ShavedIce.Type.FLAVORLESS));
        clerk.setRotation(180, 0);
        cup.setItemStack(ItemStack.empty());
        this.playSound(Sound.sound(Key.key("minecraft:entity.item.pickup"), Sound.Source.MASTER, 0.2f, 1f), clerk.getLocation());

        this.nextPhaseTask = Bukkit.getScheduler().runTaskLater(MaturiProvider.instance(), this::performanceStart, 30);
    }

    private void performanceStart() {
        final var clerk = this.kakigooriData.clerk();
        clerk.getEquipment().setItem(EquipmentSlot.HAND, ItemStack.of(this.kakigooriData.shavedIceData().mainMaterial()));
        clerk.getEquipment().setItem(EquipmentSlot.OFF_HAND, ItemStack.of(this.kakigooriData.shavedIceData().subMaterial()));
        clerk.setSpell(Spellcaster.Spell.SUMMON_VEX);

        this.tickTask = Bukkit.getScheduler().runTaskTimer(MaturiProvider.instance(), this::performanceParticle, 8, 8);
        this.nextPhaseTask = Bukkit.getScheduler().runTaskLater(MaturiProvider.instance(), this::performanceEnd, 80);
    }

    private void performanceParticle() {
        final var location = this.kakigooriData.display().dropLocation().clone().add(0, 0.35, 0);
        new ParticleBuilder(Particle.FALLING_DUST)
                .data(this.kakigooriData.shavedIceData().mainMaterial().createBlockData())
                .location(location)
                .offset(0.05, 0.2, 0.05)
                .count(2)
                .spawn();
        new ParticleBuilder(Particle.FALLING_DUST)
                .data(this.kakigooriData.shavedIceData().subMaterial().createBlockData())
                .location(location)
                .offset(0.05, 0.2, 0.05)
                .count(2)
                .spawn();
        this.playSound(Sound.sound(Key.key("ambient.underwater.exit"), Sound.Source.MASTER, 0.2f, 2f), location);
    }

    private void performanceEnd() {
        final var clerk = this.kakigooriData.clerk();
        final var drop = this.kakigooriData.display().drop();
        clerk.getEquipment().setItem(EquipmentSlot.HAND, ItemStack.empty());
        clerk.getEquipment().setItem(EquipmentSlot.OFF_HAND, ItemStack.empty());
        clerk.setSpell(Spellcaster.Spell.NONE);
        drop.setItemStack(ShavedIce.make(this.kakigooriData.shavedIceData().type()));
        this.playSound(Sound.sound(Key.key("minecraft:entity.evoker.ambient"), Sound.Source.MASTER, 0.2f, 1.75f), this.kakigooriData.clerkLocation());

        if (this.tickTask != null) {
            this.tickTask.cancel();
            this.tickTask = null;
        }

        this.nextPhaseTask = Bukkit.getScheduler().runTaskLater(MaturiProvider.instance(), this::drop, 30);
    }

    private void drop() {
        final var drop = this.kakigooriData.display().drop();
        final var location = this.kakigooriData.display().dropLocation().clone();
        final var itemStack = ShavedIce.make(this.kakigooriData.shavedIceData().type());
        itemStack.setAmount(this.kakigooriData.shavedIceData().amount());
        drop.setItemStack(ItemStack.empty());
        location.getWorld().dropItem(location, itemStack, item -> {
            item.setVelocity(new Vector(0, 0.225, 0));
            item.setOwner(this.kakigooriData.purchaser().uuid());
            item.setPickupDelay(40);
        });

        new ParticleBuilder(Particle.FIREWORK)
                .location(location.add(0, 0.25, 0))
                .count(8)
                .extra(0.05)
                .spawn();
        this.playSound(Sound.sound(Key.key("minecraft:entity.evoker.celebrate"), Sound.Source.MASTER, 0.2f, 1.75f), this.kakigooriData.clerkLocation());
        this.playSound(Sound.sound(Key.key("minecraft:entity.player.levelup"), Sound.Source.MASTER, 0.1f, 2f), location);

        this.nextPhaseTask = Bukkit.getScheduler().runTaskLater(MaturiProvider.instance(), this::end, 30);
    }

    private void end() {
        if (!this.enabled) {
            return;
        }

        if (this.nextPhaseTask != null) {
            this.nextPhaseTask.cancel();
            this.nextPhaseTask = null;
        }

        if (this.tickTask != null) {
            this.tickTask.cancel();
            this.tickTask = null;
        }

        this.reset();
        this.enabled = false;
    }

    // remove
    private void reset() {
        this.kakigooriData.clerk().remove();
        this.kakigooriData.display().handle().remove();
        this.kakigooriData.display().ice().remove();
        this.kakigooriData.display().cup().remove();
        this.kakigooriData.display().drop().remove();

        new ParticleBuilder(Particle.POOF)
                .location(this.kakigooriData.clerkLocation().clone().add(0, 1, 0))
                .offset(0.3, 0.6, 0.3)
                .count(10)
                .extra(0.1)
                .spawn();
    }

    private void playSound(final Sound sound, final Location location) {
        location.getWorld().playSound(sound, location.getX(), location.getY(), location.getZ());
    }
}
