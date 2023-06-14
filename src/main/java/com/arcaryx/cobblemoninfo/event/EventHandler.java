package com.arcaryx.cobblemoninfo.event;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.data.PokemonDrop;
import com.arcaryx.cobblemoninfo.net.NetworkHandler;
import com.arcaryx.cobblemoninfo.net.message.PokemonDropMessage;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Species;
import kotlin.ranges.IntRange;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    public static void onPlayerJoin(ServerPlayer player) {
        NetworkHandler.sendConfigToPlayer(player, CobblemonInfo.config);

        List<PokemonDrop> lootDrops = new ArrayList<>();
        var pokemonSpecies = PokemonSpecies.INSTANCE.getSpecies();
        for (var species : pokemonSpecies) {
            var forms = species.getForms();
            if (forms.isEmpty()) {
                var form = species.getStandardForm();
                addDrops(lootDrops, species, form);
                continue;
            }
            for (var form : species.getForms()) {
                addDrops(lootDrops, species, form);
            }
        }
        NetworkHandler.sendDropsToPlayer(player, new PokemonDropMessage(lootDrops));
    }

    private static void addDrops(List<PokemonDrop> lootDrops, Species species, FormData form) {
        var formDrops = form.getDrops().getEntries();
        for (var drop : formDrops) {
            if (!(drop instanceof ItemDropEntry itemDrop))
                continue;
            var range = itemDrop.getQuantityRange();
            if (range == null)
                range = new IntRange(itemDrop.getQuantity(), itemDrop.getQuantity());
            var chance = itemDrop.getPercentage() / 100;
            lootDrops.add(new PokemonDrop(species.getResourceIdentifier(), form.getName(), itemDrop.getItem(), chance, range.getFirst(), range.getLast()));
        }
    }
}
