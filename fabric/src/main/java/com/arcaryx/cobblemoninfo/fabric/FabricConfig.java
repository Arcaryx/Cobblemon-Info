package com.arcaryx.cobblemoninfo.fabric;

import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.config.ShowType;
import com.arcaryx.cobblemoninfo.waila.TooltipType;

import java.util.List;

public class FabricConfig implements IConfig {
    @Override
    public boolean modifyPokemonTooltip() {
        return true;
    }

    @Override
    public List<TooltipType> getPokemonShowTooltips() {
        return TooltipType.defaultShow;
    }

    @Override
    public List<TooltipType> getPokemonSneakTooltips() {
        return TooltipType.defaultSneak;
    }

    @Override
    public ShowType showHealerEnergy() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showApricornProgress() {
        return ShowType.SHOW;
    }
}
