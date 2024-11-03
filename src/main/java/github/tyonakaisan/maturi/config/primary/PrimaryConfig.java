package github.tyonakaisan.maturi.config.primary;

import github.tyonakaisan.maturi.game.syateki.Syateki;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
@DefaultQualifier(NonNull.class)
public class PrimaryConfig {

    @Comment("""
            モンスターと部類されるmobが湧くかどうか
            モンスター以外のmobや、自然湧き以外の理由で生まれたモンスターは関係ありません""")
    private boolean monsterNaturalSpawning = true;
    @Comment("かき氷屋台の設定")
    private KakigooriSettings kakigoori = new KakigooriSettings();
    @Comment("難易度別の射的の設定")
    private Map<Syateki.Difficulty, SyatekiSettings> syateki = Map.of(Syateki.Difficulty.HARD, new SyatekiSettings());

    public boolean monsterSpawning() {
        return this.monsterNaturalSpawning;
    }

    public KakigooriSettings kakigoori() {
        return this.kakigoori;
    }

    public Map<Syateki.Difficulty, SyatekiSettings> syateki() {
        return this.syateki;
    }

    public SyatekiSettings syateki(final Syateki.Difficulty difficulty) {
        return this.syateki.get(difficulty);
    }

}
