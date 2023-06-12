package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.cobblemon.mod.common.CobblemonEntities;
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

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(PokemonProvider.INSTANCE, PokemonEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        if (CobblemonInfo.COMMON.modifyPokemonTooltip.get())
            registration.registerEntityComponent(PokemonProvider.INSTANCE, PokemonEntity.class);
        if (CobblemonInfo.COMMON.showHealerEnergy.get() != CommonConfig.ShowType.HIDE)
            registration.registerBlockComponent(HealerProvider.INSTANCE,  HealingMachineBlock.class);
    }
}
