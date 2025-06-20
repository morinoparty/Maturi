package party.morino.maturi.config.primary;

import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import party.morino.maturi.game.syateki.Syateki;

import java.util.Map;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
@NullMarked
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
