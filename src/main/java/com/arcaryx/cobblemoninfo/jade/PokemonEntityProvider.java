package com.arcaryx.cobblemoninfo.jade;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.arcaryx.cobblemoninfo.util.PokemonUtils;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import snownee.jade.api.IServerDataProvider;

public enum PokemonEntityProvider implements IServerDataProvider<Entity> {
    INSTANCE;

    public static final String TAG_TRAINER_NAME = "ci_trainer_name";
    public static final String TAG_ABILITY_NAME = "ci_ability_name";
    public static final String TAG_ABILITY_HIDDEN = "ci_ability_hidden";

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, Entity entity, boolean b) {
        if (!(entity instanceof PokemonEntity pokemonEntity))
            return;
        var pokemon = pokemonEntity.getPokemon();
        if (CobblemonInfo.COMMON.showPokemonTrainer.get() != CommonConfig.ShowType.HIDE && pokemon.getOwnerUUID() != null) {
            var trainer = level.getPlayerByUUID(pokemon.getOwnerUUID());
            // TODO: Handle offline players when cobblemon adds ranch blocks
            if (trainer != null)
                data.putString(TAG_TRAINER_NAME, trainer.getDisplayName().getString());
        }

        if (CobblemonInfo.COMMON.showPokemonAbility.get() != CommonConfig.ShowType.HIDE) {
            data.putString(TAG_ABILITY_NAME, pokemon.getAbility().getDisplayName());
            data.putBoolean(TAG_ABILITY_HIDDEN, PokemonUtils.hasHiddenAbility(pokemon));
        }

    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonPlugin.POKEMON_ENTITY_PROVIDER;
    }
}
