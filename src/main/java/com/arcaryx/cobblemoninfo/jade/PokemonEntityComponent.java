package com.arcaryx.cobblemoninfo.jade;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

import java.util.stream.StreamSupport;

public enum PokemonEntityComponent implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!(accessor.getEntity() instanceof PokemonEntity pokemonEntity) || Minecraft.getInstance().level == null)
            return;
        var pokemon = pokemonEntity.getPokemon();
        var data = accessor.getServerData();
        var health = tooltip.get(1, IElement.Align.LEFT);
        tooltip.clear();

        var showGender = pokemon.getGender() == Gender.GENDERLESS ? CommonConfig.ShowType.HIDE : CobblemonInfo.COMMON.showPokemonGender.get();
        if (showGender == CommonConfig.ShowType.SHOW || (showGender == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            tooltip.add(Component.literal(pokemon.getGender() == Gender.MALE ? ChatFormatting.BLUE + "\u2642 " :
                    ChatFormatting.LIGHT_PURPLE + "\u2640 ").append(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE)));
        } else
            tooltip.add(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));

        var showHealth = CobblemonInfo.COMMON.showPokemonHealth.get();
        if (showHealth == CommonConfig.ShowType.SHOW || (showHealth == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(health);

        var showTrainer = data.contains(PokemonEntityProvider.TAG_TRAINER_NAME) ? CobblemonInfo.COMMON.showPokemonTrainer.get() : CommonConfig.ShowType.HIDE;
        if (showTrainer == CommonConfig.ShowType.SHOW || (showTrainer == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(Component.literal("Trainer: ").append(data.getString(PokemonEntityProvider.TAG_TRAINER_NAME)));

        var showTypes = CobblemonInfo.COMMON.showPokemonTypes.get();
        if (showTypes == CommonConfig.ShowType.SHOW || (showTypes == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var types = StreamSupport.stream(pokemon.getForm().getTypes().spliterator(), false).toList();
            var typesComponent = Component.literal(String.format("Type%s: ", types.size() > 1 ? "s" : ""));
            for (var type : types) {
                if (typesComponent.getSiblings().size() > 0)
                    typesComponent.append(Component.literal(", "));
                typesComponent.append(type.getDisplayName().withStyle(Style.EMPTY.withColor(type.getHue())));
            }
            tooltip.add(typesComponent);
        }






    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonPlugin.POKEMON_ENTITY_COMPONENT;
    }
}
