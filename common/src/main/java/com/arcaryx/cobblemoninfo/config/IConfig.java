package com.arcaryx.cobblemoninfo.config;

import com.arcaryx.cobblemoninfo.waila.TooltipType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IConfig {
    boolean modifyPokemonTooltip();
    boolean hidePokemonLabel();
    List<Pair<TooltipType, ShowType>> getPokemonTooltips();
    ShowType showHealerEnergy();
    ShowType showApricornProgress();
}
