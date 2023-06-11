package com.arcaryx.cobblemoninfo.util;

import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.abilities.HiddenAbility;

public class PokemonUtils {

    public static boolean hasHiddenAbility(Pokemon pokemon) {
        var abilities = pokemon.getForm().getAbilities();
        for (var ability : abilities)
            if (ability instanceof HiddenAbility hiddenAbility && hiddenAbility.getTemplate() == pokemon.getAbility().getTemplate())
                return true;
        return false;
    }

    public static Gender getGenderFromShowdownName(String showdownName) {
        for (Gender gender : Gender.values()) {
            if (gender.getShowdownName().equals(showdownName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid showdownName: " + showdownName);
    }

}
