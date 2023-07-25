package com.arcaryx.cobblemoninfo.forge;

import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.config.ShowType;
import com.arcaryx.cobblemoninfo.waila.TooltipType;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ForgeConfig implements IConfig {
    private final ForgeConfigSpec.BooleanValue modifyPokemonTooltip, hidePokemonLabel;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> pokemonTooltips;
    private final ForgeConfigSpec.EnumValue<ShowType> showHealerEnergy, showApricornProgress;

    public ForgeConfig(ForgeConfigSpec.Builder builder) {
        builder.push("waila");
        modifyPokemonTooltip = builder
                .comment("Modify the Pokemon tooltip.")
                .define("modifyPokemonTooltip", true);
        hidePokemonLabel = builder
                .comment("Hide the Pokemon label/nametag completely.")
                .define("hidePokemonLabel", false);
        builder.push("pokemon");
        pokemonTooltips = builder.defineList("pokemonTooltips", TooltipType.pokemonDefaults.stream().map(x -> x.getLeft().name() + ":" + x.getRight().name()).toList(), TooltipType::check);
        builder.pop().push("misc");
        showHealerEnergy = builder.defineEnum("showHealerEnergy", ShowType.SHOW);
        showApricornProgress = builder.defineEnum("showApricornProgress", ShowType.SHOW);
    }

    @Override
    public boolean modifyPokemonTooltip() {
        return modifyPokemonTooltip.get();
    }

    @Override
    public boolean hidePokemonLabel() {
        return hidePokemonLabel.get();
    }

    @Override
    public List<Pair<TooltipType, ShowType>> getPokemonTooltips() {
        return pokemonTooltips.get().stream().map(x -> {
            var str = x.split(":");
            return Pair.of(TooltipType.valueOf(str[0]), ShowType.valueOf(str[1]));
        }).toList();
    }

    @Override
    public ShowType showHealerEnergy() {
        return showHealerEnergy.get();
    }

    @Override
    public ShowType showApricornProgress() {
        return showApricornProgress.get();
    }
}