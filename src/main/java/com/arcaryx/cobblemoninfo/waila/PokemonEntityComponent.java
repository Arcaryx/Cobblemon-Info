package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.arcaryx.cobblemoninfo.util.TextUtils;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
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
        if (showGender == CommonConfig.ShowType.SHOW || (showGender == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(Component.literal(pokemon.getGender() == Gender.MALE ? ChatFormatting.BLUE + "\u2642 " :
                    ChatFormatting.LIGHT_PURPLE + "\u2640 ").append(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE)));
        else
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
            var typesComponent = Component.literal(TextUtils.basicPluralize("Type", types.size()) + ": ");
            for (var type : types) {
                if (typesComponent.getSiblings().size() > 0)
                    typesComponent.append(Component.literal(", "));
                typesComponent.append(type.getDisplayName().withStyle(Style.EMPTY.withColor(type.getHue())));
            }
            tooltip.add(typesComponent);
        }

        var showRewardIvs = CobblemonInfo.COMMON.showPokemonRewardEvs.get();
        if (showRewardIvs == CommonConfig.ShowType.SHOW || (showRewardIvs == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(Component.literal("EV Yield: ").append(TextUtils.formatEvYield(pokemon.getForm().getEvYield())));

        var showNature = CobblemonInfo.COMMON.showPokemonNature.get();
        if (showNature == CommonConfig.ShowType.SHOW || (showNature == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            // TODO: Support minted abilities
            tooltip.add(Component.literal("Nature: ").append(Component.translatable(pokemon.getNature().getDisplayName())));
        }

        var showAbility = !data.contains(PokemonEntityProvider.TAG_ABILITY_HIDDEN) ? CommonConfig.ShowType.HIDE : CobblemonInfo.COMMON.showPokemonAbility.get();
        if (showAbility == CommonConfig.ShowType.SHOW || (showAbility == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var abilityComponent = Component.literal("Ability: ").append(Component.translatable(data.getString(PokemonEntityProvider.TAG_ABILITY_NAME)));
            if (data.contains(PokemonEntityProvider.TAG_ABILITY_HIDDEN) && data.getBoolean(PokemonEntityProvider.TAG_ABILITY_HIDDEN))
                abilityComponent.append(Component.literal(" (Hidden)"));
            tooltip.add(abilityComponent);
        }

        var showIvs = !data.contains(PokemonEntityProvider.TAG_IVS) ? CommonConfig.ShowType.HIDE : CobblemonInfo.COMMON.showPokemonIvs.get();
        if (showIvs == CommonConfig.ShowType.SHOW || (showIvs == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var ivs = pokemon.getIvs().loadFromNBT(data.getCompound(PokemonEntityProvider.TAG_IVS));
            tooltip.add(Component.literal("IVs: " + TextUtils.formatStats(ivs, 31 * 6)));
        }

        var showEvs =  !data.contains(PokemonEntityProvider.TAG_EVS) ? CommonConfig.ShowType.HIDE : CobblemonInfo.COMMON.showPokemonEvs.get();
        if (showEvs == CommonConfig.ShowType.SHOW || (showEvs == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var evs = pokemon.getEvs().loadFromNBT(data.getCompound(PokemonEntityProvider.TAG_EVS));
            tooltip.add(Component.literal("EVs: " + TextUtils.formatStats(evs, 510)));
        }

        var showDex = pokemon.getForm().getPokedex().size() > 0 ? CobblemonInfo.COMMON.showPokemonDexEntry.get() : CommonConfig.ShowType.HIDE;
        if (showDex == CommonConfig.ShowType.SHOW || (showDex == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var dex = pokemon.getForm().getPokedex().stream().findFirst().orElse("");
            var dexLines = TextUtils.wrapString("Dex Entry: " + I18n.get(dex), 32);
            for (var line : dexLines)
                tooltip.add(Component.literal(line));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonWailaPlugin.POKEMON_ENTITY_COMPONENT;
    }
}
