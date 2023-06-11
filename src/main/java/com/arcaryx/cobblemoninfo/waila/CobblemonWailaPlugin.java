package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(CobblemonWailaPlugin.ID)
public class CobblemonWailaPlugin implements IWailaPlugin {
    public static final String ID = "cobblemon";
    public static final ResourceLocation POKEMON_ENTITY_PROVIDER = new ResourceLocation(ID, "pokemon_entity_provider");
    public static final ResourceLocation POKEMON_ENTITY_COMPONENT = new ResourceLocation(ID, "pokemon_entity_component");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(PokemonEntityProvider.INSTANCE, PokemonEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        if (CobblemonInfo.COMMON.removePokemonTooltip.get())
            registration.hideTarget(CobblemonEntities.POKEMON.get());
        else
            registration.registerEntityComponent(PokemonEntityComponent.INSTANCE, PokemonEntity.class);
    }
}
