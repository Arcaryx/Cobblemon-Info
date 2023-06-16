package com.arcaryx.cobblemoninfo.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    // Jade - Pokemon
    public enum ShowType {
        HIDE,
        SNEAK,
        SHOW
    }

    public final ForgeConfigSpec.BooleanValue modifyPokemonTooltip;
    public final ForgeConfigSpec.EnumValue<ShowType> showPokemonGender, showPokemonHealth, showPokemonTrainer, showpokemonFriendship,
            showPokemonTypes, showPokemonRewardEvs, showPokemonNature, showPokemonAbility, showPokemonIvs, showPokemonEvs, showPokemonDexEntry;
    public final ForgeConfigSpec.EnumValue<ShowType> showHealerEnergy;

    public CommonConfig(ForgeConfigSpec.Builder builder){
        builder.push("jade");
        modifyPokemonTooltip = builder
                .comment("Modify the Pokemon tooltip.")
                .define("modifyPokemonTooltip", true);
        builder.push("pokemon");
        showPokemonGender = builder.defineEnum("showPokemonGender", ShowType.SHOW);
        showPokemonHealth = builder.defineEnum("showPokemonHealth", ShowType.SHOW);
        showPokemonTrainer = builder.defineEnum("showPokemonTrainer", ShowType.SHOW);
        showpokemonFriendship = builder.defineEnum("showpokemonFriendship", ShowType.SHOW);
        showPokemonTypes = builder.defineEnum("showPokemonTypes", ShowType.SHOW);
        showPokemonRewardEvs = builder.defineEnum("showPokemonRewardEvs", ShowType.SHOW);
        showPokemonNature = builder.defineEnum("showPokemonNature", ShowType.SHOW);
        showPokemonAbility = builder.defineEnum("showPokemonAbility", ShowType.SHOW);
        showPokemonIvs = builder.defineEnum("showPokemonIvs", ShowType.SHOW);
        showPokemonEvs = builder.defineEnum("showPokemonEvs", ShowType.SHOW);
        showPokemonDexEntry =  builder.defineEnum("showPokemonDexEntry", ShowType.SNEAK);
        builder.pop().push("misc");
        showHealerEnergy = builder.defineEnum("showHealerEnergy", ShowType.SHOW);
        builder.pop().pop();
    }
}
