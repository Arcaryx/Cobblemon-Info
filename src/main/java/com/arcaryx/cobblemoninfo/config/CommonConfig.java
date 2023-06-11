package com.arcaryx.cobblemoninfo.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    // Jade - Pokemon
    public enum ShowType {
        HIDE,
        SNEAK,
        SHOW
    }

    public final ForgeConfigSpec.BooleanValue removePokemonTooltip;
    public final ForgeConfigSpec.EnumValue<ShowType> showPokemonGender, showPokemonHealth, showPokemonTrainer,
            showPokemonTypes, showPokemonRewardEvs, showPokemonNature, showPokemonAbility, showPokemonIvs, showPokemonEvs, showPokemonDexEntry;


    public CommonConfig(ForgeConfigSpec.Builder builder){
        builder.push("jade");
        removePokemonTooltip = builder
                .comment("Remove the Pokemon tooltip be completely.")
                .define("removePokemonTooltip", false);
        builder.push("pokemon");
        showPokemonGender = builder.defineEnum("showPokemonGender", ShowType.SHOW);
        showPokemonHealth = builder.defineEnum("showPokemonHealth", ShowType.SHOW);
        showPokemonTrainer = builder.defineEnum("showPokemonTrainer", ShowType.SHOW);
        showPokemonTypes = builder.defineEnum("showPokemonTypes", ShowType.SHOW);
        showPokemonRewardEvs = builder.defineEnum("showPokemonRewardEvs", ShowType.SHOW);
        showPokemonNature = builder.defineEnum("showPokemonNature", ShowType.SHOW);
        showPokemonAbility = builder.defineEnum("showPokemonAbility", ShowType.SHOW);
        showPokemonIvs = builder.defineEnum("showPokemonIvs", ShowType.SHOW);
        showPokemonEvs = builder.defineEnum("showPokemonEvs", ShowType.SHOW);
        showPokemonDexEntry =  builder.defineEnum("showPokemonDexEntry", ShowType.SNEAK);
        builder.pop().pop();
    }
}
