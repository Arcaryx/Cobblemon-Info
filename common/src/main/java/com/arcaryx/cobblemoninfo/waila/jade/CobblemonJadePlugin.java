package com.arcaryx.cobblemoninfo.waila.jade;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.cobblemon.mod.common.block.ApricornBlock;
import com.cobblemon.mod.common.block.HealingMachineBlock;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(CobblemonInfo.MOD_ID)
public class CobblemonJadePlugin implements IWailaPlugin {
    public static final ResourceLocation POKEMON_ENTITY = new ResourceLocation(CobblemonInfo.MOD_ID, "pokemon_entity");
    public static final ResourceLocation HEALER = new ResourceLocation(CobblemonInfo.MOD_ID, "healer");
    public static final ResourceLocation APRICORN = new ResourceLocation(CobblemonInfo.MOD_ID, "apricorn");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(PokemonProvider.INSTANCE, PokemonEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(PokemonProvider.INSTANCE, PokemonEntity.class);
        registration.registerBlockComponent(HealerProvider.INSTANCE,  HealingMachineBlock.class);
        registration.registerBlockComponent(ApricornProvider.INSTANCE,  ApricornBlock.class);
    }
}
