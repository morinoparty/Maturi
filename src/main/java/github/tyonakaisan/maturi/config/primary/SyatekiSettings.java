package github.tyonakaisan.maturi.config.primary;

import github.tyonakaisan.maturi.game.syateki.data.SyatekiArea;
import github.tyonakaisan.maturi.game.LootTable;
import github.tyonakaisan.maturi.util.Range;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class SyatekiSettings {

    private int entranceFee = 0;
    private List<SyatekiArea> areas = List.of();
    private List<LootTable> lootTable = List.of(new LootTable(new Range<>(0, 0), List.of()));

    public int entranceFee() {
        return this.entranceFee;
    }

    public List<SyatekiArea> areas() {
        return this.areas;
    }

    public List<LootTable> lootTable() {
        return this.lootTable;
    }

}
