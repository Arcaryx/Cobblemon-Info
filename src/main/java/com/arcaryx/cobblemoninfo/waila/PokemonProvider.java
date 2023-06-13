package com.arcaryx.cobblemoninfo.waila;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.CommonConfig;
import com.arcaryx.cobblemoninfo.util.PokemonUtils;
import com.arcaryx.cobblemoninfo.util.TextUtils;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.HealthElement;

import java.util.stream.StreamSupport;

public enum PokemonProvider implements IEntityComponentProvider, IServerDataProvider<Entity> {
    INSTANCE;

    public static final String TAG_GENDER = "ci_gender";
    public static final String TAG_TRAINER_NAME = "ci_trainer_name";
    public static final String TAG_NATURE_NAME = "ci_nature_name";
    public static final String TAG_ABILITY_NAME = "ci_ability_name";
    public static final String TAG_ABILITY_HIDDEN = "ci_ability_hidden";
    public static final String TAG_EV_YIELD = "ci_yield";
    public static final String TAG_IVS = "ci_ivs";
    public static final String TAG_EVS = "ci_evs";

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, Entity entity, boolean b) {
        if (!(entity instanceof PokemonEntity pokemonEntity))
            return;

        var pokemon = pokemonEntity.getPokemon();

        if (CobblemonInfo.config.showPokemonGender != CommonConfig.ShowType.HIDE)
            data.putString(TAG_GENDER, pokemon.getGender().getShowdownName());

        if (CobblemonInfo.config.showPokemonTrainer != CommonConfig.ShowType.HIDE && pokemon.getOwnerUUID() != null) {
            var trainer = level.getPlayerByUUID(pokemon.getOwnerUUID());
            // TODO: Handle offline players when cobblemon adds ranch blocks
            if (trainer != null)
                data.putString(TAG_TRAINER_NAME, trainer.getDisplayName().getString());
        }

        if (CobblemonInfo.config.showPokemonRewardEvs != CommonConfig.ShowType.HIDE) {
            data.put(TAG_EV_YIELD, PokemonUtils.saveStatMapToCompoundTag(pokemon.getForm().getEvYield()));
        }



        if (CobblemonInfo.config.showPokemonNature != CommonConfig.ShowType.HIDE)
            data.putString(TAG_NATURE_NAME, pokemon.getNature().getDisplayName());

        if (CobblemonInfo.config.showPokemonAbility != CommonConfig.ShowType.HIDE) {
            data.putString(TAG_ABILITY_NAME, pokemon.getAbility().getDisplayName());
            data.putBoolean(TAG_ABILITY_HIDDEN, PokemonUtils.hasHiddenAbility(pokemon));
        }

        if (CobblemonInfo.config.showPokemonIvs != CommonConfig.ShowType.HIDE)
            data.put(TAG_IVS, pokemon.getIvs().saveToNBT(new CompoundTag()));

        if (CobblemonInfo.config.showPokemonEvs != CommonConfig.ShowType.HIDE)
            data.put(TAG_EVS, pokemon.getEvs().saveToNBT(new CompoundTag()));


    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!(accessor.getEntity() instanceof PokemonEntity pokemonEntity))
            return;
        var pokemon = pokemonEntity.getPokemon();
        var data = accessor.getServerData();
        tooltip.clear();


        var showGender = data.contains(TAG_GENDER) ? CobblemonInfo.config.showPokemonGender : CommonConfig.ShowType.HIDE;
        var gender = PokemonUtils.getGenderFromShowdownName(data.getString(TAG_GENDER));
        if (gender == Gender.GENDERLESS)
            showGender = CommonConfig.ShowType.HIDE;
        if (showGender == CommonConfig.ShowType.SHOW || (showGender == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(Component.literal(gender == Gender.MALE ? ChatFormatting.BLUE + "\u2642 " :
                    ChatFormatting.LIGHT_PURPLE + "\u2640 ").append(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE)));
        else
            tooltip.add(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));

        var showHealth = CobblemonInfo.config.showPokemonHealth;
        if (showHealth == CommonConfig.ShowType.SHOW || (showHealth == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(new HealthElement(pokemonEntity.getMaxHealth(), pokemonEntity.getHealth()));

        var showTrainer = data.contains(TAG_TRAINER_NAME) ? CobblemonInfo.config.showPokemonTrainer : CommonConfig.ShowType.HIDE;
        if (showTrainer == CommonConfig.ShowType.SHOW || (showTrainer == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(Component.literal("Trainer: ").append(data.getString(TAG_TRAINER_NAME)));

        var showTypes = CobblemonInfo.config.showPokemonTypes;
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

        var showRewardIvs = data.contains(TAG_EV_YIELD) ? CobblemonInfo.config.showPokemonRewardEvs : CommonConfig.ShowType.HIDE;
        if (showRewardIvs == CommonConfig.ShowType.SHOW || (showRewardIvs == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching()))
            tooltip.add(Component.literal("EV Yield: ")
                    .append(TextUtils.formatEvYield(PokemonUtils.loadStatMapFromCompoundTag(data.getCompound(TAG_EV_YIELD)))));

        var showNature = !data.contains(TAG_NATURE_NAME) ? CommonConfig.ShowType.HIDE : CobblemonInfo.config.showPokemonNature;
        if (showNature == CommonConfig.ShowType.SHOW || (showNature == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            // TODO: Support minted abilities
            tooltip.add(Component.literal("Nature: ").append(Component.translatable(data.getString(TAG_NATURE_NAME))));
        }

        var showAbility = !data.contains(TAG_ABILITY_HIDDEN) ? CommonConfig.ShowType.HIDE : CobblemonInfo.config.showPokemonAbility;
        if (showAbility == CommonConfig.ShowType.SHOW || (showAbility == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var abilityComponent = Component.literal("Ability: ").append(Component.translatable(data.getString(TAG_ABILITY_NAME)));
            if (data.contains(TAG_ABILITY_HIDDEN) && data.getBoolean(TAG_ABILITY_HIDDEN))
                abilityComponent.append(Component.literal(" (Hidden)"));
            tooltip.add(abilityComponent);
        }

        var showIvs = !data.contains(TAG_IVS) ? CommonConfig.ShowType.HIDE : CobblemonInfo.config.showPokemonIvs;
        if (showIvs == CommonConfig.ShowType.SHOW || (showIvs == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var ivs = pokemon.getIvs().loadFromNBT(data.getCompound(TAG_IVS));
            tooltip.add(Component.literal("IVs: " + TextUtils.formatStats(ivs, 31 * 6)));
        }

        var showEvs =  !data.contains(TAG_EVS) ? CommonConfig.ShowType.HIDE : CobblemonInfo.config.showPokemonEvs;
        if (showEvs == CommonConfig.ShowType.SHOW || (showEvs == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var evs = pokemon.getEvs().loadFromNBT(data.getCompound(TAG_EVS));
            tooltip.add(Component.literal("EVs: " + TextUtils.formatStats(evs, 510)));
        }

        var showDex = pokemon.getForm().getPokedex().size() > 0 ? CobblemonInfo.config.showPokemonDexEntry : CommonConfig.ShowType.HIDE;
        if (showDex == CommonConfig.ShowType.SHOW || (showDex == CommonConfig.ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var dex = pokemon.getForm().getPokedex().stream().findFirst().orElse("");
            var dexLines = TextUtils.wrapString("Dex Entry: " + I18n.get(dex), 32);
            for (var line : dexLines)
                tooltip.add(Component.literal(line));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonWailaPlugin.POKEMON_ENTITY;
    }
}
