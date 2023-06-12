package com.arcaryx.cobblemoninfo.data;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientCache {
    private static Map<Species, List<PokemonDrop>> pokemonDrops = new HashMap<>();

    public static void setPokemonDrops(List<PokemonDrop> drops) {
        for (var drop : drops) {
            var species = PokemonSpecies.INSTANCE.getByIdentifier(drop.getSpecies());
            if (species == null)
                continue;
            if (!pokemonDrops.containsKey(species))
                pokemonDrops.put(species, new ArrayList<>());
            pokemonDrops.get(species).add(drop);
        }
    }

    public static List<PokemonDrop> getPokemonDrops(Species species) {
        if (pokemonDrops.containsKey(species))
            return pokemonDrops.get(species);
        return new ArrayList<>();
    }
}
