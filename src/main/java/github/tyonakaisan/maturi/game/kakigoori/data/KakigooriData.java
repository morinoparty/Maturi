package github.tyonakaisan.maturi.game.kakigoori.data;

import github.tyonakaisan.maturi.game.Gamer;
import github.tyonakaisan.maturi.game.kakigoori.ShavedIce;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.ItemDisplay;
import org.jetbrains.annotations.Range;

public record KakigooriData(
        Data data,
        Display display,
        Evoker clerk,
        Location clerkLocation,
        Gamer purchaser
) {
    public record Data(
            ShavedIce.Type type,
            Material mainMaterial,
            Material subMaterial,
            @Range(from = 0, to = 64)
            int amount
    ) {
    }

    public record Display(
            ItemDisplay handle,
            Location handleLocation,
            ItemDisplay ice, // BlockDisplayだと角を軸に回転を行うため
            Location iceLocation,
            ItemDisplay cup,
            Location cupLocation,
            ItemDisplay drop,
            Location dropLocation,
            int duration
    ) {
    }
}
