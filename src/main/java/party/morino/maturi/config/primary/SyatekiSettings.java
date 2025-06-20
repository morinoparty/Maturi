package party.morino.maturi.config.primary;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import party.morino.maturi.game.LootTable;
import party.morino.maturi.game.syateki.data.SyatekiArea;
import party.morino.maturi.util.Range;

import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
@NullMarked
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
