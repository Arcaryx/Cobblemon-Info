package com.arcaryx.cobblemoninfo.forge;

import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.config.ShowType;
import com.arcaryx.cobblemoninfo.waila.TooltipType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;
import java.util.List;

public class ForgeConfig implements IConfig {
    private final ForgeConfigSpec.BooleanValue modifyPokemonTooltip;
    private final ForgeConfigSpec.ConfigValue<List<? extends TooltipType>> pokemonTooltipsShow, pokemonTooltipsSneak;
    private final ForgeConfigSpec.EnumValue<ShowType> showHealerEnergy, showApricornProgress;

    public ForgeConfig(ForgeConfigSpec.Builder builder) {
        builder.push("waila");
        modifyPokemonTooltip = builder
                .comment("Modify the Pokemon tooltip.")
                .define("modifyPokemonTooltip", true);
        builder.push("pokemon");
        pokemonTooltipsShow = builder.defineList("pokemonTooltipsShow", TooltipType.defaultShow,
                x -> TooltipType.check((String)x));
        pokemonTooltipsSneak = builder.defineList("pokemonTooltipsSneak", TooltipType.defaultSneak,
                x -> TooltipType.check((String)x));
        builder.pop().push("misc");
        showHealerEnergy = builder.defineEnum("showHealerEnergy", ShowType.SHOW);
        showApricornProgress = builder.defineEnum("showApricornProgress", ShowType.SHOW);
    }

    @Override
    public boolean modifyPokemonTooltip() {
        return modifyPokemonTooltip.get();
    }

    @Override
    public List<TooltipType> getPokemonShowTooltips() {
        return Collections.unmodifiableList(pokemonTooltipsShow.get());
    }

    @Override
    public List<TooltipType> getPokemonSneakTooltips() {
        return Collections.unmodifiableList(pokemonTooltipsSneak.get());
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