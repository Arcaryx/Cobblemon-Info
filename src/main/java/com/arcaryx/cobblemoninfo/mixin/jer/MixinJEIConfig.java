package com.arcaryx.cobblemoninfo.mixin.jer;

import com.arcaryx.cobblemoninfo.CobblemonInfo;
import com.arcaryx.cobblemoninfo.data.ClientCache;
import com.arcaryx.cobblemoninfo.mixin.cobblemon.PokemonAccessor;
import com.cobblemon.mod.common.CobblemonEntities;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import jeresources.entry.MobEntry;
import jeresources.jei.JEIConfig;
import jeresources.registry.MobRegistry;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(value = JEIConfig.class, remap = false)
public abstract class MixinJEIConfig {
    @Inject(method = "registerRecipes(Lmezz/jei/api/registration/IRecipeRegistration;)V", at=@At(value="HEAD"), require = 0)
    private void registerRecipes(IRecipeRegistration registration, CallbackInfo ci) {
        var pokemonSpecies = PokemonSpecies.INSTANCE.getSpecies();
        if (CobblemonInfo.config.hideNonPokemonDrops)
            MobRegistry.getInstance().clear();
        for (var species : pokemonSpecies) {
            var baseTexture = PokemonModelRepository.INSTANCE.getTexture(species.getResourceIdentifier(), Set.copyOf(species.getStandardForm().getAspects()), null);
            var forms = species.getForms().isEmpty() ? Arrays.asList(species.getStandardForm()) : species.getForms();
            for (var form : forms) {
                var formTexture = PokemonModelRepository.INSTANCE.getTexture(species.getResourceIdentifier(), Set.copyOf(form.getAspects()), null);
                var isBaseForm = form.getName().equals(species.getStandardForm().getName());
                var hasNewTexture = baseTexture != formTexture;
                var hasNewDrops = !ClientCache.sameDrops(species, form, species, species.getStandardForm());
                if (!isBaseForm && !hasNewTexture && !hasNewDrops)
                    continue;

                var drops = ClientCache.getPokemonDrops(species, form);
                var lootDrops = new LootDrop[drops.size()];
                for (var i = 0; i < lootDrops.length; i++) {
                    var drop = drops.get(i);
                    var item = Registry.ITEM.get(drop.getItem());
                    var range = drop.getRange();
                    lootDrops[i] = new LootDrop(item.getDefaultInstance(), range.getFirst(), range.getLast(), drop.getChance());
                }

                var entry = MobEntry.create(() -> {
                    var pokemon = new Pokemon();
                    var accessor = (PokemonAccessor)pokemon;
                    accessor.setIsClient(true);
                    pokemon.setSpecies(species);
                    var aspects = new HashSet<>(form.getAspects());
                    aspects.add(pokemon.getGender().name().toLowerCase());
                    pokemon.setAspects(aspects);
                    pokemon.updateForm();
                    return new PokemonEntity(CompatBase.getLevel(), pokemon, CobblemonEntities.POKEMON.get());
                }, LightLevel.any, lootDrops);
                MobRegistry.getInstance().registerMob(entry);
            }
        }
    }
}