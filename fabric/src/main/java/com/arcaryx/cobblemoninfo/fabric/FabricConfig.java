package com.arcaryx.cobblemoninfo.fabric;

import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.config.ShowType;

public class FabricConfig implements IConfig {
    @Override
    public boolean modifyPokemonTooltip() {
        return true;
    }

    @Override
    public ShowType showPokemonGender() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonHealth() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonTrainer() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonFriendship() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonTypes() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonRewardEvs() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonNature() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonAbility() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonIvs() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonEvs() {
        return ShowType.SHOW;
    }

    @Override
    public ShowType showPokemonDexEntry() {
        return ShowType.SNEAK;
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
