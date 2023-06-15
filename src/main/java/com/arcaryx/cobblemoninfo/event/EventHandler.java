package com.arcaryx.cobblemoninfo.event;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.data.PokemonDrop;
import com.arcaryx.cobblemoninfo.net.NetworkHandler;
import com.arcaryx.cobblemoninfo.net.message.PokemonDropMessage;
import com.cobblemon.mod.common.api.drop.ItemDropEntry;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.FormData;
import kotlin.ranges.IntRange;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import com.cobblemon.mod.common.pokemon.Species;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CobblemonInfo.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null)
            return;

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
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(event::getPlayer), new PokemonDropMessage(lootDrops));
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