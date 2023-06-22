package com.arcaryx.cobblemoninfo.forge;

import com.arcaryx.cobblemoninfo.config.IConfig;
import com.arcaryx.cobblemoninfo.config.ShowType;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeConfig implements IConfig {
    private final ForgeConfigSpec.BooleanValue modifyPokemonTooltip;
    private final ForgeConfigSpec.EnumValue<ShowType> showPokemonGender, showPokemonHealth, showPokemonTrainer, showPokemonFriendship,
            showPokemonTypes, showPokemonRewardEvs, showPokemonNature, showPokemonAbility, showPokemonIvs, showPokemonEvs, showPokemonDexEntry;
    private final ForgeConfigSpec.EnumValue<ShowType> showHealerEnergy, showApricornProgress;
    //private final ForgeConfigSpec.BooleanValue hideNonPokemonDrops;

    public ForgeConfig(ForgeConfigSpec.Builder builder) {
        builder.push("jade");
        modifyPokemonTooltip = builder
                .comment("Modify the Pokemon tooltip.")
                .define("modifyPokemonTooltip", true);
        builder.push("pokemon");
        showPokemonGender = builder.defineEnum("showPokemonGender", ShowType.SHOW);
        showPokemonHealth = builder.defineEnum("showPokemonHealth", ShowType.SHOW);
        showPokemonTrainer = builder.defineEnum("showPokemonTrainer", ShowType.SHOW);
        showPokemonFriendship = builder.defineEnum("showPokemonFriendship", ShowType.SHOW);
        showPokemonTypes = builder.defineEnum("showPokemonTypes", ShowType.SHOW);
        showPokemonRewardEvs = builder.defineEnum("showPokemonRewardEvs", ShowType.SHOW);
        showPokemonNature = builder.defineEnum("showPokemonNature", ShowType.SHOW);
        showPokemonAbility = builder.defineEnum("showPokemonAbility", ShowType.SHOW);
        showPokemonIvs = builder.defineEnum("showPokemonIvs", ShowType.SHOW);
        showPokemonEvs = builder.defineEnum("showPokemonEvs", ShowType.SHOW);
        showPokemonDexEntry =  builder.defineEnum("showPokemonDexEntry", ShowType.SNEAK);
        builder.pop().push("misc");
        showHealerEnergy = builder.defineEnum("showHealerEnergy", ShowType.SHOW);
        showApricornProgress = builder.defineEnum("showApricornProgress", ShowType.SHOW);
        /*builder.pop().pop().push("jer").push("pokemon");
        hideNonPokemonDrops = builder
                .comment("Hide non-Pokemon drops from the JER catalog.")
                .define("hideNonPokemonDrops", false);
        builder.pop().pop();*/
    }


    @Override
    public boolean modifyPokemonTooltip() {
        return modifyPokemonTooltip.get();
    }

    @Override
    public ShowType showPokemonGender() {
        return showPokemonGender.get();
    }

    @Override
    public ShowType showPokemonHealth() {
        return showPokemonHealth.get();
    }

    @Override
    public ShowType showPokemonTrainer() {
        return showPokemonTrainer.get();
    }

    @Override
    public ShowType showPokemonFriendship() {
        return showPokemonFriendship.get();
    }

    @Override
    public ShowType showPokemonTypes() {
        return showPokemonTypes.get();
    }

    @Override
    public ShowType showPokemonRewardEvs() {
        return showPokemonRewardEvs.get();
    }

    @Override
    public ShowType showPokemonNature() {
        return showPokemonNature.get();
    }

    @Override
    public ShowType showPokemonAbility() {
        return showPokemonAbility.get();
    }

    @Override
    public ShowType showPokemonIvs() {
        return showPokemonIvs.get();
    }

    @Override
    public ShowType showPokemonEvs() {
        return showPokemonEvs.get();
    }

    @Override
    public ShowType showPokemonDexEntry() {
        return showPokemonDexEntry.get();
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
