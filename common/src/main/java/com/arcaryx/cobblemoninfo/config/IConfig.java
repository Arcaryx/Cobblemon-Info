package com.arcaryx.cobblemoninfo.config;

import com.arcaryx.cobblemoninfo.waila.TooltipType;

import java.util.List;

public interface IConfig {
    public boolean modifyPokemonTooltip();
    public List<TooltipType> getPokemonShowTooltips();
    public List<TooltipType> getPokemonSneakTooltips();
    public ShowType showHealerEnergy();
    public ShowType showApricornProgress();
}
