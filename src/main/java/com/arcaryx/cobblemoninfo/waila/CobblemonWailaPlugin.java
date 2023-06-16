package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.cobblemon.mod.common.block.ApricornBlock;
import com.cobblemon.mod.common.block.HealingMachineBlock;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(CobblemonWailaPlugin.ID)
public class CobblemonWailaPlugin implements IWailaPlugin {
    public static final String ID = "cobblemon";
    public static final ResourceLocation POKEMON_ENTITY = new ResourceLocation(ID, "pokemon_entity");
    public static final ResourceLocation HEALER = new ResourceLocation(ID, "healer");
    public static final ResourceLocation APRICORN = new ResourceLocation(ID, "apricorn");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(PokemonProvider.INSTANCE, PokemonEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        if (CobblemonInfo.config.modifyPokemonTooltip)
            registration.registerEntityComponent(PokemonProvider.INSTANCE, PokemonEntity.class);
        if (CobblemonInfo.config.showHealerEnergy != CommonConfig.ShowType.HIDE)
            registration.registerBlockComponent(HealerProvider.INSTANCE,  HealingMachineBlock.class);
        if (CobblemonInfo.config.showApricornProgress != CommonConfig.ShowType.HIDE)
            registration.registerBlockComponent(ApricornProvider.INSTANCE,  ApricornBlock.class);
    }
}
