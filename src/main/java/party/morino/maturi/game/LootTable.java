package party.morino.maturi.game;

import party.morino.maturi.util.Range;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

// lootTableという名のただのコマンド
// ランダム要素に対応してない
@ConfigSerializable
public record LootTable(
        Range<Integer> scoreRange,
        List<String> commands
) {
}
