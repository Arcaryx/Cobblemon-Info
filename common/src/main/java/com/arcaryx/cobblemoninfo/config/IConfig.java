package com.arcaryx.cobblemoninfo.config;

import com.arcaryx.cobblemoninfo.waila.TooltipType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IConfig {
    public boolean modifyPokemonTooltip();
    public List<Pair<TooltipType, ShowType>> getPokemonTooltips();
    public ShowType showHealerEnergy();
    public ShowType showApricornProgress();
}
