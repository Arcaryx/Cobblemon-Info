package com.arcaryx.cobblemoninfo.jade;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.ShowType;
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
    public static final String TAG_FRIENDSHIP = "ci_friendship";
    public static final String TAG_ABILITY_NAME = "ci_ability_name";
    public static final String TAG_ABILITY_HIDDEN = "ci_ability_hidden";
    public static final String TAG_EV_YIELD = "ci_yield";
    public static final String TAG_IVS = "ci_ivs";
    public static final String TAG_EVS = "ci_evs";

    @Override
    public void appendServerData(CompoundTag data, ServerPlayer player, Level level, Entity entity, boolean b) {
        if (!CobblemonInfo.CONFIG.modifyPokemonTooltip()) {
            return;
        }

        if (!(entity instanceof PokemonEntity pokemonEntity)) {
            return;
        }

        var pokemon = pokemonEntity.getPokemon();

        if (CobblemonInfo.CONFIG.showPokemonGender() != ShowType.HIDE) {
            data.putString(TAG_GENDER, pokemon.getGender().getShowdownName());
        }

        if (CobblemonInfo.CONFIG.showPokemonTrainer() != ShowType.HIDE && pokemon.getOwnerUUID() != null) {
            var trainer = level.getPlayerByUUID(pokemon.getOwnerUUID());
            if (trainer != null) {
                data.putString(TAG_TRAINER_NAME, trainer.getDisplayName().getString());
            }
        }

        if (CobblemonInfo.CONFIG.showPokemonFriendship() != ShowType.HIDE && !pokemon.isWild()) {
            data.putInt(TAG_FRIENDSHIP, pokemon.getFriendship());
        }

        if (CobblemonInfo.CONFIG.showPokemonRewardEvs() != ShowType.HIDE) {
            data.put(TAG_EV_YIELD, PokemonUtils.saveStatMapToCompoundTag(pokemon.getForm().getEvYield()));
        }

        if (CobblemonInfo.CONFIG.showPokemonNature() != ShowType.HIDE) {
            data.putString(TAG_NATURE_NAME, pokemon.getNature().getDisplayName());
        }

        if (CobblemonInfo.CONFIG.showPokemonAbility() != ShowType.HIDE) {
            data.putString(TAG_ABILITY_NAME, pokemon.getAbility().getDisplayName());
            data.putBoolean(TAG_ABILITY_HIDDEN, PokemonUtils.hasHiddenAbility(pokemon));
        }

        if (CobblemonInfo.CONFIG.showPokemonIvs() != ShowType.HIDE) {
            data.put(TAG_IVS, pokemon.getIvs().saveToNBT(new CompoundTag()));
        }

        if (CobblemonInfo.CONFIG.showPokemonEvs() != ShowType.HIDE) {
            data.put(TAG_EVS, pokemon.getEvs().saveToNBT(new CompoundTag()));
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!CobblemonInfo.CONFIG.modifyPokemonTooltip()) {
            return;
        }

        if (!(accessor.getEntity() instanceof PokemonEntity pokemonEntity)) {
            return;
        }

        var pokemon = pokemonEntity.getPokemon();
        pokemon.setAspects(pokemonEntity.getAspects().get());
        pokemon.updateForm();
        var data = accessor.getServerData();
        tooltip.clear();

        var showGender = data.contains(TAG_GENDER) ? CobblemonInfo.CONFIG.showPokemonGender() : ShowType.HIDE;
        var gender = data.contains(TAG_GENDER) ? PokemonUtils.getGenderFromShowdownName(data.getString(TAG_GENDER)) : Gender.GENDERLESS;
        if (gender == Gender.GENDERLESS) {
            showGender = ShowType.HIDE;
        }
        if (showGender == ShowType.SHOW || (showGender == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var prefix = gender == Gender.MALE ? ChatFormatting.BLUE + "\u2642 " : ChatFormatting.LIGHT_PURPLE + "\u2640 ";
            var component = Component.literal(prefix).append(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));
            tooltip.add(component);
        } else {
            tooltip.add(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));
        }

        var showHealth = CobblemonInfo.CONFIG.showPokemonHealth();
        if (showHealth == ShowType.SHOW || (showHealth == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            tooltip.add(new HealthElement(pokemonEntity.getMaxHealth(), pokemonEntity.getHealth()));
        }

        var showTrainer = data.contains(TAG_TRAINER_NAME) ? CobblemonInfo.CONFIG.showPokemonTrainer() : ShowType.HIDE;
        if (showTrainer == ShowType.SHOW || (showTrainer == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            tooltip.add(Component.literal("Trainer: ").append(data.getString(TAG_TRAINER_NAME)));
        }

        var showFriendship = data.contains(TAG_FRIENDSHIP) ? CobblemonInfo.CONFIG.showPokemonFriendship() : ShowType.HIDE;
        if (showFriendship == ShowType.SHOW || (showFriendship == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            double percentage = (data.getInt(TAG_FRIENDSHIP) / 255.0F) * 100;
            int flooredPercentage = (int)Math.floor(percentage);
            tooltip.add(Component.literal(String.format("Friendship: %d (%d%%)", data.getInt(TAG_FRIENDSHIP), flooredPercentage)));
        }

        var showTypes = CobblemonInfo.CONFIG.showPokemonTypes();
        if (showTypes == ShowType.SHOW || (showTypes == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var types = StreamSupport.stream(pokemon.getForm().getTypes().spliterator(), false).toList();
            var typesComponent = Component.literal(TextUtils.basicPluralize("Type", types.size()) + ": ");
            for (var type : types) {
                if (typesComponent.getSiblings().size() > 0) {
                    typesComponent.append(Component.literal(", "));
                }
                typesComponent.append(type.getDisplayName().withStyle(Style.EMPTY.withColor(type.getHue())));
            }
            tooltip.add(typesComponent);
        }

        var showRewardIvs = data.contains(TAG_EV_YIELD) ? CobblemonInfo.CONFIG.showPokemonRewardEvs() : ShowType.HIDE;
        if (showRewardIvs == ShowType.SHOW || (showRewardIvs == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var yield = TextUtils.formatEvYield(PokemonUtils.loadStatMapFromCompoundTag(data.getCompound(TAG_EV_YIELD)));
            tooltip.add(Component.literal("EV Yield: ").append(yield));
        }

        var showNature = !data.contains(TAG_NATURE_NAME) ? ShowType.HIDE : CobblemonInfo.CONFIG.showPokemonNature();
        if (showNature == ShowType.SHOW || (showNature == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            tooltip.add(Component.literal("Nature: ").append(Component.translatable(data.getString(TAG_NATURE_NAME))));
        }

        var showAbility = !data.contains(TAG_ABILITY_HIDDEN) ? ShowType.HIDE : CobblemonInfo.CONFIG.showPokemonAbility();
        if (showAbility == ShowType.SHOW || (showAbility == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var abilityComponent = Component.literal("Ability: ").append(Component.translatable(data.getString(TAG_ABILITY_NAME)));
            if (data.contains(TAG_ABILITY_HIDDEN) && data.getBoolean(TAG_ABILITY_HIDDEN)) {
                abilityComponent.append(Component.literal(" (Hidden)"));
            }
            tooltip.add(abilityComponent);
        }

        var showIvs = !data.contains(TAG_IVS) ? ShowType.HIDE : CobblemonInfo.CONFIG.showPokemonIvs();
        if (showIvs == ShowType.SHOW || (showIvs == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var ivs = pokemon.getIvs().loadFromNBT(data.getCompound(TAG_IVS));
            tooltip.add(Component.literal("IVs: " + TextUtils.formatStats(ivs, 31 * 6)));
        }

        var showEvs =  !data.contains(TAG_EVS) ? ShowType.HIDE : CobblemonInfo.CONFIG.showPokemonEvs();
        if (showEvs == ShowType.SHOW || (showEvs == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var evs = pokemon.getEvs().loadFromNBT(data.getCompound(TAG_EVS));
            tooltip.add(Component.literal("EVs: " + TextUtils.formatStats(evs, 510)));
        }

        var showDex = pokemon.getForm().getPokedex().size() > 0 ? CobblemonInfo.CONFIG.showPokemonDexEntry() : ShowType.HIDE;
        if (showDex == ShowType.SHOW || (showDex == ShowType.SNEAK && accessor.getPlayer().isCrouching())) {
            var dex = pokemon.getForm().getPokedex().stream().findFirst().orElse("");
            var dexLines = TextUtils.wrapString("Dex Entry: " + I18n.get(dex), 32);
            for (var line : dexLines) {
                tooltip.add(Component.literal(line));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonJadePlugin.POKEMON_ENTITY;
    }
}
