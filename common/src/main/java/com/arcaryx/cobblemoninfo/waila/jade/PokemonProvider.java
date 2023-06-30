package com.arcaryx.cobblemoninfo.waila.jade;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.config.ShowType;
import com.arcaryx.cobblemoninfo.util.PokemonUtils;
import com.arcaryx.cobblemoninfo.util.TextUtils;
import com.arcaryx.cobblemoninfo.waila.TooltipType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.ui.HealthElement;

import java.util.List;
import java.util.stream.StreamSupport;

public enum PokemonProvider implements IEntityComponentProvider, IServerDataProvider<Entity> {
    INSTANCE;

    public static final String TAG_GENDER = "ci_gender";
    public static final String TAG_TRAINER_NAME = "ci_trainer_name";
    public static final String TAG_NATURE_NAME = "ci_nature_name";
    public static final String TAG_MINTED_NATURE_NAME = "ci_minted_nature_name";
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
        var tooltips = CobblemonInfo.CONFIG.getPokemonTooltips();

        if (configContains(tooltips, TooltipType.TITLE_GENDER_SPECIES)) {
            data.putString(TAG_GENDER, pokemon.getGender().getShowdownName());
        }

        if (configContains(tooltips, TooltipType.TRAINER) && pokemon.getOwnerUUID() != null) {
            var trainer = level.getPlayerByUUID(pokemon.getOwnerUUID());
            if (trainer != null) {
                data.putString(TAG_TRAINER_NAME, trainer.getDisplayName().getString());
            }
        }

        if (configContains(tooltips, TooltipType.FRIENDSHIP) && !pokemon.isWild()) {
            data.putInt(TAG_FRIENDSHIP, pokemon.getFriendship());
        }

        if (configContains(tooltips, TooltipType.REWARD_EVS)) {
            data.put(TAG_EV_YIELD, PokemonUtils.saveStatMapToCompoundTag(pokemon.getForm().getEvYield()));
        }

        if (configContains(tooltips, TooltipType.NATURE)) {
            data.putString(TAG_NATURE_NAME, pokemon.getNature().getDisplayName());
            if (pokemon.getMintedNature() != null) {
                data.putString(TAG_MINTED_NATURE_NAME, pokemon.getMintedNature().getDisplayName());
            }
        }

        if (configContains(tooltips, TooltipType.ABILITY)) {
            data.putString(TAG_ABILITY_NAME, pokemon.getAbility().getDisplayName());
            data.putBoolean(TAG_ABILITY_HIDDEN, PokemonUtils.hasHiddenAbility(pokemon));
        }

        if (configContains(tooltips, TooltipType.IVS)) {
            data.put(TAG_IVS, pokemon.getIvs().saveToNBT(new CompoundTag()));
        }

        if (configContains(tooltips, TooltipType.EVS)) {
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
        tooltip.clear();
        var tooltips = CobblemonInfo.CONFIG.getPokemonTooltips();

        for (var type : tooltips) {
            if (type.getRight() == ShowType.SHOW) {
                addTooltip(type.getLeft(), tooltip, accessor, pokemonEntity, pokemon);
            } else if (type.getRight() == ShowType.SNEAK && accessor.getPlayer().isCrouching()) {
                addTooltip(type.getLeft(), tooltip, accessor, pokemonEntity, pokemon);
            }
        }
        if (!accessor.getPlayer().isCrouching() && tooltips.stream().anyMatch(x -> x.getRight() == ShowType.SNEAK)) {
            tooltip.add(Component.literal("<sneak for additional info>").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CobblemonJadePlugin.POKEMON_ENTITY;
    }

    private boolean configContains(List<Pair<TooltipType, ShowType>> tooltips, TooltipType type) {
        return tooltips.stream().anyMatch(x -> x.getLeft() == type && x.getRight() != ShowType.HIDE);
    }

    private void addTooltip(TooltipType tooltipType, ITooltip tooltip, EntityAccessor accessor, PokemonEntity pokemonEntity, Pokemon pokemon) {
        var data = accessor.getServerData();
        switch (tooltipType) {
            case TITLE_SPECIES -> {
                tooltip.add(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));
            }
            case TITLE_GENDER_SPECIES -> {
                var gender = data.contains(TAG_GENDER) ? PokemonUtils.getGenderFromShowdownName(data.getString(TAG_GENDER)) : Gender.GENDERLESS;
                if (gender != Gender.GENDERLESS) {
                    var prefix = gender == Gender.MALE ? ChatFormatting.BLUE + "\u2642 " : ChatFormatting.LIGHT_PURPLE + "\u2640 ";
                    var component = Component.literal(prefix).append(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));
                    tooltip.add(component);
                } else {
                    tooltip.add(pokemon.getDisplayName().withStyle(ChatFormatting.WHITE));
                }
            }
            case HEALTH -> {
                tooltip.add(new HealthElement(pokemonEntity.getMaxHealth(), pokemonEntity.getHealth()));
            }
            case TRAINER -> {
                if (data.contains(TAG_TRAINER_NAME)) {
                    tooltip.add(Component.literal("Trainer: ").append(data.getString(TAG_TRAINER_NAME)));
                }
            }
            case FRIENDSHIP -> {
                if (data.contains(TAG_FRIENDSHIP)) {
                    double percentage = (data.getInt(TAG_FRIENDSHIP) / 255.0F) * 100;
                    int flooredPercentage = (int)Math.floor(percentage);
                    tooltip.add(Component.literal(String.format("Friendship: %d (%d%%)", data.getInt(TAG_FRIENDSHIP), flooredPercentage)));
                }
            }
            case TYPES -> {
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
            case REWARD_EVS -> {
                if (data.contains(TAG_EV_YIELD)) {
                    var yield = TextUtils.formatEvYield(PokemonUtils.loadStatMapFromCompoundTag(data.getCompound(TAG_EV_YIELD)));
                    tooltip.add(Component.literal("EV Yield: ").append(yield));
                }
            }
            case NATURE -> {
                if (data.contains(TAG_NATURE_NAME)) {
                    if (!data.contains(TAG_MINTED_NATURE_NAME)) {
                        tooltip.add(Component.literal("Nature: ").append(Component.translatable(data.getString(TAG_NATURE_NAME))));
                    } else {
                        tooltip.add(Component.literal("Nature: ")
                                .append(Component.translatable(data.getString(TAG_MINTED_NATURE_NAME)))
                                .append(Component.literal(" (Minted)")));
                    }
                }
            }
            case ABILITY -> {
                if (data.contains(TAG_ABILITY_HIDDEN)) {
                    var abilityComponent = Component.literal("Ability: ").append(Component.translatable(data.getString(TAG_ABILITY_NAME)));
                    if (data.contains(TAG_ABILITY_HIDDEN) && data.getBoolean(TAG_ABILITY_HIDDEN)) {
                        abilityComponent.append(Component.literal(" (Hidden)"));
                    }
                    tooltip.add(abilityComponent);
                }
            }
            case IVS -> {
                if (data.contains(TAG_IVS)) {
                    var ivs = pokemon.getIvs().loadFromNBT(data.getCompound(TAG_IVS));
                    tooltip.add(Component.literal("IVs: " + TextUtils.formatStats(ivs, 31 * 6)));
                }
            }
            case EVS -> {
                if (data.contains(TAG_EVS)) {
                    var evs = pokemon.getEvs().loadFromNBT(data.getCompound(TAG_EVS));
                    tooltip.add(Component.literal("EVs: " + TextUtils.formatStats(evs, 510)));
                }
            }
            case DEX_ENTRY -> {
                if (pokemon.getForm().getPokedex().size() > 0) {
                    var dex = pokemon.getForm().getPokedex().stream().findFirst().orElse("");
                    var dexLines = TextUtils.wrapString("Dex Entry: " + I18n.get(dex), 32);
                    for (var line : dexLines) {
                        tooltip.add(Component.literal(line));
                    }
                }
            }
        }
    }




}
